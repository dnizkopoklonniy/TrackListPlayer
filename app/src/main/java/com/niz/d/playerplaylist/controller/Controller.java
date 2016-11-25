package com.niz.d.playerplaylist.controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Pair;

import com.niz.d.playerplaylist.model.PlayList;
import com.niz.d.playerplaylist.util.TimeFormatter;
import com.niz.d.playerplaylist.view.PlayerViewInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

public class Controller implements PlayerControllerInterface{

    public Controller(Context context, PlayerViewInterface view, PlayList model) {
        this.context = context;
        this.view = view;
        this.model = model;

        view.pause();
        initPlayers();
    }

    private Context context;
    private PlayerViewInterface view;
    private PlayList model;

    private void initPlayers() {
        // Init-s media players for load songs.
        for (Uri uri: model.getTracks()) {
            players.add(createPlayer(uri));
        }

        // Calculate duration.
        int duration = 0;
        for (int i = 0; i< model.getTracks().size(); i++) {
            MediaPlayer player = players.get(i);
            durations.add(player.getDuration());
            duration += player.getDuration();
        }

        model.setTime(0);
        model.setDuration(duration);

        players.clear();
        for (int i = 0; i< model.getTracks().size(); i++) {
            MediaPlayer player = createPlayer(model.getTracks().get(i));
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            players.add(player);
        }
    }

    private MediaPlayer createPlayer(Uri uri) {
        MediaPlayer player = new MediaPlayer();
        try {
            player.setDataSource(context, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return player;
    }

    private int currentTrack = 0;
    private int currentTrackTime = 0;

    private List<MediaPlayer> players = new ArrayList<>();
    private List<Integer> durations = new ArrayList<>();

    @Override
    public void press() {
        if (model.getState() == PlayList.State.PREPARE)
            return;

        if (model.getState() == PlayList.State.PAUSE) {
            model.setState(PlayList.State.PREPARE);
            view.prepare();

            MediaPlayer currentPlayer = players.get(currentTrack);
            subscribePlayer(currentPlayer);
            return;
        }

        if (model.getState() == PlayList.State.PLAY) {
            model.setState(PlayList.State.PAUSE);
            view.pause();

            if (!subscription.isUnsubscribed()) {
                subscription.unsubscribe();

                Uri uri = model.getTracks().get(currentTrack);
                MediaPlayer mediaPlayer = createPlayer(uri);

                players.remove(currentTrack);
                players.add(currentTrack, mediaPlayer);
            }

            return;
        }
    }

    @Override
    public void updateView() {
    }

    private void subscribePlayer(MediaPlayer mediaPlayer) {
        RxMediaPlayer.songTime = currentTrackTime;
        subscription = RxMediaPlayer
                .play(mediaPlayer)
                .subscribe(new Subscriber<Pair<Integer, Integer>>() {
                    @Override
                    public void onCompleted() {
                        model.setTime(model.getTime() + durations.get(currentTrack));

                        // Prepare new player.
                        Uri uri = model.getTracks().get(currentTrack);
                        players.remove(currentTrack);
                        players.add(currentTrack, createPlayer(uri));

                        currentTrack++;
                        currentTrackTime = 0;

                        if (currentTrack < model.getTracks().size()) {
                            subscribePlayer(players.get(currentTrack));
                        }else{
                            currentTrack = 0;

                            model.setTime(0);
                            model.setState(PlayList.State.PAUSE);

                            view.pause();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Pair<Integer, Integer> time) {
                        int position = time.first;

                        model.setState(PlayList.State.PLAY);
                        currentTrackTime = position;
                        view.play();

                        String formattedTime = new TimeFormatter().formatTime(model.getDuration() - (model.getTime() + position));
                        view.update(model.getTime() + position, model.getDuration(), formattedTime);
                    }
                });

    }

    private Subscription subscription;
}