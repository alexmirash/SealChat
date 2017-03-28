package com.alex.mirash.sealchat.chat;

import com.alex.mirash.sealchat.MainChatActivity;
import com.alex.mirash.sealchat.R;
import com.quickblox.sample.core.gcm.CoreGcmPushListenerService;
import com.quickblox.sample.core.utils.NotificationUtils;
import com.quickblox.sample.core.utils.ResourceUtils;

public class GcmPushListenerService extends CoreGcmPushListenerService {
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void showNotification(String message) {
        //TODO
        NotificationUtils.showNotification(this, MainChatActivity.class,
                ResourceUtils.getString(R.string.app_name), message,
                R.mipmap.ic_launcher, NOTIFICATION_ID);
    }
}