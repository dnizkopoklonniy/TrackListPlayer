package com.niz.d.playerplaylist.model;

import android.net.Uri;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class PlayList {

    @Setter
    @Getter
    private int id;

    /**
     * Duration all tracks.
     */
    @Setter
    @Getter
    private int duration;

    /**
     * Current time of
     */
    @Setter
    @Getter
    private int time = 0;

    @Setter
    @Getter
    private State state = State.PAUSE;

    public enum State{
        PREPARE(0),
        PAUSE(1),
        PLAY(2);

        State(int value){
            this.value = value;
        }

        private int value;
    }

    @Setter
    @Getter
    private List<Uri> tracks;
}
