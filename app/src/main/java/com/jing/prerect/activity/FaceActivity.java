package com.jing.prerect.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jing.camerapreview.R;

/**
 * 人脸检测的预览界面
 */
public class FaceActivity extends AppCompatActivity {
    public static  FaceActivity faceActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        faceActivity = this;
    }
}
