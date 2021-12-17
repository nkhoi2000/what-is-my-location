package com.huawei.finalassignment.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.huawei.finalassignment.R;
import com.huawei.finalassignment.models.Account;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Account account;
    private ImageView img_avatar;
    private TextView txt_full_name, txt_email, txt_name;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        if (getIntent().hasExtra("Account")) {
            account = getIntent().getParcelableExtra("Account");

            img_avatar = findViewById(R.id.img_avatar);
            txt_full_name = findViewById(R.id.txt_full_name);
            txt_email = findViewById(R.id.txt_email);
            txt_name = findViewById(R.id.txt_name);

            displayProfile();
        }

        /*---toolbar---*/
        setSupportActionBar(toolbar);
        /*---navView---*/
        navView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.menu_profile);

    }

    private void displayProfile() {
        txt_name.setText(account.getFullname());
        txt_full_name.setText(account.getFullname());
        txt_email.setText(account.getEmail());
        Glide.with(this).load(account.getAvatarString()).into(img_avatar);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                //get intent
                if (getIntent().hasExtra("Account")) {
                    account = getIntent().getParcelableExtra("Account");
                    Intent intent = new Intent(ProfileActivity.this, MapActivity.class);
                    intent.putExtra("Account", account);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Failed to load your profile!", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.menu_profile:
                break;
            case R.id.menu_logout:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
        }
        return true;
    }
}