package com.jing.prerect.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageButton;

import com.jing.prerect.camera.CameraInterface;
import com.jing.prerect.detect.GoogleFaceDetect;
import com.jing.prerect.util.EventUtil;
import com.jing.camerapreview.R;
import com.jing.prerect.model.FaceView;
import com.jing.prerect.view.CameraSurfaceView;

/**
 * Created by knjin on 17/4/28.
 */

public class CameraActivity extends Activity {
    CameraSurfaceView surfaceView = null;
    ImageButton shutterBtn;
    ImageButton switchBtn;
    FaceView faceView;
    float previewRate = -1f;
    private MainHandler mMainHandler = null;
    GoogleFaceDetect googleFaceDetect = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

    }

    private  class MainHandler extends Handler {

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what){
                case EventUtil.UPDATE_FACE_RECT:
                    Camera.Face[] faces = (Camera.Face[]) msg.obj;
                    faceView.setFaces(faces);
                    break;
                case EventUtil.CAMERA_HAS_STARTED_PREVIEW:
                    startGoogleFaceDetect();
                    break;
            }
            super.handleMessage(msg);
        }

    }

    private void takePicture(){
        CameraInterface.getInstance().doTakePicture();
        mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
    }
    private void switchCamera(){
        stopGoogleFaceDetect();
        int newId = (CameraInterface.getInstance().getCameraId() + 1)%2;
        CameraInterface.getInstance().doStopCamera();
        CameraInterface.getInstance().doOpenCamera(null, newId);
        CameraInterface.getInstance().doStartPreview(surfaceView.getSurfaceHolder(), previewRate);
        mMainHandler.sendEmptyMessageDelayed(EventUtil.CAMERA_HAS_STARTED_PREVIEW, 1500);
//		startGoogleFaceDetect();

    }
    private void startGoogleFaceDetect(){
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if(params.getMaxNumDetectedFaces() > 0){
            if(faceView != null){
                faceView.clearFaces();
                faceView.setVisibility(View.VISIBLE);
            }
            CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(googleFaceDetect);
            CameraInterface.getInstance().getCameraDevice().startFaceDetection();
        }
    }
    private void stopGoogleFaceDetect(){
        Camera.Parameters params = CameraInterface.getInstance().getCameraParams();
        if(params.getMaxNumDetectedFaces() > 0){
            CameraInterface.getInstance().getCameraDevice().setFaceDetectionListener(null);
            CameraInterface.getInstance().getCameraDevice().stopFaceDetection();
            faceView.clearFaces();
        }
    }
}
