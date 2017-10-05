package xyz.kongjing.prerect.util;

import android.hardware.Camera;
import android.os.Build;
import android.view.Surface;

public class CameraUtil {
    /**
     * getCameraId获得摄像头id. <br/>
     *
     * @param facing
     * @return -1表示没有对应摄像头  -2低于android2.3
     * @author:284891377 Date: 2016-4-18 上午11:46:24
     * @since JDK 1.7
     */
    public static boolean isHasCamera(int facing) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {//小于2.3
            return false;
        }
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);

            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }


    public static void setCameraDisplayOrientation(int rotation, int cameraId,
                                                   Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        // int rotation = activity.getWindowManager ().getDefaultDisplay
        // ().getRotation ();
        int degrees = 0;
        switch (rotation) {
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
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else {
            // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
}