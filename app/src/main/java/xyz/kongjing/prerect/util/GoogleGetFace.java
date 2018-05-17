package xyz.kongjing.prerect.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;

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
}
