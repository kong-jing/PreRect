package xyz.kongjing.prerect.callback;

import android.hardware.Camera;

/**
 * 摄像头回调数据
 */
public interface PreDataCallBack {
	void onPreviewFrame(byte[] data, Camera carema);
}