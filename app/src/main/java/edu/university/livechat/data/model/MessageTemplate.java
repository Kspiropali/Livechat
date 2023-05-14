package edu.university.livechat.data.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.university.livechat.R;

@SuppressLint("ViewConstructor")
public class MessageTemplate extends ConstraintLayout {
    private final Context context;

    public MessageTemplate(Context context, String message, boolean isSelf, String timestamp, String username) {
        super(context);
        this.context = context;

        if (isSelf) {
            myMessage(message, timestamp, username);
        } else {
            othersMessage(message, timestamp, username);
        }
    }


    public void othersMessage(String message, String timestamp, String otherUser) {


        // Create message date TextView
        TextView messageDate = new TextView(context);
        messageDate.setId(View.generateViewId());
        messageDate.setText(timestamp);
        messageDate.setTextColor(0xFF000000);
        messageDate.setTypeface(messageDate.getTypeface(), Typeface.BOLD);
        messageDate.setTextSize(17f);
        messageDate.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));
        LayoutParams messageDateParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        messageDateParams.topToTop = ConstraintSet.PARENT_ID;
        messageDateParams.endToEnd = ConstraintSet.PARENT_ID;
        messageDateParams.topMargin = dpToPx(2);
        messageDateParams.setMarginEnd(dpToPx(60));
        addView(messageDate, messageDateParams);

        // Create card view
        CardView cardView = new CardView(context);
        cardView.setId(View.generateViewId());
        cardView.setCardBackgroundColor(0xFF774df2);
        cardView.setCardElevation(0);
        cardView.setRadius(dpToPx(12));
        cardView.setPreventCornerOverlap(false);
        cardView.setUseCompatPadding(true);
        LayoutParams cardViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        cardViewParams.topToBottom = messageDate.getId();
        cardViewParams.rightMargin = dpToPx(75);
        cardViewParams.endToEnd = ConstraintSet.PARENT_ID;
        addView(cardView, cardViewParams);

        // create imageview for user profile pic
        ImageView profilePic = new ImageView(context);
        profilePic.setId(View.generateViewId());
        profilePic.setImageResource(R.drawable.user);
        LayoutParams profilePicParams = new LayoutParams(dpToPx(30), dpToPx(30));
        profilePicParams.rightMargin = dpToPx(20);
        profilePicParams.bottomToBottom = LayoutParams.PARENT_ID;
        profilePicParams.endToEnd = LayoutParams.PARENT_ID;
        profilePicParams.topToTop = LayoutParams.PARENT_ID;
        this.addView(profilePic, profilePicParams);

        // create textview for username
        TextView username = new TextView(context);
        username.setId(View.generateViewId());
        username.setText(otherUser);
        username.setTextColor(0xFF000000);
        username.setTypeface(username.getTypeface(), Typeface.BOLD);
        username.setTextSize(17f);
        username.setGravity(Gravity.CENTER);
        LayoutParams usernameParams = new LayoutParams(dpToPx(70), dpToPx(30));
        usernameParams.rightMargin = dpToPx(-4);
        usernameParams.bottomToBottom = LayoutParams.PARENT_ID;
        usernameParams.endToEnd = LayoutParams.PARENT_ID;
        this.addView(username, usernameParams);

        // create imageview for arrow left
        ImageView arrowLeft = new ImageView(context);
        arrowLeft.setId(View.generateViewId());
        arrowLeft.setImageResource(R.drawable.arrow_left);
        LayoutParams arrowLeftParams = new LayoutParams(dpToPx(27), dpToPx(27));
        arrowLeftParams.topMargin = dpToPx(28);
        arrowLeftParams.rightMargin = dpToPx(55);
        arrowLeftParams.endToEnd = LayoutParams.PARENT_ID;
        arrowLeftParams.topToTop = LayoutParams.PARENT_ID;
        arrowLeftParams.bottomToBottom = LayoutParams.PARENT_ID;
        this.addView(arrowLeft, arrowLeftParams);

        // Create message linear layout
        LinearLayout messageLinearLayout = new LinearLayout(context);
        messageLinearLayout.setId(View.generateViewId());
        messageLinearLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams messageLinearLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        cardView.addView(messageLinearLayout, messageLinearLayoutParams);

        // Create message text TextView
        TextView messageText = new TextView(context);
        messageText.setId(View.generateViewId());
        messageText.setText(message);
        messageText.setTextColor(0xFFFFFFFF);
        messageText.setTextSize(16f);
        messageText.setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(10));
        messageText.setMaxWidth(dpToPx(260));
        LayoutParams messageTextParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        messageLinearLayout.addView(messageText, messageTextParams);

        // Set ConstraintSet
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(messageDate.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(cardView.getId(), ConstraintSet.TOP, messageDate.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(cardView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.applyTo(this);
    }


    public void myMessage(String message, String messageTimeStamp, String myUsername) {
        // Create TextView for message date
        TextView messageDate = new TextView(context);
        messageDate.setId(View.generateViewId());
        messageDate.setText(messageTimeStamp);
        messageDate.setTypeface(messageDate.getTypeface(), Typeface.BOLD);
        messageDate.setTextColor(0xFF000000);
        messageDate.setTextSize(17f);
        messageDate.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));
        LayoutParams messageDateLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        messageDateLayoutParams.setMarginStart(dpToPx(-80));
        messageDateLayoutParams.topToTop = LayoutParams.PARENT_ID;
        messageDateLayoutParams.startToStart = LayoutParams.PARENT_ID;
        messageDateLayoutParams.topMargin = dpToPx(4);
        addView(messageDate, messageDateLayoutParams);

        // Create CardView for message
        CardView messageCardView = new CardView(context);
        messageCardView.setId(View.generateViewId());
        messageCardView.setCardBackgroundColor(0xFF774df2);
        messageCardView.setRadius(dpToPx(12));
        messageCardView.setCardElevation(0);
        messageCardView.setPreventCornerOverlap(false);
        messageCardView.setUseCompatPadding(true);
        LayoutParams messageCardViewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        messageCardViewLayoutParams.leftMargin = dpToPx(65);
        messageCardViewLayoutParams.topToBottom = messageDate.getId();
        messageCardViewLayoutParams.startToStart = LayoutParams.PARENT_ID;
        addView(messageCardView, messageCardViewLayoutParams);

        // create ImageView for user profile picture
        ImageView userProfilePicture = new ImageView(context);
        userProfilePicture.setId(View.generateViewId());
        userProfilePicture.setImageResource(R.drawable.user);
        LayoutParams userProfilePictureLayoutParams = new LayoutParams(dpToPx(30), dpToPx(30));
        userProfilePictureLayoutParams.topToTop = LayoutParams.PARENT_ID;
        userProfilePictureLayoutParams.startToStart = LayoutParams.PARENT_ID;
        userProfilePictureLayoutParams.endToStart = messageCardView.getId();
        userProfilePictureLayoutParams.topMargin = dpToPx(34);
        this.addView(userProfilePicture, userProfilePictureLayoutParams);

        // create TextView for user name
        TextView userName = new TextView(context);
        userName.setId(View.generateViewId());
        userName.setText(myUsername);
        userName.setTextColor(0xFF000000);
        userName.setTextSize(17f);
        userName.setTypeface(userName.getTypeface(), Typeface.BOLD);
        userName.setGravity(Gravity.CENTER);
        LayoutParams userNameLayoutParams = new LayoutParams(dpToPx(50), dpToPx(26));
        userNameLayoutParams.endToStart = messageCardView.getId();
        userNameLayoutParams.topToBottom = userProfilePicture.getId();
        userNameLayoutParams.startToStart = LayoutParams.PARENT_ID;
        this.addView(userName, userNameLayoutParams);

        // create ImageView for the arrow right
        ImageView arrowRight = new ImageView(context);
        arrowRight.setId(View.generateViewId());
        arrowRight.setImageResource(R.drawable.arrow_right);
        LayoutParams arrowRightLayoutParams = new LayoutParams(dpToPx(27), dpToPx(27));
        arrowRightLayoutParams.bottomToBottom = LayoutParams.PARENT_ID;
        arrowRightLayoutParams.endToStart = messageCardView.getId();
        arrowRightLayoutParams.startToStart = LayoutParams.PARENT_ID;
        arrowRightLayoutParams.leftMargin = dpToPx(48);
        arrowRightLayoutParams.bottomMargin = dpToPx(19);
        this.addView(arrowRight, arrowRightLayoutParams);


        // Create LinearLayout for message text
        LinearLayout messageLinearLayout = new LinearLayout(context);
        messageLinearLayout.setId(View.generateViewId());
        messageLinearLayout.setOrientation(LinearLayout.VERTICAL);
        messageCardView.addView(messageLinearLayout);

        // Create TextView for message text
        TextView messageText = new TextView(context);
        messageText.setId(View.generateViewId());
        messageText.setText(message);
        messageText.setTextColor(0xFFFFFFFF);
        messageText.setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(0));
        messageText.setTextSize(16f);
        messageText.setMaxWidth(dpToPx(260));
        messageText.setGravity(Gravity.FILL);
        LinearLayout.LayoutParams messageTextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        messageTextLayoutParams.bottomMargin = dpToPx(8);
        messageTextLayoutParams.leftMargin = dpToPx(5);
        messageTextLayoutParams.rightMargin = dpToPx(5);
        messageLinearLayout.addView(messageText, messageTextLayoutParams);

        // Create constraint set and apply constraints
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(messageDate.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, dpToPx(2));
        constraintSet.connect(messageDate.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, dpToPx(45));
        constraintSet.connect(messageCardView.getId(), ConstraintSet.TOP, messageDate.getId(), ConstraintSet.BOTTOM, 0);

        constraintSet.applyTo(this);

    }

    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}