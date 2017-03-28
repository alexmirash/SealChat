package com.alex.mirash.sealchat;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.alex.mirash.sealchat.chat.util.chat.ChatHelper;
import com.alex.mirash.sealchat.chat.util.qb.QbDialogHolder;
import com.alex.mirash.sealchat.chat.view.ChatView;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;

import java.util.ArrayList;

public class MainChatActivity extends AppCompatActivity {
    private SlidingPaneLayout slidingPanel;

    private ChatView chatView;
    private QBChatDialog chatDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        chatView = (ChatView) findViewById(R.id.chat_view);

        slidingPanel = (SlidingPaneLayout) findViewById(R.id.sliding_panel);
        slidingPanel.setPanelSlideListener(panelListener);
        slidingPanel.setParallaxDistance(200);

        loadDialogs();
    }

    private void loadDialogs() {
        final long time = System.currentTimeMillis();
        QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
        requestBuilder.setLimit(1);
        ChatHelper.getInstance().getDialogs(requestBuilder, new QBEntityCallback<ArrayList<QBChatDialog>>() {
            @Override
            public void onSuccess(ArrayList<QBChatDialog> dialogs, Bundle bundle) {
                Log.d("LOL", "loadDialogs sux " + (System.currentTimeMillis() - time));
                QbDialogHolder.getInstance().addDialogs(dialogs);
                if (dialogs != null && !dialogs.isEmpty()) {
                    chatDialog = dialogs.get(0);
                    if (chatDialog != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatView.showDialog(chatDialog);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("LOL", "loadDialogs fail " + String.valueOf(e));
            }
        });
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
