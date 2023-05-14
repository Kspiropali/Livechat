package edu.university.livechat.ui.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import edu.university.livechat.R;
import edu.university.livechat.data.KeyStoreHelper;
import edu.university.livechat.data.handlers.RequestTask;
import edu.university.livechat.data.model.ChatTemplate;
import edu.university.livechat.data.model.Message;
import edu.university.livechat.data.model.MessageTemplate;
import edu.university.livechat.databinding.ActivityChatPageBinding;
import edu.university.livechat.ui.about.AboutApp;
import edu.university.livechat.ui.login.LoginActivity;

public class ChatPage extends Activity {
    private KeyStoreHelper keyStoreHelper;
    private LinearLayout messagesBox;
    private final RequestTask requestTask = new RequestTask();
    private String username;
    private String destinationUser;
    private TableLayout onlineUsersTable;
    private Intent chatPage;
    private EditText messageBox;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set up activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        // data-bindings
        edu.university.livechat.databinding.ActivityChatPageBinding binding = ActivityChatPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // get online users table in the nav drawer
        onlineUsersTable = binding.navView.getHeaderView(0).findViewById(R.id.onlineUsersTable);
        // get navigation drawer
        NavigationView drawer = binding.navView;
        ScrollView scrollView = findViewById(R.id.scrollView1);
        messagesBox = scrollView.findViewById(R.id.chatMessages);
        // login and about page intents
        Intent loginPage = new Intent(this, LoginActivity.class);
        Intent aboutPage = new Intent(this, AboutApp.class);
        // TODO: account settings intent
        // key store helper to get the token
        keyStoreHelper = new KeyStoreHelper(getApplicationContext());
        // user and destination user
        username = getIntent().getStringExtra("username");
        destinationUser = getIntent().getStringExtra("destinationUser");
        //chat-page intent to switch rooms
        chatPage = new Intent(this, ChatPage.class);
        // chat box message and the string
        messageBox = findViewById(R.id.chatBoxMessage);


        // create a thread to run every second, without using handler
        Thread mainThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(300);
                    ArrayList<Message> response = requestTask.downloadMessages(username, calculateRoomId(), keyStoreHelper.getLatestToken());
                    if(response == null) {
                        break;
                    }
                    int currentMessageCount = messagesBox.getChildCount();
                    int serverMessageCount = response.size();
                    runOnUiThread(() -> {
                        for (int i = currentMessageCount; i < serverMessageCount; i++) {
                            if (response.get(i).getSender().equals(username)) {
                                int finalI = i;
                                runOnUiThread(() -> messagesBox.addView(new MessageTemplate(getApplicationContext(), response.get(finalI).getContent(), true, response.get(finalI).getTimestamp(),
                                        response.get(finalI).getSender())));
                                continue;
                            }
                            int finalI1 = i;
                            runOnUiThread(() -> messagesBox.addView(new MessageTemplate(getApplicationContext(), response.get(finalI1).getContent(), false, response.get(finalI1).getTimestamp(),
                                    response.get(finalI1).getSender())));
                        }

                    });
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mainThread.start();

        // set username in drawer
        TextView usernameTextView = binding.navView.getHeaderView(0).findViewById(R.id.loggedInUser);
        usernameTextView.setText("Currently logged in as: " + username);

        // hide drawer
        drawer.setVisibility(View.GONE);
        // getting all registered users in the system
        getOnlineUsers();
        // setting up the current chatroom's history
        downloadSetMessageHistory();
//        messageBox.requestFocus();

