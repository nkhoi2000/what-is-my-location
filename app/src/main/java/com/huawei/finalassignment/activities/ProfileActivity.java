package com.huawei.finalassignment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huawei.finalassignment.R;
import com.huawei.finalassignment.models.Account;

public class ProfileActivity extends AppCompatActivity {
    private Account account;

    private ImageView img_avatar;
    private TextView txt_full_name, txt_email, txt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getIntent().hasExtra("Account")) {
            account = getIntent().getParcelableExtra("Account");

            img_avatar = findViewById(R.id.img_avatar);
            txt_full_name = findViewById(R.id.txt_full_name);
            txt_email = findViewById(R.id.txt_email);
            txt_name = findViewById(R.id.txt_name);

            displayProfile();
        }

    }

    private void displayProfile(){
        txt_name.setText(account.getFullname());
        txt_full_name.setText(account.getFullname());
        txt_email.setText(account.getEmail());
        Glide.with(this).load(account.getAvatarString()).into(img_avatar);
    }
}