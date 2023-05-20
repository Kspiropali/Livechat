package edu.university.livechat.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;

import edu.university.livechat.LiveChat;
import edu.university.livechat.R;
import edu.university.livechat.data.KeyStoreHelper;
import edu.university.livechat.data.handlers.RequestTask;
import edu.university.livechat.ui.chat.ChatPage;
import edu.university.livechat.ui.register.RegisterActivity;

public class LoginActivity extends Activity {
    private final RequestTask requestTask = new RequestTask();
    private Toast toast;
    private KeyStoreHelper keyStoreHelper;
    private boolean change;
    private ConstraintLayout loginLayout;
    private Button notificationAllowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        change = true;
        // set up activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // set up various variables
        keyStoreHelper = new KeyStoreHelper(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        Intent chatPage = new Intent(this, ChatPage.class);

        // get UI elements
        final EditText usernameEditText = findViewById(R.id.username_login);
        final EditText passwordEditText = findViewById(R.id.password_login);
        final Button registerButton = findViewById(R.id.register_page);
        final Button loginButton = findViewById(R.id.login);
        notificationAllowButton = findViewById(R.id.enable_notifications);
        loginLayout = findViewById(R.id.login_container);

        // check if user is already logged in
        String token = keyStoreHelper.getLatestToken();
        // if token is not null, we may have a valid token
        if (token != null) {
            new Thread(() -> {
                try {
                    // send login request to server with token, on another thread
                    String response = requestTask.loginByToken(token);
                    if (response.equals("Logged in!")) {
                        // token is ok, start activity
                        toast.setText("Welcome Back, " + token.split(":")[0] + "!");
                        toast.show();
                        chatPage.putExtra("username", token.split(":")[0]);
                        chatPage.putExtra("destinationUser", "public");
                        startActivity(chatPage);
                        // finish the login activity, inform onDestroy that it was intentional
                        change = false;
                        finish();
                    } else {
                        // if we get here, the token is invalid so we delete it
                        toast.setText(response);
                        toast.show();
                        keyStoreHelper.deleteAllTokens();
                    }
                } catch (IOException e) {
                    // server error, try manual login
                    toast.setText("Error: " + e.getMessage());
                    toast.show();
                }
            }).start();
        }


        ////////////////////// EVENT LISTENERS//////////////////////////////////////////////
        // login button
        loginButton.setOnClickListener(v -> {
            // get username and password from UI
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // check if username and password are empty
            if (username.equals("") || password.equals("")) {
                toast.setText("Please fill in all fields");
                toast.show();
                return;
            }

            new Thread(() -> {
                try {
                    // send login request to server on another thread
                    String response = requestTask.loginPostRequest(username, password);
                    if (response.equals("Logged in!")) {
                        // credentials are good, start activity
                        toast.setText("Logged in!");
                        toast.show();

                        // saving token to keystore
                        keyStoreHelper.insertToken(username + ":" + password);
                        // start activity
                        toast.setText("Welcome, " + username + "!");
                        toast.show();
                        // pass username to next activity
                        chatPage.putExtra("username", username);
                        // pass the default destination user to next activity
                        chatPage.putExtra("destinationUser", "public");
                        // start activity
                        startActivity(chatPage);
                        change = false;
                        finish();
                    } else {
                        // credentials are bad, show error message
                        runOnUiThread(() -> {
                            usernameEditText.setText("");
                            passwordEditText.setText("");
                            toast.setText(response);
                            toast.show();
                        });
                    }
                } catch (Exception e) {
                    // server error, show error message
                    toast.setText("Error: " + e.getMessage());
                    toast.show();
                }
            }).start();
        });

        // notification button
        notificationAllowButton.setOnClickListener(v -> {
            // ask for notification permission
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                toast.setText("Please enable notifications for this app in the settings");
                toast.show();
                // ask for permission
                Intent notificationSettings = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(notificationSettings);
            }
        });

        // redirect to register page
        assert registerButton != null;
        registerButton.setOnClickListener(v -> {
            Intent registerPage = new Intent(this, RegisterActivity.class);
            startActivity(registerPage);
        });
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    protected void onDestroy() {
        if (change) {
            LiveChat liveChat = (LiveChat) getApplicationContext();
            liveChat.setAppState(getApplicationContext(), "closed");
        }

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // check if permission was granted
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            loginLayout.removeView(notificationAllowButton);

        }
    }
}