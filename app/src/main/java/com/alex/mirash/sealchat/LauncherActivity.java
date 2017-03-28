package com.alex.mirash.sealchat;

import android.os.Bundle;
import android.util.Log;

import com.alex.mirash.sealchat.chat.util.chat.ChatHelper;
import com.alex.mirash.sealchat.chat.util.qb.DataHolder;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.utils.DeviceUtils;
import com.quickblox.sample.core.utils.Toaster;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

public class LauncherActivity extends BaseUserSessionActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        proceedToTheNextActivity();
    }

    protected void proceedToTheNextActivity() {
        if (checkSignIn()) {
            restoreChatSession();
        } else {
            startLoginActivity();
        }
    }


    private void restoreChatSession() {
        if (ChatHelper.getInstance().isLogged()) {
            startMainActivity();
            finish();
        } else {
            QBUser currentUser = getUserFromSession();
            loginToChat(currentUser);
        }
    }

    private void startLoginActivity() {
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
        testLogin();
        finish();
    }

    private void testLogin() {
        String deviceName = DeviceUtils.getDeviceName();
        Log.d("LOL", deviceName);
        final LoginUserDataHolder userData = new LoginUserDataHolder(
                deviceName.contains("HTC") ? "seal1" : "seal2", "qwerty123");
        QBUser qbUser = new QBUser(userData.getLogin(), userData.getPassword());
        QBUsers.signIn(qbUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser qbUser, Bundle bundle) {
                Log.d("LOL", "onSuccess: " + String.valueOf(qbUser));
                qbUser.setPassword(userData.getPassword());
                DataHolder.getInstance().setSignInQbUser(qbUser);
                Toaster.longToast(R.string.user_successfully_sign_in);
                loginToChat(qbUser);
            }

            @Override
            public void onError(QBResponseException errors) {
                Log.d("LOL", "signin error " + String.valueOf(errors));
            }
        });
    }

    public static class LoginUserDataHolder {
        private String login;
        private String password;
        private String confirmPassword;

        public LoginUserDataHolder(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public LoginUserDataHolder(String login, String password, String confirmPassword) {
            this(login, password);
            this.confirmPassword = confirmPassword;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }
    }
}
