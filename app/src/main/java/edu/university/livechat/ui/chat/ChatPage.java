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

import edu.university.livechat.LiveChat;
import edu.university.livechat.R;
import edu.university.livechat.data.KeyStoreHelper;
import edu.university.livechat.data.handlers.RequestTask;
import edu.university.livechat.data.model.ChatTemplate;
import edu.university.livechat.data.model.Message;
import edu.university.livechat.data.model.MessageTemplate;
import edu.university.livechat.databinding.ActivityChatPageBinding;
import edu.university.livechat.ui.about.AboutApp;
import edu.university.livechat.ui.login.LoginActivity;
import edu.university.livechat.ui.settings.SettingsPage;

public class ChatPage extends Activity {
    // top level class variables
    private String token;
    private LinearLayout messagesBox;
    private final RequestTask requestTask = new RequestTask();
    private String username;
    private String destinationUser;
    private TableLayout onlineUsersTable;
    private Intent chatPage;
    private EditText messageBox;
    // global thread to help with requests
    private Thread mainThread;

    // variable to help with let onDestroy know
    // if the activity was finished intentionally
    private boolean change;

    private LiveChat liveChat;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // set up activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        // needed for onDestroy
        change = true;
        liveChat = (LiveChat) getApplicationContext();

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
        Intent settingsPage = new Intent(this, SettingsPage.class);

        // key store helper to get the token

        try(KeyStoreHelper keyStoreHelper = new KeyStoreHelper(getApplicationContext())){
            token = keyStoreHelper.getLatestToken();
        }
        //token = keyStoreHelper.getLatestToken();
        // user and destination user
        username = getIntent().getStringExtra("username");
        destinationUser = getIntent().getStringExtra("destinationUser");

        // setting up the top chatroom destination name
        // get the currentlyConnectedToTextView in top_bar_menu, set the text to the destination user
        TextView currentlyConnectedToTextView = findViewById(R.id.currentlyConnectedToTextView);
        currentlyConnectedToTextView.setText(destinationUser);

        //chat-page intent to switch rooms
        chatPage = new Intent(this, ChatPage.class);

        // chat box message and the string
        messageBox = findViewById(R.id.chatBoxMessage);

        // reuse the global thread to run every second, without deprecated handlers
        mainThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                    // get the messages from the server
                    ArrayList<Message> response = requestTask.downloadMessages(calculateRoomId(), token);

                    // if the response is null or the server is down, go back to login page
                    if (response == null || response.size() == 0) {
                        continue;
                    }

                    // if the response is unauthorized, or offline go back to login page
                    if (response.get(0).getContent().equals("Server is down") || response.get(0).getContent().equals("unauthorized")) {
                        runOnUiThread(() -> {
                            //Toast.makeText(getApplicationContext(), "Server connection lost!", Toast.LENGTH_LONG).show();
                            startActivity(loginPage);
                            change = false;
                            finish();
                            mainThread.interrupt();
                        });
                    }

                    //count the messages in the chatroom
                    int currentMessageCount = messagesBox.getChildCount();
                    // count the messages in the server
                    int serverMessageCount = response.size();
                    // if the messages are the same, no need to add them again
                    if (currentMessageCount == serverMessageCount) {
                        continue;
                    }

