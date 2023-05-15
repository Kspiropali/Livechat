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
        // create about page
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("This is a simple LiveChat app that allows users to chat with each other, instantly." +
                        " This app focuses on performance and security. It is built with Java and Spring Boot on the backend, and Java and Android on the frontend." +
                        "If you have any questions, please contact me at the github link below.")
                .setImage(R.drawable.chat_main_logo)
                .addGroup("Connect with me:")
                .addEmail("kristianspd1@gmail.com")
                .addWebsite("https://kspiropali.github.io")
                .addGitHub("Kspiropali")
                .addGroup("Connect with the University:")
                .addEmail("alumni@londonmet.ac.uk")
                .addWebsite("https://www.londonmet.ac.uk/")
                .addFacebook("LondonMetUni")
                .addTwitter("LondonMetUni")
                .addYoutube("UCJXZ9nZ9xRJZpJr8DTEyvIA")
                .addGroup("Miscellaneous:")
                .addItem(new Element().setTitle(
                        "Current Version: 1.0-STABLE\n" +
                                "Created By:    Kristian Spiropali\n" +
                                "Module Leader: Dr. TingKai Wang\n" +
                                "Module Code:   CS6051\n" +
                                "University:    London Metropolitan University\n" +
                                "Course:        Computer Science BSc\n" +
                                "Year:            2023\n"))
                .addGroup("Return to the main page")
                .create();

        aboutPage.findViewById(mehdi.sakout.aboutpage.R.id.about_providers).setOnClickListener(v -> finish());
        setContentView(aboutPage);
    }
}