package cn.msy.zc.commonutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Pair;

import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hxy on 2016/5/18 0018. <p/> description :图片工具类(杰作~)
 */
public class ImageUtils {
    private static final String TAG = ImageUtils.class.getSimpleName();

    /**
     * 质量压缩法： 将bitmap压缩到指定的文件中,并且文件大小小于指定尺寸 <p/> 它不会减少图片的像素。它是在保持像素的前提下改变图片的位深及透明度等， 来达到压缩图片的目的。进过它压缩的图片文件大小会有改变，
     * 但是导入成bitmap后占得内存是不变的。因为要保持像素不变， 所以它就无法无限压缩，到达一个值之后就不会继续变小了。
     *
     * @param bmp         bitmap图片
     * @param fileAbsPath 文件的绝对路径
     * @param targetSize  限制的最大大小
     *
     * @throws IOException 文件名不存在报参数异常
     */
    public static boolean compressBitmap2DiscBelowTargetSize(Bitmap bmp, String fileAbsPath, int targetSize) throws
            IOException {
        if (TextUtils.isEmpty(fileAbsPath)) {
            throw new IllegalArgumentException("filePath 为空");
        }
        if (bmp == null) {
            throw new IllegalArgumentException("bitmap 为空");
        }
        File outFile = new File(fileAbsPath);
        if (outFile.exists()) {
            outFile.delete();
        }
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }

