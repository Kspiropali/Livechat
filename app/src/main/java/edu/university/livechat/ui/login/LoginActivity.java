package edu.university.livechat.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import edu.university.livechat.RequestTask;
import edu.university.livechat.ui.chat.ChatPage;
import edu.university.livechat.ui.register.RegisterActivity;

public class LoginActivity extends Activity {
//    private final RequestTask requestTask = new RequestTask();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent chatPage = new Intent(this, ChatPage.class);
        super.onCreate(savedInstanceState);
//        Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show();

        // set up the window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //get binding
        edu.university.livechat.databinding.ActivityLoginBinding binding = edu.university.livechat.databinding.ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button registerButton = binding.registerPage;
        final Button loginButton = binding.login;


        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();


            if (username.equals("") || password.equals("")) {
                Toast.makeText(getApplicationContext(), "Enter credentials!", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Extra username and password checking needed


//             send login request to server

            RequestTask requestTask = new RequestTask();
            // spawn an other thread to send login request
            new Thread(() -> {
                try {
                    String response = requestTask.loginPostRequest(username, password);
                    runOnUiThread(() -> {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();


            // credentials are valid, go to chat page
            startActivity(chatPage);
        });


        registerButton.setOnClickListener(v -> {
            Intent registerPage = new Intent(this, RegisterActivity.class);
            startActivity(registerPage);
        });
    }

}