package com.msy.globalaccess.common.enums;

/**
 * Created by hxy on 2017/2/8.
 * class description:
 */

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.msy.globalaccess.common.enums.RequestType.TYPE_LOAD_MORE;
import static com.msy.globalaccess.common.enums.RequestType.TYPE_LOCAL;
import static com.msy.globalaccess.common.enums.RequestType.TYPE_LATEST;
import static com.msy.globalaccess.common.enums.RequestType.TYPE_OTHER;

/**
 * 请求方式
 */
@IntDef({TYPE_LATEST, TYPE_LOAD_MORE, TYPE_OTHER, TYPE_LOCAL})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestType {
    int TYPE_LATEST = 1;//请求加载最新数据
    int TYPE_LOAD_MORE = 2;//请求加载更多数据
    int TYPE_OTHER = 3;//其他
    int TYPE_LOCAL = 4;//请求本地数据,如数据库
}
