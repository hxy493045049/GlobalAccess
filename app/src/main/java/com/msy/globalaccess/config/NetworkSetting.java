package com.msy.globalaccess.config;

/**
 * Created by hxy on 2017/1/16 0016.
 * <p>
 * description : 网络配置
 */

public class NetworkSetting {
    //设缓存stale时长为两天
    public static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    //设缓存age时长为0,表示不用换乘
    public static final long CACHE_AGE_SEC = 0;
    //连接超时
    public static final int CONNECT_TIME_OUT = 30;
    //读取超时
    public static final int READ_TIME_OUT = 30;
    //写入超时
    public static final int WRITE_TIME_OUT = 30;




    //--------------以下信息配置到请求的头部,在basepresenterimpl中调用-----------------
    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    public static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;

    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    public static final String CACHE_CONTROL_AGE = "public, max-age=" + CACHE_AGE_SEC;
}