                    runOnUiThread(() -> {
                        // add the new message/s to the chatroom
                        try {
                            for (int i = currentMessageCount; i < serverMessageCount; i++) {
                                int finalI = i;
                                // if the message is from the user, add it to the right side of the screen
                                if (response.get(finalI).getSender().equals(username)) {
                                    runOnUiThread(() -> messagesBox.addView(new MessageTemplate(getApplicationContext(), response.get(finalI).getContent(), true, response.get(finalI).getTimestamp(),
                                            response.get(finalI).getSender(), response.get(finalI).getType())));

                                } else {
                                    // if the message is from the destination user, add it to the left side of the screen
                                    runOnUiThread(() -> messagesBox.addView(new MessageTemplate(getApplicationContext(), response.get(finalI).getContent(), false, response.get(finalI).getTimestamp(),
                                            response.get(finalI).getSender(), response.get(finalI).getType())));
                                }

                            }
                        } catch (Exception e) {
                            // if the server is down, go back to login page
//                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            startActivity(loginPage);
                            change = false;
                            finish();
                        }

                    });
                } catch (InterruptedException e) {
                    // If the thread is interrupted, break out of the loop
                    return;
                } catch (IOException e) {
                    // generic IO exception
                    e.printStackTrace();
                }
            }
        });
        // start the thread
        mainThread.start();

        // set username in drawer
        TextView usernameTextView = binding.navView.getHeaderView(0).findViewById(R.id.logged_user_text);
        usernameTextView.setText("Currently logged in as: " + username);

        // hide drawer
        drawer.setVisibility(View.GONE);

        // getting all registered users in the database
        getOnlineUsers();

        // setting up the current chatroom's history
        downloadSetMessageHistory();

        //////////////////////EVENT LISTENERS////////////////////////
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
            settingsPage.putExtra("username", username);
            // go to account settings page
            startActivity(settingsPage);
            change = false;
            finish();
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

            // Delete token from database
            try (KeyStoreHelper keyStoreHelper = new KeyStoreHelper(getApplicationContext())){
                keyStoreHelper.deleteAllTokens();
            }
            token = null;

            // clear the chat history
            messagesBox.removeAllViews();

            // clear username and destination user
            username = "";
            destinationUser = "";

            // return to login page
            startActivity(loginPage);
            change = false;
            finish();
        });

        // get the public chatroom button and set its listener
        LinearLayout publicChatroomButton = binding.navView.getHeaderView(0).findViewById(R.id.publicRoom);
        publicChatroomButton.setOnClickListener(v -> {
            // close drawer
            drawer.setVisibility(View.GONE);
            // if we are already in the public chatroom, no need to reload it
            if (destinationUser.equals("public")) {
                change = true;
                return;
            }
            // if we are not in public chatroom, go to public chatroom page
            chatPage.putExtra("username", username);
            chatPage.putExtra("destinationUser", "public");
            // send SIGINT to the main thread
            mainThread.interrupt();
            //start the new activity with the new destination user
            startActivity(chatPage);
            change = false;
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
                    // calculate the current date
                    @SuppressLint("SimpleDateFormat") String dateNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    Message messageToSend = new Message(username, message);
                    // type is always CHAT, TODO: IMAGE & RECORDING
                    messageToSend.setType("CHAT");

                    //calculate the room id of the destination user and set it as the destination of the message
                    messageToSend.setDestination(calculateRoomId());

                    // send message to the server
                    requestTask.sendMessage(token, messageToSend);

                    // add message to the chat history
                    runOnUiThread(() -> {
                        // add message to chat history
                        addMessageToChatHistory(message, dateNow);
                    });
                } catch (IOException e) {
                    // generic IO exception
                    e.printStackTrace();
                }
            }).start();
        });
        ////////////////////////////////////////////////////////////////////////////////////////
    }

    @Override
    protected void onDestroy() {
        if (change) {
            liveChat.setAppState(getApplicationContext(),"closed");
        }

        // send SIGINT to the main thread, if activity is destroyed
        mainThread.interrupt();

        super.onDestroy();

    }

    private void getOnlineUsers() {
        // get all registered users, add them in the drawer
        new Thread(() -> {
            try {
                // send POST request to the server to get all registered users
                String response = requestTask.getRegisteredUsers(token);

                // String is of type: ["bob","sam","john"], so we need to remove the [ and ] and split by ,
                String[] users = response.substring(1, response.length() - 1).split(",");

                // remove all left string literals "" from the array
                Arrays.setAll(users, i -> users[i].replace("\"", ""));

                // get the table that holds all online users
                for (String user : users) {
                    if (user.equals(username)) {
                        continue;
                    }
                    // create the linear layout that holds the imageview, username and status
                    LinearLayout userView = new ChatTemplate(getApplicationContext()).getOnlineUserFrameView(user);
                    // set the listener of the linear layout to chat rooms
                    userView.setOnClickListener(v -> {
                        // first close drawer
                        findViewById(R.id.nav_view).setVisibility(View.GONE);

                        // we are already chatting with this user, no need to reload the activity
                        if (destinationUser.equals(user)) {
                            change = true;
                            return;
                        }

                        // pass the new username and destination user variables to the intent
                        chatPage.putExtra("username", username);
                        chatPage.putExtra("destinationUser", user);

                        // start the new activity with the new destination user
                        startActivity(chatPage);

                        // send SIGINT to the current activity's main thread
                        mainThread.interrupt();
                        change = false;
                        finish();
                    });
                    // add the linear layout to the table
                    onlineUsersTable.addView(userView);
                }
            } catch (IOException e) {
                // generic IO exception
                e.printStackTrace();
            }
        }).start();
    }

    private void downloadSetMessageHistory() {
        // clear the chat history if any remnants are left
        messagesBox.removeAllViews();

        // get all messages between the two users
        new Thread(() -> {
            try {
                // if key is null, user is not logged in
                if (token == null) {
                    return;
                }

                // send POST request to the server to get all messages between the users
                ArrayList<Message> response = requestTask.downloadMessages(calculateRoomId(), token);

                // if its empty, return
                if (response.isEmpty() || response.get(0).getType() == null) {
                    return;
                }

                // parse the messages from the response
                for (Message message : response) {
                    if (message.getSender().equals(username)) {
                        // if the message is from the current user, set the message to the left
                        runOnUiThread(() -> messagesBox.addView(new MessageTemplate(getApplicationContext(), message.getContent(), true, message.getTimestamp(),
                                message.getSender(), message.getType())));
                    } else {
                        // if the message is from the other user, set the message to the right
                        runOnUiThread(() -> messagesBox.addView(new MessageTemplate(getApplicationContext(), message.getContent(), false, message.getTimestamp(),
                                message.getSender(), message.getType())));
                    }
                }

            } catch (IOException e) {
                // generic IO exception
                e.printStackTrace();
            }
        }).start();
    }

    private void addMessageToChatHistory(String message, String dateNow) {
        // add message to chat history
        runOnUiThread(() -> messagesBox.addView(new MessageTemplate(getApplicationContext(), message, true, dateNow, username, "CHAT")));
    }

    private String calculateRoomId() {
        // with sockets, I had to calculate the room id, so that the server & users knows where
        // to send the message, so I did it with alphabetical order, so that the room id is always the same
        // if username>destinationUser, then room id = destinationUser+username
        // if username<destinationUser, then room id = username+destinationUser
        if (destinationUser.equals("public")) {
            return destinationUser;
        }

        // sort the two users alphabetically in an array
        String[] users = {username, destinationUser};
        Arrays.sort(users);

        // return the room id
        return users[0] + users[1];
    }

}