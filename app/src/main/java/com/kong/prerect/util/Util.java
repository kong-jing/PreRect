package com.kong.prerect.util;

import android.content.Context;
import android.graphics.Matrix;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import java.lang.reflect.Field;

public class Util {

    public static void prepareMatrix(Matrix matrix, boolean mirror, int displayOrientation,
                                     int viewWidth, int viewHeight) {
        // Need mirror for front camera.
        matrix.setScale(mirror ? -1 : 1, 1);
        // This is the value for android.hardware.Camera.setDisplayOrientation.
        matrix.postRotate(displayOrientation);
        // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
        // UI coordinates range from (0, 0) to (width, height).
        matrix.postScale(viewWidth / 2000f, viewHeight / 2000f);
        matrix.postTranslate(viewWidth / 2f, viewHeight / 2f);
    }

    /**
     * 获取状态栏高度
     *
     * @param ctx s上下文
     * @return
     */
    public static int getStatusBarHeight(Context ctx) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 40;//40状态栏高度默认40
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = ctx.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {

            e1.printStackTrace();
        }
        return sbar;
    }

    public static boolean checkDeviceHasNavigationBar(Context activity) {

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
            .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
            .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

}