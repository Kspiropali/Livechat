package edu.university.livechat.ui.about;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import edu.university.livechat.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutApp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("This is a simple LiveChat app that allows users to chat with each other, instantly." +
                        " This app focuses on performance and security. It is built with Java and Spring Boot on the backend, and Java and Android on the frontend." +
                        "If you have any questions, please contact me at the github link below.")
                .setImage(R.drawable.chat_main_logo)
                .addItem(new Element().setTitle("Version 1.0-STABLE"))
                .addGroup("Connect with me:")
                .addEmail("kristianspd1@gmail.com")
                .addWebsite("https://kspiropali.github.io")
                .addPlayStore("com.edu.livechat")
                .addGitHub("Kspiropali")
                .create();

        setContentView(aboutPage);
    }
}