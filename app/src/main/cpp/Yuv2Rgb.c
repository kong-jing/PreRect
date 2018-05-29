//
// Created by Jing on 18/5/29.
//
#include <jni.h>

JNIEXPORT jintArray JNICALL
Java_xyz_kongjing_prerect_ndk_ImageUtilEngine_decodeYUV420SP(JNIEnv* env, jobject thiz,
                                                                  jbyteArray buf, jint width, jint height){
    jbyte * yuv420sp = (*env)->GetByteArrayElements(env, buf, 0);
    int frameSize = width * height;
    jint rgb[frameSize];//新图像像素

    int i = 0, j = 0, yp = 0;
    int uvp = 0, u = 0, v = 0;
    for (int j = 0, yp = 0; j < height; j++) {
        uvp = frameSize + (j >> 1) * width;
        u = 0;
        v = 0;
        for (int i = 0; i < width; i++, yp++) {
            int y =(0xff & ((int) yuv420sp[yp])) - 16;
            if (y < 0)
                y = 0;
            if ((i & 1) == 0)
            {
                v = (0xff & yuv420sp[uvp++]) - 128;
                u = (0xff & yuv420sp[uvp++]) - 128;
            }

            int y1192 = 1192 * y;
            int r = (y1192 + 1634 * v);
            int g = (y1192 - 833 * v - 400 * u);
            int b = (y1192 + 2066 * u);

            if (r < 0) r = 0; else if (r > 262143) r = 262143;
            if (g < 0) g = 0; else if (g > 262143) g = 262143;
            if (b < 0) b = 0; else if (b > 262143) b = 262143;

            rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
        }
    }

    jintArray result = (*env)->NewIntArray(env, frameSize);
    (*env)->SetIntArrayRegion(env, result, 0, frameSize, rgb);
    (*env)->ReleaseByteArrayElements(env, buf, yuv420sp, 0);
    return result;
}
