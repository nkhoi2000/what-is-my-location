package com.huawei.finalassignment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.huawei.finalassignment.R;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ScanActivity extends AppCompatActivity {
    private ImageView btn_back, iv_output;
    private String locationString;
    private final String temp = "11.926220" + "&" + "109.161438";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //get intent
        if (getIntent().hasExtra("locationString")) {
            locationString = getIntent().getStringExtra("locationString");
        } else {
            finish();
        }
        //assign variable
        iv_output = findViewById(R.id.iv_output);
        btn_back = findViewById(R.id.btn_back);

        //get location to matrix
        MultiFormatWriter writer = new MultiFormatWriter();

        try {
            //init bit matrix
            BitMatrix matrix = writer.encode(temp, BarcodeFormat.QR_CODE
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

        btn_back.setOnClickListener(v -> finish());

    }

}