package com.huawei.finalassignment.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.huawei.finalassignment.R;
import com.huawei.finalassignment.models.Account;

public class HomeActivity extends AppCompatActivity {

    private Account account;

    private DrawerLayout drawerLayout;
    private ImageView img_menu_nav, img_menu_avatar;
    private NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        img_menu_nav = findViewById(R.id.img_menu_nav);
        img_menu_avatar = findViewById(R.id.img_menu_avatar);
        nav_view = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);

        img_menu_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        nav_view.setItemIconTintList(null);

        NavigationUI.setupWithNavController(nav_view, navController);
        //get intent
        if (getIntent().hasExtra("Account")) {
            account = getIntent().getParcelableExtra("Account");
        }
    }

}