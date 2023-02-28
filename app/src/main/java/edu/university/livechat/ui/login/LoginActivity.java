package edu.university.livechat.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.university.livechat.register;

import java.util.Objects;

import edu.university.livechat.RequestTask;
import edu.university.livechat.ui.chat.ChatPage;


public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent chatPage = new Intent(this, ChatPage.class);
        Intent registerPage = new Intent(this, register.class);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //get binding
        edu.university.livechat.databinding.ActivityLoginBinding binding = edu.university.livechat.databinding.ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;


        loginButton.setOnClickListener(v -> {
            if (usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter credentials!", Toast.LENGTH_SHORT).show();
                return;
            }
            //send http post
            //Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
            try {
                String check = new RequestTask().run("asd");
//                if(Objects.equals(check, "Success")){
//                    //TODO:Save to user repository
//
//                    startActivity(chatPage);
//                } else {
//                    Toast.makeText(this, "Unsuccessful login", Toast.LENGTH_SHORT).show();
//                }
                startActivity(registerPage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });


    }

}