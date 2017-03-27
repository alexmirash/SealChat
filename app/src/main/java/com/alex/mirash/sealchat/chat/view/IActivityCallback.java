package com.alex.mirash.sealchat.chat.view;

/**
 * @author Mirash
 */

public interface IActivityCallback {
    void onCreate();

    void onDestroy();

    void onResume();

    void onPause();

    void onStart();

    void onStop();

    void onBackPressed();


}
