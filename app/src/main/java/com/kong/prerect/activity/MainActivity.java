package com.kong.prerect.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kong.camerapreview.R;

public class MainActivity extends AppCompatActivity {
    private Button btn_preview,btn_detect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_detect = (Button) findViewById(R.id.btn_detect);
        btn_detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FaceActivity.class));
            }
        });
    }



}
