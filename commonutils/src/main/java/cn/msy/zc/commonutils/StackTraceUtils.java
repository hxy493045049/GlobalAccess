package cn.msy.zc.commonutils;

import android.content.Context;

import com.orhanobut.logger.Logger;

/**
 * Created by hxy on 2016/7/19 0019.
 * <p>
 * description :  java堆栈信息的utils
 */
public class StackTraceUtils {
    private static final String TAG = StackTraceUtils.class.getSimpleName();

    /**
     * 获取调用此方法的上一个类的类名和方法名,并输出到log中
     *
     * @param ctx 上下文环境
     * @throws Exception 当上下文为空时,抛出参数异常
     */
    public static boolean checkContext(Context ctx) {
        if (ctx == null) {
            StackTraceElement[] ele = new Throwable().getStackTrace();
            String methodName = ele[1].getMethodName();
            String className = ele[1].getClassName();
            Logger.d(TAG, "检验到传递的上下文环境为空,调用者--> className :" + className + ", methodName: " + methodName + "; ctx 上下文为空");
            return false;
        }
        return true;
    }

    /**
     * 输出上一级调用者信息
     */
    public static void printCaller() {
        StackTraceElement[] ele = new Throwable().getStackTrace();
        String methodName = ele[1].getMethodName();
        String className = ele[1].getClassName();
        Logger.d(TAG, "打印调用者--> className: " + className + ", methodName: " + methodName);
    }
}
