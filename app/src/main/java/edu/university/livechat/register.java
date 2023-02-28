package edu.university.livechat;

import android.app.Activity;
import android.os.Bundle;
import edu.university.livechat.databinding.ActivityRegisterBinding;

public class register extends Activity {
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}