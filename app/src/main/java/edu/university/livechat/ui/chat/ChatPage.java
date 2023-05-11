package edu.university.livechat.ui.chat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import edu.university.livechat.R;


public class ChatPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        // delete first message in scrollview linearlayout
        ScrollView scrollView = findViewById(R.id.scrollView1);
        LinearLayout layout = scrollView.findViewById(R.id.linearLayout1);
//        layout.removeViewAt(0);
//        layout.removeAllViews();
        // sleep
    }
}
