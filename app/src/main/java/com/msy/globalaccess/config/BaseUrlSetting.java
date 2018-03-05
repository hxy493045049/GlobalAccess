/*
 * Copyright (c) 2016 shawn <shawn0729@foxmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.msy.globalaccess.config;


import com.msy.globalaccess.common.enums.EnvironmentType;
import com.orhanobut.logger.Logger;


import static com.msy.globalaccess.common.enums.EnvironmentType.ENV_DEBUG;
import static com.msy.globalaccess.common.enums.EnvironmentType.ENV_INTEGRATION;
import static com.msy.globalaccess.common.enums.EnvironmentType.ENV_RELEASE;

/**
 * @author shawn
 * @version 1.0 2017/1/16
 */
public class BaseUrlSetting {

    /**
     * 修改retrofit的baseurl不太方便,所以针对不同baseurl创建不同的实例;
     * 这里的count=3个内部环境(开发,集成,正式)+1个外部环境(外链)
     */
    public static final int TYPE_COUNT = 4;

    //测试环境
    public static final String DEBUG_BASE_URL = "http://www.wootide.net/globalTourism/";
    //全域宝
    public static final String INTEGRATION_BASE_URL = "http://test1.wootide.net/globalTourism/";
    //全域通
    public static final String RELEASE_BASE_URL = "http://110.53.51.220:8188/zjjqyt/";

    //-------------------外链写在下面-----------------------
    // TODO: 2017/2/6   外链修改这里
    public static final String EXTERNAL_BASE_URL = "https://zc.msy.cn";

    //----------------------------------------------------

    /**
     * 获取对应的host
     *
     * @param envType host类型
     * @return host
     */
    public static String getBaseUrl(@EnvironmentType int envType) {
        String host;
        switch (envType) {
            case ENV_DEBUG:
                host = DEBUG_BASE_URL;
                break;
            case ENV_INTEGRATION:
                host = INTEGRATION_BASE_URL;
                break;
            case ENV_RELEASE:
                host = RELEASE_BASE_URL;
                break;
            default:
                Logger.d("其他环境");
                host = EXTERNAL_BASE_URL;
                break;
        }
        return host;
    }
}
