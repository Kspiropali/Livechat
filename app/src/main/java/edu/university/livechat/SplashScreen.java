package edu.university.livechat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import edu.university.livechat.ui.login.LoginActivity;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // display splash screen for 450 ms
        try {
            Thread.sleep(450);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //start main activity when done
        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        // close splash activity
        finish();
    }
}