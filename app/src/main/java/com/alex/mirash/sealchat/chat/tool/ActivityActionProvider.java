package com.alex.mirash.sealchat.chat.tool;

import android.app.Activity;

/**
 * @author Mirash
 */

public interface ActivityActionProvider<T extends Activity> {
    T getActivity();
}
