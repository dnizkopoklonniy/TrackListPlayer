package com.niz.d.playerplaylist.controller;

/*
MIT License

Copyright (c) 2015 Adel Nizamutdinov

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Pair;
import org.jetbrains.annotations.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public class RxMediaPlayer {

    public static int songTime = 0;

    public static @NotNull Observable<MediaPlayer> from(@NotNull File file) {
        return Observable.create(subscriber -> {
            final MediaPlayer player = create();
            try {
                player.setDataSource(file.getAbsolutePath());
                subscriber.onNext(player);
                subscriber.onCompleted();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    public static @NotNull Observable<Pair<Integer, Integer>> play(@NotNull MediaPlayer mp) {
        return prepare(mp).flatMap(RxMediaPlayer::stream)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    static @NotNull MediaPlayer create() {
        final MediaPlayer player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return player;
    }

    static @NotNull Observable<MediaPlayer> prepare(@NotNull MediaPlayer mp) {
        return Observable.create(subscriber -> {
            subscriber.onNext(mp);
            subscriber.onCompleted();
        });
    }

    static @NotNull Observable<Pair<Integer, Integer>> stream(@NotNull MediaPlayer mp) {
        return Observable.create(subscriber -> {
            subscriber.add(Subscriptions.create(() -> {
                if (mp.isPlaying()) {
                    mp.stop();
                }
                mp.reset();
                mp.release();
            }));
            mp.seekTo(songTime);
            mp.start();
            subscriber.add(ticks(mp)
                                   .takeUntil(complete(mp))
                                   .subscribe(subscriber));
        });
    }

    static @NotNull Observable<Pair<Integer, Integer>> ticks(@NotNull MediaPlayer mp) {
        return Observable.interval(16, TimeUnit.MILLISECONDS)
                .map(y -> Pair.create(mp.getCurrentPosition(), mp.getDuration()));
    }

    static @NotNull Observable<MediaPlayer> complete(@NotNull MediaPlayer player) {
        return Observable.create(subscriber -> player.setOnCompletionListener(mp -> {
            subscriber.onNext(mp);
            subscriber.onCompleted();
        }));
    }
}
