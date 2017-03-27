package com.alex.mirash.sealchat.menu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.alex.mirash.sealchat.R;

/**
 * @author Mirash
 */

public class MenuView extends FrameLayout {
    public MenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_menu, this);
    }
}
