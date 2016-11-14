package com.niz.d.playerplaylist.view;

public interface PlayerViewInterface {

    public abstract void prepare();

    public abstract void pause();

    public abstract void play();

    public void update(int time, int duration, String formattedTime);
}
