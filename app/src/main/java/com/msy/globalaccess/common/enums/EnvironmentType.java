package com.msy.globalaccess.common.enums;

/**
 * Created by hxy on 2017/2/8.
 * class description: 开发环境
 */

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.msy.globalaccess.common.enums.EnvironmentType.ENV_DEBUG;
import static com.msy.globalaccess.common.enums.EnvironmentType.ENV_EXTERNAL;
import static com.msy.globalaccess.common.enums.EnvironmentType.ENV_INTEGRATION;
import static com.msy.globalaccess.common.enums.EnvironmentType.ENV_RELEASE;

/**
 * 性能优化,正常枚举会消耗2倍的内存
 * 替代枚举的方案，使用IntDef保证类型安全
 */
@IntDef({ENV_DEBUG, ENV_INTEGRATION, ENV_RELEASE, ENV_EXTERNAL})
@Retention(RetentionPolicy.SOURCE)
public @interface EnvironmentType {
    //开发环境
    int ENV_DEBUG = 1;
    //集成环境
    int ENV_INTEGRATION = 2;
    //正式环境
    int ENV_RELEASE = 3;
    //外链的类型
    int ENV_EXTERNAL = 4;
}
