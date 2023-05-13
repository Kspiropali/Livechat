package edu.university.livechat.data.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint("ViewConstructor")
public class MessageTemplate extends ConstraintLayout {
    private final Context context;

    public MessageTemplate(Context context, String message, boolean isSelf) {
        super(context);
        this.context = context;

        if (isSelf) {
            myMessage(message);
        } else {
            othersMessage(message);
        }
    }


    public void othersMessage(String message){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, hh:mm a", Locale.getDefault());

        // Create message date TextView
        TextView messageDate = new TextView(context);
        messageDate.setId(View.generateViewId());
        messageDate.setText(dateFormat.format(date));
        messageDate.setTextColor(0xFFC0C0C0);
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
        cardViewParams.endToEnd = ConstraintSet.PARENT_ID;
        addView(cardView, cardViewParams);

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


    public void myMessage(String message) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, hh:mm a", Locale.getDefault());
        // Create TextView for message date
        TextView messageDate = new TextView(context);
        messageDate.setId(View.generateViewId());
        messageDate.setText(dateFormat.format(date));
        messageDate.setTextColor(0xFFC0C0C0);
        messageDate.setPadding(dpToPx(8), dpToPx(4), dpToPx(8), dpToPx(4));
        LayoutParams messageDateLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // set tools:layout_editor_absoluteX="53dp"
        messageDateLayoutParams.setMarginStart(dpToPx(-150));
        messageDateLayoutParams.topToTop = LayoutParams.PARENT_ID;
        messageDateLayoutParams.startToStart = LayoutParams.PARENT_ID;
        messageDateLayoutParams.topMargin = dpToPx(2);
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
        messageCardViewLayoutParams.topToBottom = messageDate.getId();
        messageCardViewLayoutParams.startToStart = LayoutParams.PARENT_ID;
        addView(messageCardView, messageCardViewLayoutParams);

        // Create LinearLayout for message text
        LinearLayout messageLinearLayout = new LinearLayout(context);
        messageLinearLayout.setId(View.generateViewId());
        messageLinearLayout.setOrientation(LinearLayout.VERTICAL);
        messageCardView.addView(messageLinearLayout);

        // Create TextView for message text
        TextView messageText = new TextView(context);
        messageText.setId(View.generateViewId());
        messageText.setText(message);
        messageText.setTextColor(Color.parseColor("#ffffff"));
        messageText.setPadding(dpToPx(12), dpToPx(8), dpToPx(12), dpToPx(0));
        messageText.setTextSize(16f);
        messageText.setMaxWidth(dpToPx(260));
        LinearLayout.LayoutParams messageTextLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        messageTextLayoutParams.bottomMargin = dpToPx(8);
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