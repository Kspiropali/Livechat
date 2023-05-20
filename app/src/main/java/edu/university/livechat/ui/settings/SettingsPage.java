package edu.university.livechat.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import java.io.IOException;

import edu.university.livechat.LiveChat;
import edu.university.livechat.R;
import edu.university.livechat.data.KeyStoreHelper;
import edu.university.livechat.data.handlers.RequestTask;
import edu.university.livechat.data.model.User;
import edu.university.livechat.ui.chat.ChatPage;

public class SettingsPage extends Activity {
    private final RequestTask requestTask = new RequestTask();

    private boolean change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        change = true;
        // setting up the  settings-page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        TextView usernameDisplay = findViewById(R.id.username);
        usernameDisplay.setText(getIntent().getStringExtra("username"));

        // getting the token for authentication from secret storage
        @SuppressWarnings("resource") String token = new KeyStoreHelper(getApplicationContext()).getLatestToken();

        // setting login page intent to redirect to it
        Intent chatPage = new Intent(this, ChatPage.class);

        // setup the spinner with the languages
        Spinner dropdown = findViewById(R.id.country_spinner);
        String[] items = new String[]{"English(UK)", "English(US)", "Albanian", "Armenian", "Romanian", "Italian", "Turkish", "Chinese"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        // setting up hints for the edit texts
        new Thread(() -> {
            User user;
            try {
                user = requestTask.getUserDetails(token);
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(this, "Error getting user details", Toast.LENGTH_SHORT).show());
                return;
            }

            runOnUiThread(() -> {
                // initializing the edit texts from the layout
                EditText firstNameEditText = findViewById(R.id.first_name_edit_text);
                EditText lastNameEditText = findViewById(R.id.last_name_edit_text);
                EditText phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
                EditText passwordEditText = findViewById(R.id.password_edit_text);
                EditText confirmPasswordEditText = findViewById(R.id.password_repeat_edit_text);
                // opt in checkbox
                CheckBox checkBox = findViewById(R.id.email_notifications_checkbox);
                // the following if-else set the hints
                // null for defaults, otherwise set details from server database
                if (user.getName() != null) {
                    firstNameEditText.setHint(user.getName());
                } else {
                    firstNameEditText.setHint("Enter your First Name");
                }

                if (user.getSurname() != null) {
                    lastNameEditText.setHint(user.getSurname());
                } else {
                    lastNameEditText.setHint("Enter your Last Name");
                }

                if (user.getPhoneNumber() != null) {
                    phoneNumberEditText.setHint(user.getPhoneNumber());
                } else {
                    phoneNumberEditText.setHint("Enter your Phone Number");
                }
                if (user.getLanguage() != null) {
                    dropdown.setSelection(adapter.getPosition(user.getLanguage()));
                } else {
                    dropdown.setSelection(adapter.getPosition("English(UK)"));
                }
                // set the checkbox to the value from the server
                checkBox.setChecked(user.isOptIn());

                passwordEditText.setHint("********");
                confirmPasswordEditText.setHint("********");

            });
        }).start();
        // initializing the edit texts from the layout
        EditText firstNameEditText = findViewById(R.id.first_name_edit_text);
        EditText lastNameEditText = findViewById(R.id.last_name_edit_text);
        EditText phoneNumberEditText = findViewById(R.id.phone_number_edit_text);
        EditText passwordEditText = findViewById(R.id.password_edit_text);
        EditText confirmPasswordEditText = findViewById(R.id.password_repeat_edit_text);
        CheckBox checkBox = findViewById(R.id.email_notifications_checkbox);

        // setting up the buttons
        Button submitButton = findViewById(R.id.submit_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        // cancel button redirects to the chat page
        cancelButton.setOnClickListener(v -> {
            // go back to the previous page
            chatPage.putExtra("username", token.split(":")[0]);
            chatPage.putExtra("destinationUser", "public");
            // start chat page activity
            startActivity(chatPage);
            // notify onDestroy() that the activity is finished intentionally
            change = false;
            finish();
        });

        // submit button updates the user details, sending POST request to the server
        submitButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
            String countrySelected = dropdown.getSelectedItem().toString();
            boolean checkBoxChecked = checkBox.isChecked();

            // check if the fields are empty
            if (firstName.isEmpty() || lastName.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // check if the phone number is 10 digits
            if (phoneNumber.length() != 10) {
                Toast.makeText(this, "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
                return;
            }

            // check if the passwords match
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // create a new user object with the details
            User user = new User(token.split(":")[0], firstName, lastName, phoneNumber, password, countrySelected, checkBoxChecked);

            new Thread(() -> {
                try {
                    // send the request to the server, on another thread
                    String response = requestTask.update(token, user);
                    if (response.equals("User updated")) {
                        runOnUiThread(() -> {
                            // user updated successfully, redirect to the chat page
                            Toast.makeText(this, "User updated successfully, please relog again!", Toast.LENGTH_SHORT).show();
                            chatPage.putExtra("username", token.split(":")[0]);
                            chatPage.putExtra("destinationUser", "public");
                            startActivity(chatPage);
                            // finish the current activity
                            change = false;
                            finish();
                        });
                    } else {
                        // user update failed, display the error message toast
                        runOnUiThread(() -> Toast.makeText(this, "User update failed", Toast.LENGTH_SHORT).show());
                    }
                } catch (IOException e) {
                    // IO error occurred, display the error message toast
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).start();
        });
    }

    @Override
    protected void onDestroy() {
        // if the activity is finished intentionally, do not show the toast
        if (change) {
            LiveChat liveChat = (LiveChat) getApplicationContext();
            liveChat.setAppState(getApplicationContext(),"closed");
        }
        super.onDestroy();
    }
}