        return compressBmpToFileBelowTargetSize(bmp, outFile, targetSize);
    }

    /**
     * 质量压缩法： 将bitmap压缩到文件中,并限制文件的大小在指定范围内,该方法过度压缩会导致图片失真,并且不会改变bmp在内存中占有的大小
     *
     * @param bmp        bitmap 图
     * @param file       压缩到的文件
     * @param targetSize 文件最大大小
     *
     * @return 压缩结果
     * @throws IOException bmp为空报参数异常
     */
    public static boolean compressBmpToFileBelowTargetSize(Bitmap bmp, File file, int targetSize) throws IOException {
        if (bmp == null) {
            throw new IllegalArgumentException("bitmap is empty");
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ByteArrayOutputStream baos = compressQuality(bmp, targetSize);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
        } catch (Exception e) {
            Logger.e(TAG, "压缩失败", e);
            return false;
        } finally {
            try {
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 采样率压缩法
     *
     * @param srcPath     图片路径
     * @param limitWidth  期望的宽度
     * @param limitHeight 期望的高度
     *
     * @return 压缩后的图片
     * @throws IOException 图片地址无效报参数异常
     */
    public static Bitmap compressImageFromFile(String srcPath, int limitWidth, int limitHeight) throws IOException {
        if (TextUtils.isEmpty(srcPath)) {
            throw new IllegalArgumentException("srcPath 为空");
        }
        File f = new File(srcPath);
        if (!f.exists()) {
            throw new IllegalArgumentException("图片不存在:" + srcPath);
        }

        return BitmapFactory.decodeFile(srcPath, getLimitOption(srcPath, limitWidth, limitHeight));
    }

    /**
     * 获取本地图片
     *
     * @param path 本地图片路径
     *
     * @return bitmap
     */
    public static Bitmap getBitMapFromLocal(String path) throws IOException {
        //图片宽高不超过手机整个屏幕,防止内存溢出
        Bitmap bitmap = compressImageFromFile(path, DisplayUtil.getScreenWidth(), DisplayUtil.getScreenHeight());
        // 旋转图片
        int degree = readPicDegree(path);
        if (degree > 0) {
            bitmap = rotateBitmap(degree, bitmap);
        }
        return bitmap; // 压缩好比例大小后再进行质量压缩
    }

    /**
     * 将图片纠正到正确方向
     *
     * @param degree ： 图片被系统旋转的角度
     * @param bitmap ： 需纠正方向的图片
     *
     * @return 纠向后的图片
     */
    public static Bitmap rotateBitmap(int degree, Bitmap bitmap) throws IOException {
        if (bitmap == null) {
            throw new IllegalArgumentException("bitmap 为空");
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 根据图片路径获取图片的输入流,并且保持在限制大小内
     *
     * @param path      图片路径
     * @param limitSize 限制大小
     *
     * @return 输入流
     * @throws IOException 图片无效报参数异常
     */
    public static InputStream getInputStreamByImgPath(String path, int limitSize) throws IOException {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path 为空");
        }
        File f = new File(path);
        if (!f.exists()) {
            throw new IllegalArgumentException("file not exist");
        }

        if (limitSize <= 0) {
            Logger.w(TAG, "limitSize 小于0, 将不执行质量压缩");
        }

        Bitmap bitmap = getBitMapFromLocal(path);
        // 旋转图片

        long bitLength = f.length();
        if (bitLength <= limitSize * 1024) {
            return new ByteArrayInputStream(bitmap2Bytes(bitmap));
        }
        return compressImageToInputStream(bitmap, limitSize);
    }

    /**
     * 将本地文件压缩到指定的文件中,并且文件大小在指定的大小以内
     *
     * @param imgPath        图片源文件路径
     * @param limitSize      图片压缩的指定大小  -1不压缩
     * @param targetFilePath 压缩后文件存入的位置
     *
     * @return 如果压缩成功, 返回压缩的文件, 失败返回空
     * @throws IOException 图片源文件无效报参数异常
     */
    public static File compressImageFileByLimitSize(String imgPath, int limitSize, String targetFilePath, String
            fileName) throws IOException {
        File f = new File(targetFilePath, fileName);
        String absPath = f.getAbsolutePath();
        return compressImageFileByLimitSize(imgPath, limitSize, absPath);
    }

    public static File compressImageFileByLimitSize(String imgPath, int limitSize, String absPath) throws IOException {
        if (TextUtils.isEmpty(imgPath)) {
            throw new IllegalArgumentException("path 为空");
        }
        File f = new File(imgPath);
        if (!f.exists()) {
            throw new IllegalArgumentException("file not exist");
        }

        if (limitSize <= 0) {
            Logger.w(TAG, "limitSize 小于0, 将不执行质量压缩");
        }

        Bitmap bitmap = getBitMapFromLocal(imgPath);
        // 旋转图片
        //        int degree = readPicDegree(imgPath);
        //        if (degree > 0) {
        //            bitmap = rotateBitmap(degree, bitmap);
        //        }
        File targetFile = new File(absPath);
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        if (targetFile.exists()) {
            targetFile.delete();
        }
        boolean result = compressBmpToFileBelowTargetSize(bitmap, targetFile, limitSize);
        if (result) {
            return targetFile;
        } else {
            return null;
        }
    }

    /**
     * 将bitmap转为输入流,并且物理压缩到指定尺寸
     *
     * @param image     目标bitmap
     * @param limitSize 限制的大小,单位kb  -1不压缩
     *
     * @return 压缩后的输入流
     * @throws IOException 图片为空抛参数异常
     */
    public static InputStream compressImageToInputStream(Bitmap image, int limitSize) throws IOException {
        if (image == null) {
            throw new IllegalArgumentException("bitmap 为空");
        }
        ByteArrayOutputStream baos = compressQuality(image, limitSize);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * 根据图片有效路径获取图片的宽高信息
     *
     * @param imgPath 图片路径
     *
     * @return 图片宽高数组, index 0:宽度,index 1: 高度, 单位px,
     * @throws IOException 图片地址无效或图片不存在
     */
    public static int[] getImageOriginalInfo(String imgPath) throws IOException {
        ExifInterface exifInterface = getImgExifInfo(imgPath);
        int width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, ExifInterface.ORIENTATION_NORMAL);
        int height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, ExifInterface.ORIENTATION_NORMAL);
        // 获取图片的高度
        return new int[]{width, height};
    }

    /**
     * 根据图片有效路径获取图片的信息
     *
     * @param imgPath 图片路径
     *
     * @return 图片信息
     * @throws IOException 图片地址无效或图片不存在
     */
    public static ExifInterface getImgExifInfo(String imgPath) throws IOException {
        if (TextUtils.isEmpty(imgPath)) {
            throw new IllegalArgumentException("图片地址为空");
        }
        File f = new File(imgPath);
        if (!f.exists()) {
            throw new IllegalArgumentException("图片不存在");
        }
        return new ExifInterface(imgPath);
    }


    /**
     * 通过ExifInterface类读取图片文件的被旋转角度
     *
     * @param path ： 图片文件的路径
     *
     * @return 图片文件的被旋转角度
     */
    public static int readPicDegree(String path) throws IOException {
        int degree = 0;
        ExifInterface exif = getImgExifInfo(path);

        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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
        }

        return degree;
    }

    /**
     * drawable转bitmap
     *
     * @param drawable 目标drawable
     *
     * @return bitmap 转换后的bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) throws IOException {
        if (drawable == null) {
            throw new IllegalArgumentException("drawalbe 为空");
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    /**
     * bitmap转为byte[]
     *
     * @param bm 目标bitmap
     *
     * @return byte数组
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * byte转为bitmap
     *
     * @param b 目标byte
     *
     * @return bitmap
     */
    public static Bitmap bytes2Bimap(byte[] b) throws IOException {
        if (b == null || b.length == 0) {
            throw new IllegalArgumentException("byte 为空");
        }
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (Exception e) {
            Logger.e(TAG, "数组越界", e);
        }
        return bmp;
    }

    /**
     * 质量压缩核心算法
     *
     * @param bmp       目标bitmap
     * @param sizeLimit 限制大小  ,-1保持不变
     *
     * @return 进过压缩后的输出流
     */
    public static ByteArrayOutputStream compressQuality(Bitmap bmp, int sizeLimit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (sizeLimit > 0 && baos.toByteArray().length / 1024 > sizeLimit) {
            if (options <= 0) {
                Logger.e(TAG, "图片太大,无法压缩到 " + sizeLimit + " Kb 下,当前大小: " + baos.toByteArray().length / 1024);
                break;
            }
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);

        }
        return baos;
    }

    /**
     * 采样率压缩核心算法,获取限制后的采样比
     *
     * @param srcPath     图片路径
     * @param limitWidth  限制宽 -1为使用手机尺寸
     * @param limitHeight 限制高 -1为使用手机尺寸
     *
     * @return 限制后的采样比
     */
    public static int getSampleSize(String srcPath, int limitWidth, int limitHeight) throws IOException {
        if (TextUtils.isEmpty(srcPath)) {
            throw new IllegalArgumentException("srcPath 为空");
        }
        File f = new File(srcPath);
        if (!f.exists()) {
            throw new IllegalArgumentException("图片不存在");
        }
        if (limitWidth <= 0) {
            limitWidth = DisplayUtil.getScreenWidth();
        }
        if (limitHeight <= 0) {
            limitHeight = DisplayUtil.getScreenHeight();
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && w > limitWidth) {
            be = newOpts.outWidth / limitWidth;
        } else if (w < h && h > limitHeight) {
            be = newOpts.outHeight / limitHeight;
        }
        if (be <= 0)
            be = 1;
        return be;
    }

    /**
     * 获取满足限制条件内的option选项
     *
     * @param srcPath     图片路径
     * @param limitWidth  限制宽
     * @param limitHeight 限制高
     *
     * @return 满足限制条件内的option选项
     */
    public static BitmapFactory.Options getLimitOption(String srcPath, int limitWidth, int limitHeight) throws
            IOException {
        if (TextUtils.isEmpty(srcPath)) {
            throw new IllegalArgumentException("srcPath 为空");
        }
        File f = new File(srcPath);
        if (!f.exists()) {
            throw new IllegalArgumentException("图片不存在");
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inSampleSize = getSampleSize(srcPath, limitWidth, limitHeight);//设置采样率
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//当系统内存不够时候图片自动被回收
        return newOpts;
    }

    /**
     * 获取图片原生的option选项
     *
     * @param path 图片路径
     *
     * @return 图片的option
     */
    public static BitmapFactory.Options getDefaultOption(String path) throws IOException {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("srcPath 为空");
        }
        File f = new File(path);
        if (!f.exists()) {
            throw new IllegalArgumentException("图片不存在");
        }
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, newOpts);
        newOpts.inJustDecodeBounds = false;
        return newOpts;
    }

    /**
     * --以前的老方法,不明白什么意思 按比例显示图片，聊天
     *
     * @param picWidth  略
     * @param picheight 略
     * @param maxWidth  略
     * @param maxHeight 略
     * @param scale     略
     *
     * @return 略
     */
    public static Pair<Float, Float> getScaleImgWH(float picWidth, float picheight, int maxWidth, int maxHeight,
                                                   float scale) {
        if (picWidth != 0 && picheight != 0) {
            if (picWidth >= maxWidth && scale >= 1) {
                picWidth = maxWidth;
                picheight = (int) (picWidth / scale);
            } else if (picheight >= maxHeight && scale < 1) {
                picheight = maxHeight;
                picWidth = (int) (picheight * scale);
            }
        }
        return Pair.create(picWidth, picheight);
    }

    /**
     * 方法说明：保存文件到本地
     *
     * @param urlPath
     * @param fileName
     * @param savePath
     *
     * @return 图片路径
     */
    public static boolean saveUrlImg(String urlPath, String fileName, String savePath) {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        File imgFile = new File(urlPath);
        if (!urlPath.contains("http") && imgFile.exists()) {
            //            return FileUtils.copyFile(imgFile, new File(savePath + "/" + fileName));
            return true;
        }
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                OutputStream bos = new FileOutputStream(savePath + "/" + fileName);

                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = in.read(buff)) != -1) {
                    bos.write(buff, 0, len);
                }
                bos.flush();
                bos.close();
                in.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取sd卡路径
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //
        // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }
}
