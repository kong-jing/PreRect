package xyz.kongjing.prerect.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RelativeLayout;
import xyz.kongjing.prerect.R;
import xyz.kongjing.prerect.callback.PreDataCallBack;
import xyz.kongjing.prerect.camera.CameraPreview;
import xyz.kongjing.prerect.ndk.ImageUtilEngine;
import xyz.kongjing.prerect.util.CameraUtil;
import xyz.kongjing.prerect.util.DisplayUtil;
import xyz.kongjing.prerect.util.ImgUtil;
import xyz.kongjing.prerect.util.Util;
import xyz.kongjing.prerect.view.FaceView;

/**
 * 人脸检测的预览界面
 */
public class FaceActivity extends AppCompatActivity implements PreDataCallBack{
    private static final String TAG = "FaceActivity";
    public static  FaceActivity faceActivity;
    CameraPreview mCameraPreview;
    FaceView mFaceView;
    int orientation = 0;
    int count = 0;
    Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        setContentView(R.layout.activity_face);
        mCameraPreview = (CameraPreview) findViewById(R.id.preview);
        mFaceView = (FaceView) findViewById(R.id.faceview);
        orientation = this.getResources().getConfiguration().orientation;
        initView();
        faceActivity = this;

    }

    @Override protected void onResume() {
        super.onResume();
        mCameraPreview.StartCamera();
    }

    private void initView() {
        mCameraPreview.setPreDataCallBack(this);
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

    Bitmap bitmap;
    byte[] data;
    Camera camera;
    boolean is;
    @Override public void onPreviewFrame(final byte[] data, final Camera camera) {
        this.data = data;
        this.camera = camera;
        ++count;
        Log.w(TAG, "onPreviewFrame: " + data + ", " + count);
        if (data != null && camera != null) {
            int[] agb = ImageUtilEngine.decodeYUV420SP(data, 640, 480);
            bitmap = Bitmap.createBitmap(agb, 640, 480, Bitmap.Config.ARGB_8888);
            //bitmap = ImgUtil.runInPreviewFrame(data, camera);
            Log.e(TAG, "run: " + bitmap);
        }

    }


}
