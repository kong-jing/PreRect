package com.aheadle.camerapreview;

import android.content.Intent;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.widget.Button;

import com.aheadle.camerapreview.activity.FaceActivity;
import com.aheadle.camerapreview.activity.PushActivity;

public class MainActivity extends AppCompatActivity {
    private Button btn_preview,btn_detect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_preview = (Button) findViewById(R.id.btn_preview);
        btn_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PushActivity.class));
            }
        });
        btn_detect = (Button) findViewById(R.id.btn_detect);
        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FaceActivity.class));
            }
        });
    }



}
