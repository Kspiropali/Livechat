package edu.university.livechat.ui.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.Arrays;

import edu.university.livechat.R;
import edu.university.livechat.data.KeyStoreHelper;
import edu.university.livechat.data.handlers.RequestTask;
import edu.university.livechat.data.model.ChatTemplate;
import edu.university.livechat.data.model.MessageTemplate;
import edu.university.livechat.databinding.ActivityChatPageBinding;
import edu.university.livechat.ui.about.AboutApp;
import edu.university.livechat.ui.login.LoginActivity;


public class ChatPage extends Activity {
    private KeyStoreHelper keyStoreHelper;
    private ActivityChatPageBinding binding;
    private LinearLayout messagesBox;
    private RequestTask requestTask = new RequestTask();
    private String username;
    private String destinationUser;
    private TableLayout onlineUsersTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set up activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        // data-bindings
        binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set up various variables
        onlineUsersTable = binding.navView.getHeaderView(0).findViewById(R.id.onlineUsersTable);
        ScrollView scrollView = findViewById(R.id.scrollView1);
        messagesBox = scrollView.findViewById(R.id.chatMessages);
        Intent loginPage = new Intent(this, LoginActivity.class);
        Intent aboutPage = new Intent(this, AboutApp.class);
        keyStoreHelper = new KeyStoreHelper(getApplicationContext());
        username = getIntent().getStringExtra("username");

        // set username in drawer
        TextView usernameTextView = binding.navView.getHeaderView(0).findViewById(R.id.loggedInUser);
        usernameTextView.setText("Currently logged in as: "+username);
        getOnlineUsers();

        // get drawer
        NavigationView drawer = binding.navView;
        // hide drawer
        drawer.setVisibility(View.GONE);
        drawer.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                drawer.setVisibility(View.GONE);
            }
        });

        /////////////EVENT LISTENERS/////////////////////////////////////
        // get settings button and set its listener
        ImageView settingsButton = binding.topBarMenu.settingsImageButton;
        settingsButton.setOnClickListener(v -> {
            if (drawer.getVisibility() == View.VISIBLE) {
                // close drawer if opened
                drawer.setVisibility(View.GONE);
                return;
            }
            // open drawer
            drawer.setVisibility(View.VISIBLE);
        });

        // get the account settings button and set its listener
        TextView accountSettingsButton = binding.navView.getHeaderView(0).findViewById(R.id.accountSettingsPage);
        accountSettingsButton.setOnClickListener(v -> {
            // close drawer
            drawer.setVisibility(View.GONE);
            // go to account settings page
            // TODO: account settings page
        });


        // get the about this project button and set its listener
        TextView aboutThisProjectButton = binding.navView.getHeaderView(0).findViewById(R.id.aboutThisProjectPage);
        aboutThisProjectButton.setOnClickListener(v -> {
            // close drawer
            drawer.setVisibility(View.GONE);
            // go to about this project page
            startActivity(aboutPage);
        });

        // get the logout button and set its listener
        TextView logoutButton = binding.navView.getHeaderView(0).findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            // close drawer
            drawer.setVisibility(View.GONE);
            // logout

            // Delete token from database
            keyStoreHelper.deleteAllTokens();

            // return to login page
            startActivity(loginPage);
            finish();
        });
        ////////////////////////////////////////////////////////////////////////////////////////

//        messagesBox.removeAllViews();
//        messagesBox.removeViewAt(0);
        System.out.println(username);
        messagesBox.addView(new MessageTemplate(getApplicationContext(), "Welcome to LiveChaaaat!", true));
        messagesBox.addView(new MessageTemplate(getApplicationContext(), "Welcome to LiveChaaaat!", true));
        messagesBox.addView(new MessageTemplate(getApplicationContext(), "Welcome to LiveChaaaat!", true));
        messagesBox.addView(new MessageTemplate(getApplicationContext(), "Welcome to LiveaaaaaaaaaChat!", true));
        messagesBox.addView(new MessageTemplate(getApplicationContext(), "Welcaaaaaaaaaaaome to LiveChat!", false));
        messagesBox.addView(new MessageTemplate(getApplicationContext(), "Welcome to LiveChaaaaaaaaaaaaaaaaaat!", false));
        messagesBox.addView(new MessageTemplate(getApplicationContext(), "Welcome to LiveaaaaaaaaaChat!", true));
        messagesBox.addView(new MessageTemplate(getApplicationContext(), "Welcaaaaaaaaaaaome to LiveChat!", false));
        messagesBox.addView(new MessageTemplate(getApplicationContext(), "Welcome to LiveChaaaaaaaaaaaaaaaaaat!", false));
    }

    private void getOnlineUsers(){
        // get all registered users, add them in the drawer
        new Thread(() -> {
            try {
                String response = requestTask.getRegisteredUsers(keyStoreHelper.getLatestToken());
                // String is of type: ["bob","sam","john"], remove string literals [] from the string
                String[] users = response.substring(1, response.length() - 1).split(",");
                // remove all left string literals "" from the array
                Arrays.setAll(users, i -> users[i].replace("\"", ""));

                for (String user: users){
                    if(user.equals(username)){
                        continue;
                    }
                    onlineUsersTable.addView(new ChatTemplate(getApplicationContext()).getOnlineUserFrameView(user));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

}
