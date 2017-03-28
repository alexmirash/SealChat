package com.alex.mirash.sealchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alex.mirash.sealchat.chat.util.chat.ChatHelper;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.ui.dialog.ProgressDialogFragment;
import com.quickblox.sample.core.utils.SharedPrefsHelper;
import com.quickblox.users.model.QBUser;

/**
 * @author Mirash
 */

public class BaseUserSessionActivity extends AppCompatActivity {

    protected boolean checkSignIn() {
        return SharedPrefsHelper.getInstance().hasQbUser();
    }

    protected void startMainActivity() {
        Intent intent = new Intent(this, MainChatActivity.class);
        startActivity(intent);
        finish();
    }

    protected QBUser getUserFromSession() {
        QBUser user = SharedPrefsHelper.getInstance().getQbUser();
        user.setId(QBSessionManager.getInstance().getSessionParameters().getUserId());
        return user;
    }

    protected void loginToChat(final QBUser user) {
        ChatHelper.getInstance().loginToChat(user, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void result, Bundle bundle) {
                SharedPrefsHelper.getInstance().saveQbUser(user);
                startMainActivity();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.d("LOL", "logintoChat errero " + String.valueOf(e));
            }
        });
    }
}
