package xyz.kongjing.prerect.ndk;

/**
 * @author Jing
 * @Description
 * @Time 18/5/29
 */
public class ImageUtilEngine {
    static {
      System.load("imagelib");
    }

    public static native int[] decodeYUV420SP(byte[] buf, int width, int height);
}
