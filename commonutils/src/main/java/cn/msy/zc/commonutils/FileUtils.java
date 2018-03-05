package cn.msy.zc.commonutils;


import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by hxy on 2016/5/18 0018. <p> description :
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();


    // 判断文件是否存在
    public static boolean isExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    // 创建目录
    public static File createDir(String path) throws IOException {
        try {
            File dir = new File(path);
            dir.mkdirs();
            return dir;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除目录中的所有文件
     *
     * @param dir          要删除的目录
     * @param isDeleteSelf 是否删除自己
     * @return true 删除成功,反之false
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean deleteDir(File dir, boolean isDeleteSelf) {
        if (!dir.exists()) {
            Logger.e(TAG, "file is not exist");
            return false;
        }
        if (!dir.isDirectory()) {
            Logger.e(TAG, "file is not a directory");
            return false;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            Logger.w(TAG, "the dir is empty:" + dir.getAbsolutePath());
            return true;
        }
        for (File f : files) {
            if (!f.exists()) {
                continue;
            }
            if (f.isDirectory()) {
                deleteDir(f, true);
            }
            f.delete();
        }
        if (isDeleteSelf) {
            dir.delete();
        }
        return true;
    }

    /**
     * 移动文件
     *
     * @param from 文件来源
     * @param to   移动到那个文件
     * @return 成功true, 失败false
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean moveFile(File from, File to) {
        if (copyFile(from, to)) {
            from.delete();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 复制文件
     *
     * @param from 从哪复制
     * @param to   复制到那个文件
     * @return 成功true, 失败false
     */
    public static boolean copyFile(File from, File to) {
        try {
            FileOutputStream fos = new FileOutputStream(to);
            FileInputStream fis = new FileInputStream(from);
            byte[] bytes = new byte[1024];
            while (fis.read(bytes) != -1) {
                fos.write(bytes);
            }
            fis.close();
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            Logger.e(TAG, "", e);
            return false;
        }

    }

    /**
     * 复制文件(以较快速度复制文件)
     *
     * @param srcFile 源文件File
     * @param destDir 目标目录File
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回false
     */
    public static boolean fastCopyFile(File srcFile, File destDir) {
        boolean copySizes = true;
        try {
            if (!srcFile.exists()) {
                System.out.println("源文件不存在");
                copySizes = false;
            } else if (!destDir.exists()) {
                System.out.println("目标目录不存在");
                copySizes = false;
            } else {
                FileChannel fcin = new FileInputStream(srcFile).getChannel();
                FileChannel fcout = new FileOutputStream(destDir).getChannel();
                fcin.transferTo(0, fcin.size(), fcout);
                fcin.close();
                fcout.close();
                copySizes = true;
            }
        } catch (Exception e) {
            Logger.e(TAG, "", e);
            copySizes = false;
        }
        return copySizes;
    }

    /**
     * 复制文件
     *
     * @return 复制成功, true, 失败false
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean copy(String response, String path, String fileName) {
        InputStream in;
        try {
            in = new ByteArrayInputStream(response.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        File f = new File(path);
        //创建文件夹
        if (!f.exists()) {
            f.mkdirs();
        }
        //创建文件
        File file = new File(f, fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            OutputStream out = new FileOutputStream(file);
            return copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 复制数据
     *
     * @param in  源数据
     * @param out 目标数据
     * @return 复制成功true, 失败false
     */
    public static boolean copy(InputStream in, OutputStream out) {
        if (in == null || out == null) {
            return false;
        }
        try {
            byte[] b = new byte[1024 * 4];
            while (in.read(b) != -1) {
                out.write(b);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Logger.e(TAG, "", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Logger.e(TAG, "", e);
                }
            }
        }
        return false;
    }

    public static String readFileSdcardFile(String fileName) {
        File file = new File(fileName);
        long currentTime = System.currentTimeMillis();
        StringBuilder res = new StringBuilder();
        if (file.exists() && currentTime - file.lastModified() < (1000 * 60 * 120)) {

            try {
                FileInputStream fin = new FileInputStream(fileName);
                byte[] buffer = new byte[4 * 1024];
                while (fin.read(buffer) != -1) {
                    res.append(new String(buffer, "UTF-8"));
                }
                fin.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res.toString();
    }

    /**
     * 获取文件大小(会阻塞线程).单位byte
     *
     * @param dir 要遍历的文件地址
     * @return 文件大小
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return dir.length();
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 删除文件夹下指定的名称的文件
     * @param path
     * @throws IOException
     */
    public static void deleteFolder(String path) throws IOException {
        File dirFile = new File(path);
        if(dirFile.exists()){
            dirFile.getAbsoluteFile().delete();
        }
    }

    /**
     * 使用多线程遍历文件大小,单位byte,有问题
     */
    static class FileSizeComputer {
        final private BlockingQueue<Long> fileSizes = new ArrayBlockingQueue<>(500);
        private AtomicInteger ato = new AtomicInteger();
        private ExecutorService service;

        /**
         * 多线程浏览文件
         *
         * @param file 文件目录
         */
        private void startExploreDir(final File file) {
            ato.incrementAndGet();
            try {
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        exploreDir(file);
                    }
                });
            } catch (Exception e) {
                Logger.e(TAG, "", e);
            }
        }

        /**
         * 浏览文件,文件的运行过程
         *
         * @param file 文件目录
         */
        private void exploreDir(File file) {
            long fileSize = 0;
            if (file.isFile()) {
                fileSize = file.length();
            } else {
                File[] children = file.listFiles();
                if (children != null) {
                    for (final File child : children) {
                        if (child.isFile()) {
                            fileSize += child.length();
                        } else {
                            startExploreDir(child);
                        }
                    }
                }
            }
            try {
                // 把fileSize加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续
                fileSizes.put(fileSize);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        private String getTotalSizeOfFile(String fileName) throws InterruptedException {
            return getTotalSizeOfFile(new File(fileName));
        }

        private String getTotalSizeOfFile(File f) throws InterruptedException {
            service = Executors.newFixedThreadPool(10);
            try {
                startExploreDir(f);
                long totalSize = 0;
                while (fileSizes.size() > 0) {
                    // 从BlockingQueue取出一个队首的对象,如果在指定时间内,队列一旦有数据可取,则立即返回队列中的数据
                    //否则知道时间超时还没有数据可取,返回失败。
                    final long size = fileSizes.take();
                    totalSize += size;
                    ato.decrementAndGet();
                }
                return FormatUtils.formatFileSize(totalSize);
            } finally {
                service.shutdown();
            }
        }
    }


}
