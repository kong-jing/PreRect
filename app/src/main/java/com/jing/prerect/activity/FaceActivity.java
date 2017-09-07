package com.jing.prerect.activity;

import android.content.res.Configuration;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.widget.RelativeLayout;
import com.jing.camerapreview.R;
import com.jing.prerect.camera.CameraPreview;
import com.jing.prerect.util.CameraUtil;
import com.jing.prerect.util.DisplayUtil;
import com.jing.prerect.util.Util;
import com.jing.prerect.view.FaceRectView;

/**
 * 人脸检测的预览界面
 */
public class FaceActivity extends AppCompatActivity {
    public static  FaceActivity faceActivity;
    CameraPreview mCameraPreview;
    FaceRectView mFaceView;
    int orientation = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        mCameraPreview = (CameraPreview) findViewById(R.id.preview);
        mFaceView = (FaceRectView) findViewById(R.id.faceview);
        orientation = this.getResources().getConfiguration().orientation;
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
        int navigationBarnH = 0;
        if (Util.checkDeviceHasNavigationBar(this)) {
            navigationBarnH = Util.getStatusBarHeight(this);
        }
        int height = dm.heightPixels - DisplayUtil.dip2px(this, 45)
            - Util.getStatusBarHeight(this) - navigationBarnH;

        mCameraPreview.setScreenOrientation(orientation);
        if (CameraUtil.isHasCamera(Camera.CameraInfo.CAMERA_FACING_FRONT)) {//有前置摄像头
            mCameraPreview.setCaremaId(Camera.CameraInfo.CAMERA_FACING_FRONT);
        }else {
            mCameraPreview.setCaremaId(Camera.CameraInfo.CAMERA_FACING_BACK);
        }


        if(orientation == Configuration.ORIENTATION_LANDSCAPE){//横屏
            //屏幕方向
            int previewH = height;
            int previewW = (int) (height * 1.0 * 640 / 480);
            //调整布局大小
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(previewW, previewH);
            mCameraPreview.setLayoutParams(params);
            mFaceView.setLayoutParams(params);
            mFaceView.setSurface(previewW, previewH, 640, 480);
        }else{
            int previewW = width;
            int previewH = (int) (width * 1.0 * 640 /480);
            //调整布局大小
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(previewW, previewH);
            mCameraPreview.setLayoutParams(params);
            mFaceView.setLayoutParams(params);
            mFaceView.setSurface(previewW, previewH, 480, 640);
        }

    }

    @Override protected void onStop() {
        super.onStop();
        mCameraPreview.StopCamera();
    }
}
