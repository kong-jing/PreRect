package com.aheadle.camerapreview.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.aheadle.camerapreview.activity.FaceActivity;
import com.aheadle.camerapreview.activity.PushActivity;

import java.io.IOException;

/**
 * Created by aheadle on 16/6/30.
 * faceactivity中使用的cameraSurfaceview预览界面
 */
public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Context mContext;
    private Camera mCamera;

    public CameraSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }

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

    //初始化预览界面
    private void doOpenCamera() {
        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        }catch (IOException e){
            e.printStackTrace();
        }
        setCameraDisplayOrientation(FaceActivity.faceActivity, Camera.CameraInfo.CAMERA_FACING_FRONT,mCamera);
        mCamera.startPreview();
    }

    //设置相机的角度与方向
    private void setCameraDisplayOrientation(FaceActivity faceActivity, int cameraId, Camera mCamera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId,info);
        int rotation = faceActivity.getWindowManager().getDefaultDisplay().getRotation();
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
