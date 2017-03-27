package com.alex.mirash.sealchat.chat.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author Mirash
 */

public abstract class ScreenView extends FrameLayout implements IActivityCallback {

    public ScreenView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public ScreenView(@NonNull Context context) {
        this(context, null);
    }

    protected abstract void init();

    protected Activity getActivity() {
        return (Activity) getContext();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onBackPressed() {
    }


    public void show() {
        if (!isScreenShown()) {
            setVisibility(VISIBLE);
        }
    }

    public void hide() {
        if (isScreenShown()) {
            setVisibility(GONE);
        }
    }

    public boolean isScreenShown() {
        return getVisibility() == VISIBLE;
    }
}
