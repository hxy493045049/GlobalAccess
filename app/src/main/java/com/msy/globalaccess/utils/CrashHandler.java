package com.msy.globalaccess.utils;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

/**
 * 全局异常捕获
 * Created by chensh
 * <p/>
 * description : 原文地址  http://blog.csdn.net/singwhatiwanna/article/details/17289479
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    //    private static final boolean DEBUG = true;

    //    private static final String FILE_NAME = "crash";

    //log文件的后缀名
    //    private static final String FILE_NAME_SUFFIX = ".trace";

    private static CrashHandler sInstance = new CrashHandler();

    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;

    //构造方法私有，防止外部构造多个实例，即采用单例模式
    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    //这里主要完成初始化工作
    public void init(Context context) {
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        //获取Context，方便内部使用
        mContext = context.getApplicationContext();
    }

    /**
     * 这个是最关键的函数，当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法 thread为出现未捕获异常的线程，ex为未捕获的异常，有了这个ex，我们就可以得到异常信息。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        //        try {
        //            //导出异常信息到SD卡中
        //            dumpExceptionToSDCard(ex);
        //            //这里可以通过网络上传异常信息到服务器，便于开发人员分析日志从而解决bug
        //            uploadExceptionToServer();
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //        }

        //打印出当前调用栈信息
        Logger.e(ex, TAG+"未捕获到的异常: \r\n");
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.reportError(mContext, ex);

        //        ArrayList<Activity> allActivity = App.getAllActivity();
        //        if (allActivity != null && allActivity.size() > 0) {
        //            for (Activity activity : allActivity) {
        //                if (activity != null) {
        //                    activity.finish();
        //                }
        //            }
        //        }

        //如果原来已经有默认的处理方式,再交给它处理
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(thread, ex);
        }

        //强制关闭app
        //        android.os.Process.killProcess(android.os.Process.myPid());
    }


    //    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
    //        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
    //        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    //            if (DEBUG) {
    //                Logger.w(TAG, "sdcard unmounted,skip dump exception");
    //                return;
    //            }
    //        }
    //
    //        File dir = DirManager.callActivity().getDirByTarget(DirManager.CRASH_LOG_DIR);
    //        if (dir == null) {
    //            dir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "msyCache" +
    // File
    //                    .separator + "crachLog" );
    //        }
    //        if (!dir.exists()) {
    //            dir.mkdirs();
    //        }
    //        long current = System.currentTimeMillis();
    //        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
    //        //以当前时间创建log文件
    //        File file = new File(dir.getAbsolutePath()+ File.separator + FILE_NAME + time + FILE_NAME_SUFFIX);
    //
    //        try {
    //            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
    //            //导出发生异常的时间
    //            pw.println(time);
    //
    //            //导出手机信息
    //            dumpPhoneInfo(pw);
    //
    //            pw.println();
    //            //导出异常的调用栈信息
    //            ex.printStackTrace(pw);
    //
    //            pw.close();
    //        } catch (Exception e) {
    //            Logger.e(TAG, "dump crash info failed", e);
    //        }
    //    }
    //
    //    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
    //        //应用的版本名称和版本号
    //        PackageManager pm = mContext.getPackageManager();
    //        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
    //        pw.print("App Version: ");
    //        pw.print(pi.versionName);
    //        pw.print('_');
    //        pw.println(pi.versionCode);
    //
    //        //android版本号
    //        pw.print("OS Version: ");
    //        pw.print(Build.VERSION.RELEASE);
    //        pw.print("_");
    //        pw.println(Build.VERSION.SDK_INT);
    //
    //        //手机制造商
    //        pw.print("Vendor: ");
    //        pw.println(Build.MANUFACTURER);
    //
    //        //手机型号
    //        pw.print("Model: ");
    //        pw.println(Build.MODEL);
    //
    //        //cpu架构
    //        pw.print("CPU ABI: ");
    //        pw.println(Build.CPU_ABI);
    //    }
    //
    //    private void uploadExceptionToServer() {
    //        //TODO Upload Exception Message To Your Web Server
    //    }

}
