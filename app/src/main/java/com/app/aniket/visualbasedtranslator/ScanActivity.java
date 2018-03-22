package com.app.aniket.visualbasedtranslator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ScanActivity extends AppCompatActivity {

    static int part = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        part = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
    }

    public void LiveCapture(View view) {
        // Do something in response to button
        Intent intent1 = new Intent(this, LiveCapture.class);
        startActivity(intent1);
    }

    public void ChoosefromGallary(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ChoosefromGallary.class);
        startActivity(intent);
    }
}
