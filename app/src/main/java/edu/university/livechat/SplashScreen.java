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
        //setContentView(R.layout);
        try {
            Thread.sleep(750);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
        finish();
    }
}