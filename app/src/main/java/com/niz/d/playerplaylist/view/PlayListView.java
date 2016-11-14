package com.niz.d.playerplaylist.view;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

public class PlayListView implements PlayerViewInterface{

    public PlayListView(Context context, TextView statusBar, Button action) {
        this.context = context;
        this.statusBar = statusBar;
        this.action = action;
    }

    private Context context;
    private TextView statusBar;
    private Button action;

    @Override
    public void prepare() {
        action.setText("prepare");
    }

    @Override
    public void pause() {
        action.setText("play");
    }

    @Override
    public void play() {
        action.setText("pause");
    }

    @Override
    public void update(int time, int duration, String formattedTime) {
        statusBar.setText(formattedTime);
    }
}
