package com.huawei.finalassignment.activities;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_WIFI_STATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.huawei.finalassignment.R;
import com.huawei.finalassignment.models.Account;
import com.huawei.finalassignment.models.Location;
import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.LatLng;

import java.util.concurrent.Executor;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private MapView mMapView;
    private HuaweiMap hMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private Location currentLocation;
    private DrawerLayout drawerLayout;

    @Override
    public boolean bindIsolatedService(Intent service, int flags, String instanceName, Executor executor, ServiceConnection conn) {
        return super.bindIsolatedService(service, flags, instanceName, executor, conn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //assign variable
        ImageView share = findViewById(R.id.img_btn_share);
        ImageView scanner = findViewById(R.id.img_btn_scan);
        mMapView = findViewById(R.id.mapview);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        //listener
        /*---mapView---*/
        getMapView(savedInstanceState);
        /*---toolbar---*/
        setSupportActionBar(toolbar);
        /*---navView---*/
        navView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.menu_home);

        /*---buttons---*/
        scanner.setOnClickListener(v -> {
            IntentIntegrator intent = new IntentIntegrator(MapActivity.this);
            intent.setBeepEnabled(true);
            intent.setOrientationLocked(true);
            intent.setCaptureActivity(Capture.class);
            intent.initiateScan();
        });
        share.setOnClickListener(v -> {
            if (currentLocation != null) {
                Intent intent = new Intent(MapActivity.this, ScanActivity.class);
                intent.putExtra("locationString", currentLocation.toString());
                startActivity(intent);
            } else
                Toast.makeText(getApplicationContext(), "Cannot find your location!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult.getContents() != null) {
            String[] locationArray = intentResult.getContents().split("&", -1);
            for (String item: locationArray){
                Log.d("locationArray: ", item);
            }
            Location getLocation = new Location(
                    Double.parseDouble(locationArray[0]),
                    Double.parseDouble(locationArray[1]));

            Toast.makeText(getApplicationContext(), getLocation.toString(), Toast.LENGTH_SHORT).show();
            moveCamera(hMap, getLocation);
        } else {
            Toast.makeText(getApplicationContext(), "You did not scan anything!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void getMapView(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey");
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
        // If the API level is 23 or higher (Android 6.0 or later), you need to dynamically apply for permissions.
        Log.i("sdk", "sdk >= 23 M");
        // Check whether your app has the specified permission and whether the app operation corresponding to the permission is allowed.
        if (ActivityCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions for your app.
            String[] strings =
                    {ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            // Request permissions.
            ActivityCompat.requestPermissions(this, strings, 1);
        }
    }

    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_WIFI_STATE})
    @Override
    public void onMapReady(HuaweiMap huaweiMap) {
        hMap = huaweiMap;
        hMap.setMyLocationEnabled(true);
        hMap.getUiSettings().setMyLocationButtonEnabled(true);
        getCurrentLocation(huaweiMap);
    }

    private void moveCamera(HuaweiMap huaweiMap, Location location) {
        float zoom = 8.0f;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        huaweiMap.moveCamera(cameraUpdate);
    }

    private void getCurrentLocation(HuaweiMap huaweiMap) {
        LatLng currentPos = huaweiMap.getCameraPosition().target;
        currentLocation = new Location(currentPos.latitude, currentPos.longitude);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                //get intent
                if (getIntent().hasExtra("Account")) {
                    Account account = getIntent().getParcelableExtra("Account");
                    Intent intent = new Intent(MapActivity.this, ProfileActivity.class);
                    intent.putExtra("Account", account);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Failed to load your profile!", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.menu_home:
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