package xyz.kongjing.prerect.ndk;

/**
 * @author Jing
 * @Description use to handle the yuv2rgb
 * @Time 18/5/29
 */
public class ImageUtilEngine {

    static {
      System.loadLibrary("imagelib");
    }

    public static native int[] decodeYUV420SP(byte[] buf, int width, int height);
}
