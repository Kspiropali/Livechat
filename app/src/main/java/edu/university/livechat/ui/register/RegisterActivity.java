package edu.university.livechat.ui.register;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import edu.university.livechat.LiveChat;
import edu.university.livechat.R;
import edu.university.livechat.data.handlers.RequestTask;

public class RegisterActivity extends Activity {
    // regex email string
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    // regex username string, characters only
    private static final String USERNAME_REGEX = "^[A-Za-z]+$";
    private static final RequestTask requestTask = new RequestTask();
    private boolean change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        change = true;
        // setting up the activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // get buttons from ui
        final Button registerButton = findViewById(R.id.register_submit_user);
        final Button loginRedirect = findViewById(R.id.login_redirect_button);

        // get edit text from ui
        final EditText emailEditText = findViewById(R.id.register_user_email);
        final EditText usernameEditText = findViewById(R.id.register_user_username);
        final EditText firstNameEditText = findViewById(R.id.register_user_firstname);
        final EditText lastNameEditText = findViewById(R.id.register_user_lastname);
        final EditText passwordEditText = findViewById(R.id.register_user_password);

        // redirect back to login page
        loginRedirect.setOnClickListener(v -> {
            change = false;
            finish();
        });

        // register button listener
        registerButton.setOnClickListener(v -> {
            // get user input from edit text, parse as string
            String email = emailEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // check if any fields are empty
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

            new Thread(() -> {
                try {
                    // send register request to the backend server, on a new thread
                    String response = requestTask.registerPostRequest(email, username, firstName, lastName, password);
                    if (response.equals("Success")) {
                        runOnUiThread(() -> {
                            // register success, redirect back to login page
                            Toast.makeText(this, "Register success! A verification email will be sent shortly!", Toast.LENGTH_SHORT).show();
                            change = false;
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            // username/email restrictions encountered
                            Toast.makeText(this, "Email/Username already exist!", Toast.LENGTH_SHORT).show();
                        });
                    }
                } catch (IOException e) {
                    // request failed, IO exception
                    e.printStackTrace();
                }
            }).start();
        });
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
