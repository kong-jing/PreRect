package com.knjin.prerect.detect;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.knjin.prerect.util.EventUtil;

public class GoogleFaceDetect implements Camera.FaceDetectionListener {
	private static final String TAG = "YanZi";
	private Context mContext;
	private Handler mHander;

	public GoogleFaceDetect(Context c, Handler handler) {
		mContext = c;
		mHander = handler;
	}

	@Override
	public void onFaceDetection(Camera.Face[] faces, Camera camera) {
		// TODO Auto-generated method stub

		Log.i(TAG, "onFaceDetection...");
		if (faces != null) {

			Message m = mHander.obtainMessage();
			m.what = EventUtil.UPDATE_FACE_RECT;
			m.obj = faces;
			m.sendToTarget();
		}
	}

}