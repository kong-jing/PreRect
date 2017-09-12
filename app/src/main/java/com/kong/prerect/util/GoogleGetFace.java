package com.kong.prerect.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.util.Log;

/**
 * Created by Jing on 16/6/16.
 * 使用谷歌的人脸识别来抠图
 */
public class GoogleGetFace {

    private static GoogleGetFace instance;

    public static GoogleGetFace getInstance(){
        if(instance == null){
            instance = new GoogleGetFace();
        }
        return instance;
    }

    private FaceDetector.Face[] mFace;
    private FaceDetector mFaceDetector;

    public Bitmap getFace(String imgPath){
        BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
        BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap myBitmap = BitmapFactory.decodeFile(imgPath,BitmapFactoryOptionsbfo);
        mFaceDetector = new FaceDetector(myBitmap.getWidth(),myBitmap.getHeight(),1);
        mFace = new FaceDetector.Face[1];
        int facesByFound = mFaceDetector.findFaces(myBitmap,mFace);
        PointF p = new PointF();
        int startX = 0,startY =0,width = 0,height = 0;
        if(mFace.length>0){
            mFace[0].getMidPoint(p);
            int faceX = (int) p.x;
            int faceY = (int) p.y;

            if(faceX <= myBitmap.getWidth()-faceX){
                startX = 0;
                width = faceX * 2;
            }else {
                startX = faceX - (myBitmap.getWidth()-faceX);
                width = (myBitmap.getWidth())*2;
            }
            if(faceY <= myBitmap.getHeight() - faceY){
                startY = 0;
                height = faceY * 2;
            }else {
                startY = faceY - (myBitmap.getHeight() - faceY);
                height = (myBitmap.getHeight() - faceY) * 2;
            }
        }

        Bitmap result = Bitmap.createBitmap(myBitmap,startX,startY,width,height);
        myBitmap.recycle();
        return result;
    }
    int imageWidth,imageHeight;
    private FaceDetector myFaceDetect;  //人脸识别类的实例
    private FaceDetector.Face[] myFace;
    int numberOfFaceDetected;
    public boolean findFace(Bitmap myBitmap){
        Bitmap formatBitmap = BitmapOperate.formatBitmap(myBitmap);
        formatBitmap = BitmapOperate.formatBitmapTo565(formatBitmap);
        Bitmap aimBitmap = formatBitmap.copy(Bitmap.Config.RGB_565, true);
        if (formatBitmap != aimBitmap) {
            formatBitmap.recycle();
        }
        // 创建一个人脸识别器
        FaceDetector faceDetector = new FaceDetector(aimBitmap.getWidth(),
                aimBitmap.getHeight(), 2);
        // 人脸数组
        FaceDetector.Face[] faces = new FaceDetector.Face[2];
        // Bitmap必须是565格式
        numberOfFaceDetected = faceDetector.findFaces(aimBitmap, faces);
        Log.e("TAG","numberOfFaceDetected"+numberOfFaceDetected);
        return numberOfFaceDetected > 0;
    }

    public Bitmap getAvatar(Bitmap bitmap){
        Bitmap myBitmap = formatBitmap(bitmap);
        myBitmap = formatBitmapTo565(myBitmap);
        Bitmap tarBitmap = myBitmap.copy(Bitmap.Config.RGB_565,true);
        if (myBitmap != tarBitmap){
            myBitmap.recycle();
        }
        FaceDetector faceDetector = new FaceDetector(tarBitmap.getWidth()
                                ,tarBitmap.getHeight(),1);
        FaceDetector.Face[] faces = new FaceDetector.Face[1];
        numberOfFaceDetected = faceDetector.findFaces(tarBitmap,faces);

        if (numberOfFaceDetected == 0){
            return tarBitmap;
        }
        PointF pointF = new PointF();
        faces[0].getMidPoint(pointF);
        int eyeLength = (int) faces[0].eyesDistance();
        int width = tarBitmap.getWidth();
        int height = tarBitmap.getHeight();



        return null;
    }

    /**
     * 格式化图片，使之能够被识别
     * @param bitmap
     * @return
     */
    public static Bitmap formatBitmap(Bitmap bitmap) {
        Bitmap aimBitmap = null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        boolean needChange = false;
        if (width % 2 == 1) {
            width++;
            needChange = true;
        }
        if (height % 2 == 1) {
            height++;
            needChange = true;
        }
        // 如果发生了改变，则做形变
        if (needChange) {
            aimBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            bitmap = null;
        } else {
            aimBitmap = bitmap;
        }
        return aimBitmap;
    }

    /**
     * 将图像转化为565格式
     * @param bitmap
     * @return
     */
    public static Bitmap formatBitmapTo565(Bitmap bitmap) {
        Bitmap aimBitmap = null;
        if (bitmap.getConfig() != Bitmap.Config.RGB_565) {
            aimBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.RGB_565);
            // 将创建的565格式作为画布，把bitmap重新画到565画布上
            Paint paint = new Paint();
            Canvas canvas = new Canvas(aimBitmap);
            paint.setColor(Color.BLACK);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            bitmap = null;
        }
        return aimBitmap;
    }

    /**
     * 显示一个人脸检测后的人脸框
     * @return
     */
    public float drawFace(Bitmap bitmap){
        if (bitmap != null) {
            Bitmap rgb_565Bitmap = bitmap.copy(Bitmap.Config.RGB_565, true);
            int mFaceWidth = rgb_565Bitmap.getWidth();
            int mFaceHeight = rgb_565Bitmap.getHeight();
            FaceDetector.Face[] faces = new FaceDetector.Face[5];
            FaceDetector fd = new FaceDetector(mFaceWidth, mFaceHeight, 5);
            int count = fd.findFaces(rgb_565Bitmap, faces);
            if (count > 0) {
                return faces[0].eyesDistance();
            }
        }
        return 0;
    }

    /**
     * onPreviewFrame 的data数据转bitmap
     * @param data
     * @param width
     * @param height
     * @return
     */
    public static Bitmap rawByteArray2RGBABitmap2(byte[] data, int width, int height) {
        int frameSize = width * height;
        int[] rgba = new int[frameSize];

        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++) {
                int y = (0xff & ((int) data[i * width + j]));
                int u = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 0]));
                int v = (0xff & ((int) data[frameSize + (i >> 1) * width + (j & ~1) + 1]));
                y = y < 16 ? 16 : y;

                int r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128));
                int g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128));
                int b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128));

                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);

                rgba[i * width + j] = 0xff000000 + (b << 16) + (g << 8) + r;
            }

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bmp.setPixels(rgba, 0 , width, 0, 0, width, height);
        return bmp;
    }

}
