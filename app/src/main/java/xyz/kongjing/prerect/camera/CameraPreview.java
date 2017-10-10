package xyz.kongjing.prerect.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 实时预览帧 setPreviewCallback
 *
 * @author jing
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, PreviewCallback {
  private static final String TAG = "CameraPreview";

  private Camera mCamera;
  CameraConfigurationManager mCameraConfigurationManager;//配置摄像头

  private int orientation;
  int imageAngle;

  /**
   * 设置屏幕方向
   *
   * @param orientation Configuration.ORIENTATION_LANDSCAPE 或者
   * Configuration.ORIENTATION_PORTRAIT
   */
  public void setScreenOrientation(int orientation) {
    this.orientation = orientation;
  }

  // 摄像头id
  int caremaId = Camera.CameraInfo.CAMERA_FACING_FRONT;

  public int getCaremaId() {
    return caremaId;
  }

  public void setCaremaId(int caremaId) {
    this.caremaId = caremaId;
  }

  private boolean mPreviewing = true;
  private boolean mSurfaceCreated = false;
  Context context;

  /**
   * setReqPrevWH:设置希望的预览分辨率.
   */
  public void setReqPrevWH(int reqPrevW, int reqPrevH) {
    this.reqPrevW = reqPrevW;
    this.reqPrevH = reqPrevH;
  }

  int reqPrevW = 640, reqPrevH = 480;

  public CameraPreview(Context context) {
    super(context);
    this.context = context;
  }

  public CameraPreview(Context context, AttributeSet attrs, int defStyle) {

    super(context, attrs, defStyle);
    this.context = context;
  }

  public CameraPreview(Context context, AttributeSet attrs) {

    super(context, attrs);
    this.context = context;
  }

  public void setCamera(Camera camera) {
    mCamera = camera;
    if (mCamera != null) {
      mCameraConfigurationManager = new CameraConfigurationManager(getContext());
      getHolder().addCallback(this);
      if (mPreviewing) {
        requestLayout();
      } else {
        showCameraPreview();
      }
    }
  }

  @Override public void surfaceCreated(SurfaceHolder surfaceHolder) {
    mSurfaceCreated = true;
  }

  @Override public void surfaceChanged(SurfaceHolder surfaceHolder, int a, int b, int c) {
    if (surfaceHolder.getSurface() == null) {
      return;
    }
    stopCameraPreview();
    showCameraPreview();
  }

  @Override public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    mSurfaceCreated = false;
    stopCameraPreview();
  }

  public void showCameraPreview() {
    if (mCamera != null) {
      try {
        mPreviewing = true;
        mCamera.setPreviewDisplay(getHolder());

        int degree =
            mCameraConfigurationManager.setCameraParametersForPreviewCallBack(mCamera, caremaId,
                reqPrevW, reqPrevH);

        mCamera.startPreview();
        mCamera.setPreviewCallback(CameraPreview.this);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void stopCameraPreview() {
    if (mCamera != null) {
      try {

        mPreviewing = false;
        mCamera.cancelAutoFocus();
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void openFlashlight() {
    if (flashLightAvaliable()) {
    }
  }

  public void closeFlashlight() {
    if (flashLightAvaliable()) {
    }
  }

  private boolean flashLightAvaliable() {
    return mCamera != null && mPreviewing && mSurfaceCreated && getContext().getPackageManager()
        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
  }

  /******************************************************************/
  public Size setPreviewSize() {
    Camera.Parameters parameters = mCamera.getParameters();
    return parameters.getPreviewSize();
  }

  /**
   * 打开摄像头开始预览，但是并未开始识别
   */
  public void StartCamera() {
    if (mCamera != null) {
      return;
    }

    try {
      mCamera = Camera.open(caremaId);
    } catch (Exception e) {
      e.printStackTrace();
    }
    setCamera(mCamera);
  }

  /**
   * 关闭摄像头预览，并且隐藏扫描框
   */
  public void StopCamera() {
    if (mCamera != null) {
      stopCameraPreview();
      mCamera.release();
      mCamera = null;
    }
  }

  @Override public void onPreviewFrame(byte[] data, Camera camera) {
    Log.e(TAG, "onPreviewFrame: " + data.toString());
    switch (caremaId){

      case Camera.CameraInfo.CAMERA_FACING_FRONT://前置摄像头
        if (Configuration.ORIENTATION_PORTRAIT == orientation){//竖屏

        }
        break;
      case Camera.CameraInfo.CAMERA_FACING_BACK://后置摄像头
        if (Configuration.ORIENTATION_PORTRAIT == orientation){

        }
        break;
      default:break;
    }

  }

  /**
   * 切换摄像头
   */
  public int switchCarema() {
    StopCamera();
    if (caremaId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      caremaId = Camera.CameraInfo.CAMERA_FACING_BACK;
    } else {
      caremaId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    }
    StartCamera();
    return caremaId;
  }
}