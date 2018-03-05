package com.msy.globalaccess.config;

import android.os.Environment;

import com.msy.globalaccess.BuildConfig;

import java.io.File;

/**
 * Created by hxy on 2017/2/13.
 * class description:
 */
public class DataSetting {
    //列表数据一次请求的数量
    public static final int LIST_DATA_SIZE = 10;
    /*选择景点列表和搜索景点列表一页数据量*/
    public static final int SPOT_DATA_SIZE = 50;

    public static  String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+ "data/cache";
    public static  String path = apkPath + File.separator+BuildConfig.ENVIRONMENT +File.separator;
}
