package edu.university.livechat.data.model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.university.livechat.R;

public class ChatTemplate {
    private final Context context;

    public ChatTemplate(Context context) {
        this.context = context;
    }

    public LinearLayout getOnlineUserFrameView(String username) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        // Create the ImageView
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.user);


        // Create the TextView
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(0xFF000000);
        textView.setText(username);
        textView.setTextSize(35);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setPadding(10, 10, 0, 0);

        // Add the ImageView and TextView to the LinearLayout
        linearLayout.addView(imageView);
        linearLayout.addView(textView);

        return linearLayout;
    }
}
