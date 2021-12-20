package com.huawei.finalassignment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.huawei.finalassignment.R;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN = 3000;
    private Animation topAnimation, bottomAnimation;
    LinearLayout logo, name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.name_animation);

        logo = findViewById(R.id.layout_logo);
        name = findViewById(R.id.layout_name);

        logo.setAnimation(topAnimation);
        name.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);
    }

}