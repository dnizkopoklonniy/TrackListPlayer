package com.niz.d.playerplaylist;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.niz.d.playerplaylist.controller.Controller;
import com.niz.d.playerplaylist.controller.PlayerControllerInterface;
import com.niz.d.playerplaylist.model.PlayList;
import com.niz.d.playerplaylist.util.SongUtil;
import com.niz.d.playerplaylist.view.PlayListView;
import com.niz.d.playerplaylist.view.PlayerViewInterface;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        statusBar = (TextView) findViewById(R.id.status_bar);
        action = (Button) findViewById(R.id.action);
        cardView = (CardView) findViewById(R.id.card_report);

        countCards = (TextView) findViewById(R.id.count_cards);
        countCards.setIncludeFontPadding(false);

        // Gets uri models.
        List<Uri> tracks = getTrackUri();
        model = new PlayList();
        model.setTracks(tracks);

        // Settings of player controller.
        view = new PlayListView(context, statusBar, action);
        controller = new Controller(context, view, model);

        action.setOnClickListener(view->{
        });
    }

    private TextView statusBar;
    private Button action;

    private ImageView popupMore;
    private CardView cardView;
    private TextView countCards;

    private PlayerViewInterface view;
    private PlayList model;
    private PlayerControllerInterface controller;

    private Context context;
    private static final String TAG = MainActivity.class.getSimpleName();

    private List<Uri> getTrackUri() {

        List<Integer> trackId = new ArrayList<>();
        List<String> trackUrl = new ArrayList<>();
        List<Uri> trackUri = new ArrayList<>();

        trackId.add(111544);
        trackId.add(111582);
        trackId.add(111603);
        trackId.add(111604);

        for (int id: trackId) {
            String url = SongUtil.getUrl(id);
            Uri uri = Uri.parse(url);

            trackUrl.add(url);
            trackUri.add(uri);
        }

        return trackUri;
    }
}