        //////////////////////EVENT LISTENERS////////////////////////
        // get settings button and set its listener
        drawer.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                drawer.setVisibility(View.GONE);
            }
        });

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
            // stop download messages thread
            mainThread.interrupt();

            // Delete token from database
            keyStoreHelper.deleteAllTokens();
            // clear the chat history
            messagesBox.removeAllViews();
            // clear username and destination user
            username = "";
            destinationUser = "";

            // return to login page
            startActivity(loginPage);
            finish();
        });

        // get the public chatroom button and set its listener
        LinearLayout publicChatroomButton = binding.navView.getHeaderView(0).findViewById(R.id.publicRoom);
        publicChatroomButton.setOnClickListener(v -> {
            if (destinationUser.equals("public")) {
                // close drawer
                drawer.setVisibility(View.GONE);
                return;
            }
            // close drawer
            drawer.setVisibility(View.GONE);
            // go to public chatroom page
            chatPage.putExtra("username", username);
            chatPage.putExtra("destinationUser", "public");
            startActivity(chatPage);
            finish();

        });

        // get the send message button and set its listener
        Button sendMessageButton = binding.include.sendButton;
        sendMessageButton.setOnClickListener(v -> {
            // get message from text box
            String message = messageBox.getText().toString();
            if (message.equals("")) {
                return;
            }
            // clear text box
            messageBox.setText("");
            messageBox.clearFocus();
            // send message
            new Thread(() -> {
                try {
                    @SuppressLint("SimpleDateFormat") String dateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    Message messageToSend = new Message(username, message);
                    messageToSend.setType("CHAT");
                    messageToSend.setDestination(calculateRoomId());
                    requestTask.sendMessage(keyStoreHelper.getLatestToken(), messageToSend);

                    // add message to the chat history
                    runOnUiThread(() -> {
                        // add message to chat history
                        addMessageToChatHistory(message, dateNow);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        });
        ////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void getOnlineUsers() {
        // get all registered users, add them in the drawer
        new Thread(() -> {
            try {
                String response = requestTask.getRegisteredUsers(keyStoreHelper.getLatestToken());
                // String is of type: ["bob","sam","john"], remove string literals [] from the string
                String[] users = response.substring(1, response.length() - 1).split(",");
                // remove all left string literals "" from the array
                Arrays.setAll(users, i -> users[i].replace("\"", ""));

                for (String user : users) {
                    if (user.equals(username)) {
                        continue;
                    }
                    LinearLayout userView = new ChatTemplate(getApplicationContext()).getOnlineUserFrameView(user);
                    userView.setOnClickListener(v -> {
                        // close drawer
                        findViewById(R.id.nav_view).setVisibility(View.GONE);
                        // go to chat page
                        chatPage.putExtra("username", username);
                        chatPage.putExtra("destinationUser", user);
                        startActivity(chatPage);
                        finish();
                    });
                    onlineUsersTable.addView(userView);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    private void downloadSetMessageHistory() {
        messagesBox.removeAllViews();
        // get all messages between the two users
        new Thread(() -> {
            try {
                if (keyStoreHelper.getLatestToken() == null) {
                    return;
                }
                ArrayList<Message> response = requestTask.downloadMessages(username, calculateRoomId(), keyStoreHelper.getLatestToken());
                // check if empty
                if (response.isEmpty()) {
                    return;
                }
                for (Message message : response) {
                    if (message.getSender().equals(username)) {
                        runOnUiThread(() -> messagesBox.addView(new MessageTemplate(getApplicationContext(), message.getContent(), true, message.getTimestamp(),
                                message.getSender())));
                        continue;
                    }
                    runOnUiThread(() -> messagesBox.addView(new MessageTemplate(getApplicationContext(), message.getContent(), false, message.getTimestamp(),
                            message.getSender())));
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    private void addMessageToChatHistory(String message, String dateNow) {
        // add message to chat history
        runOnUiThread(() -> messagesBox.addView(new MessageTemplate(getApplicationContext(), message, true, dateNow, username)));
    }

    private String calculateRoomId() {
        // calculate room id
        if (destinationUser.equals("public")) {
            return "public";
        }
        String[] users = {username, destinationUser};
        Arrays.sort(users);
        return users[0] + users[1];
    }
}