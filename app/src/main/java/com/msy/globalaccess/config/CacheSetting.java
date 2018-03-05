package com.msy.globalaccess.config;

import com.msy.globalaccess.base.App;

import java.io.File;

/**
 * Created by hxy on 2017/1/17 0017.
 * <p>
 * description :
 */

public class CacheSetting {

    public static final File CACHE_DIR= App.getAppContext().getCacheDir();
    //缓存文件名称,改文件放置在{@link App.getAppContext().getCacheDir()}下
    public static final String CACHE_FILE_NAME = "globalAccessCache";

    //HTTP缓存文件大小,改文件不包含图片
    public static final long HTTP_CACHE_SIZE = 1024 * 1024 * 100;
}
