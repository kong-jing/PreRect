package com.jing.prerect.activity;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.jing.camerapreview.R;

import java.io.IOException;

/**
 * 简单的一个预览打开摄像头
 */
public class PushActivity extends AppCompatActivity {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);

        mSurfaceView = (SurfaceView) findViewById(R.id.push_sv);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                doOpenCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                releaseCamera();
            }


        });
    }

    //初始化预览界面
    private void doOpenCamera() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        Camera.Parameters parameters = mCamera.getParameters();
        Camera.Size size = (Camera.Size) parameters.getSupportedPictureSizes();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        }catch (IOException e){
            e.printStackTrace();
        }
        setCameraDisplayOrientation(this, Camera.CameraInfo.CAMERA_FACING_FRONT,mCamera);
        mCamera.startPreview();
    }


    //设置相机的角度与方向
    private void setCameraDisplayOrientation(PushActivity pushActivity, int cameraId, Camera mCamera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,info);
        int rotation = pushActivity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation){
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:break;
        }
        int result ;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degrees) % 360;
            result = (360 -result) % 360;
        } else {
            result = (info.orientation - degrees+360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }

    //关闭释放相机
    private void releaseCamera() {
        if (null != mCamera){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

}
