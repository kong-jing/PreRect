package xyz.kongjing.prerect.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ImgUtil {
    /**
     * @param b
     * @param rotateDegree
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) rotateDegree);
        Bitmap rotaBitmap =
            Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }

    //****************解决图片加载内存溢出问题
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
        int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth,
        int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromPath(String filePath, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeFile(filePath, options);
    }
    //****************解决图片加载内存溢出问题

    /**
     * 缩小图片(精准缩放w/h为指定值)避免出现内存溢出
     */
    public static Bitmap zoomPic(String imgPath, int w, int h) {
        Bitmap bitmap = decodeSampledBitmapFromPath(imgPath, w, h);

        return zoomBitmap(bitmap, w, h);
    }

    /**
     * 缩小图片(精准缩放w/h为指定值)
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);///940/1922
            float scaleHeight = ((float) h / height);
            if (scaleWidht > scaleHeight) {
                matrix.postScale(scaleHeight, scaleHeight);
            } else {
                matrix.postScale(scaleWidht, scaleWidht);
            }
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        }
        return newbmp;
    }

    /**
     * 图片去色,返回灰度图片
     *
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    /**
     * 计算缩放图片的宽高
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size) return img_size;
        double ratio = square_size / (double) Math.max(img_size[0], img_size[1]);
        return new int[] {
            (int) (img_size[0] * ratio), (int) (img_size[1] * ratio)
        };
    }

    /**
     * 创建缩略图
     *
     * @param largeImagePath 原始大图路径
     * @param square_size 输出图片宽度
     * @throws IOException
     */
    public static Bitmap createImageThumbnail(Context context, String largeImagePath,
        int square_size) throws IOException {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 1;
        // 原始图片bitmap
        Bitmap cur_bitmap = getBitmapByPath(largeImagePath, opts);

        if (cur_bitmap == null) return null;
        // 原始图片的高宽
        int[] cur_img_size = new int[] {
            cur_bitmap.getWidth(), cur_bitmap.getHeight()
        };

        // 计算原始图片缩放后的宽高
        int[] new_img_size = scaleImageSize(cur_img_size, square_size);

        if (new_img_size[0] > square_size) {
            // 生成缩放后的bitmap
            return zoomBitmap(cur_bitmap, new_img_size[0], new_img_size[1]);
        } else {
            return cur_bitmap;
        }
    }

    /**
     * 保存图片为PNG
     */
    public static void savePNG_After(Bitmap bitmap, String path, int quality) {
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, quality, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap jpgByteArrayToBitmap(byte[] data) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        return bitmap;
    }

    /**
     * 保存图片为JPEG
     */
    public static void saveJPGE_After(Bitmap bitmap, String path, int quality) {
        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drawable 转 Bitmap
     */
    public static Bitmap drawableToBitmapByBD(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    /**
     * Bitmap 转 Drawable
     */
    public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    /**
     * byte[] 转 bitmap
     */
    public static Bitmap bytesToBimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * bitmap 转 byte[]
     *
     * @param format {@link Bitmap.CompressFormat }
     */
    public static byte[] bitmapToByte(Bitmap b, Bitmap.CompressFormat format) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(format, 100, o);
        return o.toByteArray();
    }

    /**
     * 让Gallery上能马上看到该图片
     */
    private static void scanPhoto(Context ctx, String imgFileName) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取bitmap
     */
    public static Bitmap getBitmapByFile(File file) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * 获取bitmap
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return getBitmapByPath(filePath, null);
    }

    /**
     * 使用当前时间戳拼接一个唯一的文件名
     */
    public static String getTempFileName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
        String fileName = format.format(new Timestamp(System.currentTimeMillis()));
        return fileName;
    }

    /**
     * 获取bitmap
     */
    public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    private static String getPathDeprecated(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    /**
     * 通过url获取文件路径
     */
    public static String getSmartFilePath(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getPathDeprecated(context, uri);
        }
        return getPath(context, uri);
    }

    /**
     * getImagePath:获取图片路径. <br/>
     *
     * @author:284891377 Date: 2016-4-14 下午6:16:28
     * @since JDK 1.7
     */
    //	public static String getImagePath(Uri uri, Activity context){
    //			File photoFile = null;
    //			String saveDir=Util.getDiskCacheDir(context);
    //			 try {
    //			 InputStream pictureInputStream = context.getContentResolver().openInputStream(uri);
    //
    //		         photoFile = new File(saveDir, "temppic.jpg");
    //		        photoFile.createNewFile();
    //
    //		        OutputStream out = new FileOutputStream(photoFile);
    //	            byte[] buf = new byte[1024];
    //	            int len;
    //	            while ((len = pictureInputStream.read(buf)) > 0) {
    //	                out.write(buf, 0, len);
    //	            }
    //	            out.close();
    //	            pictureInputStream.close();
    //
    //
    //
    //
    //		        } catch (Exception e) {
    //		            e.printStackTrace();
    //		        }
    //			 if(photoFile==null){
    //				 return "";
    //			 }else{
    //				 return photoFile.getAbsolutePath();
    //			 }
    //
    //
    //		 }
    @SuppressLint("NewApi") public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri =
                    ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                    split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
            column
        };

        try {
            cursor =
                context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String md5(String paramString) {
        String returnStr;
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            returnStr = byteToHexString(localMessageDigest.digest());
            return returnStr;
        } catch (Exception e) {
            return paramString;
        }
    }

    public static void startImgIntent(Activity act, int requestCode) {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            act.startActivityForResult(Intent.createChooser(intent, "选择图片"), requestCode);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            act.startActivityForResult(Intent.createChooser(intent, "选择图片"), requestCode);
        }
    }

    /**
     * 将指定byte数组转换成16进制字符串
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    //************图片角度

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        // 创建新的图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap resizedBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        options.inJustDecodeBounds = false;
        return resizedBitmap;
    }
    /************系统相册显示*****************/
    /**
     * insertImage:保存图片到系统图库. <br/>
     * 文件路径为sdcard/DICM/Camera.<br/>
     * 注意:文件名不可以自己指定,系统随机生成.<br/>
     *
     * @param source Bitmap
     * @return image url
     * @author:284891377 Date: 2016-5-10 上午10:37:49
     * @since JDK 1.7
     */
    public static final String saveImageToGallery(Context ctx, Bitmap source, String title,
        String description) {
        ContentResolver cr = ctx.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // Add the date meta data to ensure the image is added at the front of
        // the gallery
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;
        String stringUrl = null; /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr, id,
                    MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(cr, miniThumb, id, 50F, 50F,
                    MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the
     * insertImage to populate the
     * android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated
     * here.
     *
     * @see MediaStore.Images.Media (StoreThumbnail private
     * method)
     */
    private static final Bitmap storeThumbnail(ContentResolver cr, Bitmap source, long id,
        float width, float height, int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb =
            Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND, kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID, (int) id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * saveImageToGallery:保存图片到sd卡指定目录,并在系统相册显示. <br/>
     * 注意：图片名称和路径可以自己指定 <br/>
     *
     * @param dirPath sd卡文件夹路径(dirName必须自己创建)
     * @author:284891377 Date: 2016-5-10 上午10:46:07
     * @since JDK 1.7
     */
    public static void saveImageToGallery(Context context, Bitmap bmp, String dirPath) {

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
            Uri.parse("file://" + file.getAbsolutePath())));
    }/************系统相册显示*****************/
    /**
     * byte转为file文件
     */
    public static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void YUV_NV21_TO_RGB(int[] argb, byte[] yuv, int width, int height) {
        int frameSize = width * height;
        boolean ii = false;
        boolean ij = false;
        boolean di = true;
        boolean dj = true;
        int a = 0;
        int i = 0;

        for (int ci = 0; i < height; ++ci) {
            int j = 0;

            for (int cj = 0; j < width; ++cj) {
                int y = 255 & yuv[ci * width + cj];
                int u = 255 & yuv[frameSize + (ci >> 1) * width + (cj & -1) + 0];
                int v =
                    255 & yuv[frameSize + (ci >> 1) * width + (ci >> 2) * width + (cj & -1) + 0];
                y = y < 16 ? 16 : y;
                int r = (int) (1.164F * (float) (y - 16) + 1.596F * (float) (v - 128));
                int g = (int) (1.164F * (float) (y - 16)
                    - 0.813F * (float) (v - 128)
                    - 0.391F * (float) (u - 128));
                int b = (int) (1.164F * (float) (y - 16) + 2.018F * (float) (u - 128));
                r = r < 0 ? 0 : (r > 255 ? 255 : r);
                g = g < 0 ? 0 : (g > 255 ? 255 : g);
                b = b < 0 ? 0 : (b > 255 ? 255 : b);
                argb[a++] = -16777216 | r << 16 | g << 8 | b;
                ++j;
            }

            ++i;
        }
    }

    public static Bitmap yuv420p2bmp(byte[] data, int width, int height) {
        int[] rgba = new int[width * height];
        YUV_NV21_TO_RGB(rgba, data, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(rgba, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static int[] convertYUV420_NV21toRGB8888(byte[] data, int width, int height) {
        int p;
        int size = width * height;
        int[] pixels = new int[size];
        for (int i = 0; i < size; i++) {
            p = data[i] & 0xFF;
            pixels[i] = 0xff000000 | p << 16 | p << 8 | p;
        }

        return pixels;
    }

    /**
     * yuv转换图像
     */
    public static Bitmap yuv2Img(byte[] frameData, int yuvFormat, int prevWidth, int prevHeight,
        int quality) {
        Long start = System.currentTimeMillis();
        Bitmap img = null;
        try {
            YuvImage image = new YuvImage(frameData, yuvFormat, prevWidth, prevHeight, null);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            if (image != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compressToJpeg(new Rect(0, 0, prevWidth, prevHeight), quality, stream);
                img =
                    BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size(), options);
                stream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    public static boolean saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    /**
     * 转化 onPreview中的视频帧为bitmap
     *
     * @param data
     * @param camera
     */


    public static Bitmap runInPreviewFrame(byte[] data, Camera camera) {
        ByteArrayOutputStream baos;
        byte[] rawImage;
        Bitmap bitmap;
        camera.setOneShotPreviewCallback(null);
        //处理data
        Camera.Size previewSize = camera.getParameters().getPreviewSize();//获取尺寸,格式转换的时候要用到
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage =
            new YuvImage(data, ImageFormat.NV21, previewSize.width, previewSize.height, null);
        baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0, previewSize.width, previewSize.height), 100,
            baos);// 80--JPG图片的质量[0-100],100最高
        rawImage = baos.toByteArray();
        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length, options);
        return bitmap;
    }
}

