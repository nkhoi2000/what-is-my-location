package com.huawei.finalassignment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.huawei.finalassignment.R;
import com.huawei.finalassignment.models.Location;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ScanActivity extends AppCompatActivity {
    private Location location;
    private ImageView btn_back, iv_output;
    private String locationString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //get intent
        if (getIntent().hasExtra("location")) {
            location = getIntent().getParcelableExtra("location");
        }
        //get location to string
        locationString = location.toString();
        //Assign variable
        iv_output = findViewById(R.id.iv_output);
        btn_back = findViewById(R.id.btn_back);

        //get location to matrix
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            //init bit matrix
            BitMatrix matrix = writer.encode(locationString, BarcodeFormat.QR_CODE
                    , 350, 350);
            //barcode encoder
            BarcodeEncoder encoder = new BarcodeEncoder();
            //bitmap
            Bitmap bitmap = encoder.createBitmap(matrix);
            //set bitmap on image
            iv_output.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}