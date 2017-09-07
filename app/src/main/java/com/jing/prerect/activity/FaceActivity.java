package com.jing.prerect.activity;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;
import com.jing.camerapreview.R;
import com.jing.prerect.camera.CameraPreview;
import com.jing.prerect.util.CameraUtil;
import com.jing.prerect.util.DisplayUtil;
import com.jing.prerect.util.Util;

/**
 * 人脸检测的预览界面
 */
public class FaceActivity extends AppCompatActivity {
    public static  FaceActivity faceActivity;
    CameraPreview mCameraPreview;
    int orientation = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        mCameraPreview = (CameraPreview) findViewById(R.id.preview);
        initView();
        faceActivity = this;
    }

    @Override protected void onResume() {
        super.onResume();
        mCameraPreview.StartCamera();
    }

    private void initView() {
        //屏幕分辨率
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels - DisplayUtil.dip2px(this, 45)
            - Util.getStatusBarHeight(this);

        mCameraPreview.setScreenOrientation(orientation);
        if (CameraUtil.isHasCamera(Camera.CameraInfo.CAMERA_FACING_FRONT)) {//有前置摄像头
            mCameraPreview.setCaremaId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }else {
            mCameraPreview.setCaremaId(Camera.CameraInfo.CAMERA_FACING_BACK);
        }


    }

    @Override protected void onStop() {
        super.onStop();
        mCameraPreview.StopCamera();
    }
}
