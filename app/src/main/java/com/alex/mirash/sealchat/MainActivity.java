package com.alex.mirash.sealchat;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alex.mirash.sealchat.chat.view.ChatView;

public class MainActivity extends AppCompatActivity {
    private SlidingPaneLayout slidingPanel;

    private ChatView chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slidingPanel = (SlidingPaneLayout) findViewById(R.id.sliding_panel);
        slidingPanel.setPanelSlideListener(panelListener);
        slidingPanel.setParallaxDistance(200);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    SlidingPaneLayout.PanelSlideListener panelListener = new SlidingPaneLayout.PanelSlideListener() {
        @Override
        public void onPanelClosed(View arg0) {
        }

        @Override
        public void onPanelOpened(View arg0) {
        }

        @Override
        public void onPanelSlide(View arg0, float arg1) {

        }

    };
}
