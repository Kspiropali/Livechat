package edu.university.livechat.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.IOException;

import edu.university.livechat.data.KeyStoreHelper;
import edu.university.livechat.data.handlers.RequestTask;
import edu.university.livechat.ui.chat.ChatPage;
import edu.university.livechat.ui.register.RegisterActivity;

public class LoginActivity extends Activity {
    private final RequestTask requestTask = new RequestTask();
    private Toast toast;
    private KeyStoreHelper keyStoreHelper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get binding
        edu.university.livechat.databinding.ActivityLoginBinding binding = edu.university.livechat.databinding.ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // set up various variables
        keyStoreHelper = new KeyStoreHelper(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
        Intent chatPage = new Intent(this, ChatPage.class);

        // get UI elements
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button registerButton = binding.registerPage;
        final Button loginButton = binding.login;


        // check if user is already logged in
        String token = keyStoreHelper.getLatestToken();
        if (token != null) {
            new Thread(() -> {
                try {
                    String response = requestTask.loginByToken(token);
                    if (response.equals("Logged in!")) {
                        finish();
                        toast.setText("Welcome Back, " + token.split(":")[0] + "!");
                        toast.show();
                        chatPage.putExtra("username", token.split(":")[0]);
                        chatPage.putExtra("destinationUser", "public");
                        startActivity(chatPage);
                    } else {
                        toast.setText(response);
                        toast.show();
                    }
                } catch (IOException e) {
                    toast.setText("Error: " + e.getMessage());
                    toast.show();
                }
            }).start();
        }


        ////////////////////// EVENT LISTENERS//////////////////////////////////////////////
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();


            if (username.equals("") || password.equals("")) {
                toast.setText("Please fill in all fields");
                toast.show();
                return;
            }

            // TODO: Extra username and password checking needed


            // send login request to server on another thread
            new Thread(() -> {
                try {
                    String response = requestTask.loginPostRequest(username, password);
                    if (response.equals("Logged in!")) {
                        toast.setText("Logged in!");
                        toast.show();

                        // saving token to keystore
                        keyStoreHelper.insertToken(username + ":" + password);
                        finish();
                        // start activity
                        toast.setText("Welcome, " + username + "!");
                        toast.show();
                        chatPage.putExtra("username", username);
                        chatPage.putExtra("destinationUser", "public");
                        startActivity(chatPage);

                    } else {
                        runOnUiThread(() -> {
                            usernameEditText.setText("");
                            passwordEditText.setText("");
                            toast.setText(response);
                            toast.show();
                        });
                    }

                } catch (Exception e) {
                    toast.setText("Error: " + e.getMessage());
                    toast.show();
                }
            }).start();
        });


        assert registerButton != null;
        registerButton.setOnClickListener(v -> {
            Intent registerPage = new Intent(this, RegisterActivity.class);
            startActivity(registerPage);
        });

        ////////////////////////////////////////////////////////////////////////////////////////////
    }

}