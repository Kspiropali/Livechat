package edu.university.livechat.ui.register;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import edu.university.livechat.data.handlers.RequestTask;

public class RegisterActivity extends Activity {
    // regex email string
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String USERNAME_REGEX = "^[A-Za-z]+$";

    private static final RequestTask requestTask = new RequestTask();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show();

        // set up the window
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        //get binding
        edu.university.livechat.databinding.ActivityRegisterBinding binding = edu.university.livechat.databinding.ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get buttons from ui
        final Button registerButton = binding.register;
        final Button loginButton = binding.loginPage;

        // get edit text from ui
        final EditText emailEditText = binding.email;
        final EditText usernameEditText = binding.username;
        final EditText firstNameEditText = binding.firstname;
        final EditText lastNameEditText = binding.lastname;
        final EditText passwordEditText = binding.password;

        loginButton.setOnClickListener(v -> {
            finish();
        });

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            } else if (!email.matches(EMAIL_REGEX)) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                return;
            } else if (!username.matches(USERNAME_REGEX)) {
                Toast.makeText(this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
                return;
            }


            // send request
            new Thread(() -> {
                try {
                    String response = requestTask.registerPostRequest(email, username, firstName, lastName, password);
                    if (response.equals("Success")) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Register success! A verification email will be sent shortly!", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Email/Username already exist!", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
