package edu.university.livechat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import edu.university.livechat.ui.login.LoginActivity;

public class SplashScreen extends AppCompatActivity {
     @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout);
         new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();
         },2000);
    }
}
