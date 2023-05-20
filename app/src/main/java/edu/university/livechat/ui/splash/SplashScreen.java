package edu.university.livechat.ui.splash;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import edu.university.livechat.LiveChat;
import edu.university.livechat.ui.login.LoginActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends Activity {
    // var to help with knowing if onDestroy was
    // called intentionally or if activity was destroyed by system
    private static boolean change = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // load splash screen
        super.onCreate(savedInstanceState);

        // display splash screen for 450 ms
        try {
            Thread.sleep(450);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //start main activity when done, inform onDestroy that it was intentional
        change = false;
        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        // close splash activity
        finish();
    }

    @Override
    protected void onDestroy() {
        if (change) {
            LiveChat liveChat = (LiveChat) getApplicationContext();
            liveChat.setAppState(getApplicationContext(),"closed");
        }

        super.onDestroy();
    }
}