package com.niz.d.playerplaylist.controller;

public interface PlayerControllerInterface {

    /**
     * Press button. May be start, loading or pause.
     */
    public abstract void press();

    public abstract void updateView();
}
