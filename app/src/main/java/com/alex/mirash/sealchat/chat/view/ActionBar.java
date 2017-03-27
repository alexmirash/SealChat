package com.alex.mirash.sealchat.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alex.mirash.sealchat.R;

/**
 * @author Mirash
 */

public class ActionBar extends RelativeLayout {
    private ImageView homeButton;
    private TextView titleView;

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_chat_action_bar, this);
    }
}
