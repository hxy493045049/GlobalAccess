package com.msy.globalaccess.di.module;

import android.text.TextUtils;
import android.util.SparseArray;

import com.msy.globalaccess.common.enums.EnvironmentType;
import com.msy.globalaccess.config.BaseUrlSetting;
import com.msy.globalaccess.config.CacheSetting;
import com.msy.globalaccess.config.NetworkSetting;
import com.msy.globalaccess.di.scope.PerApp;
import com.msy.globalaccess.utils.NetUtil;
import com.orhanobut.logger.Logger;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hxy on 2017/1/16 0016.
 * <p>
 * description : 提供retrifit依赖
 */
@Module
public class NetModule {
    private static SparseArray<Retrofit> sRetrofitManager = new SparseArray<>(BaseUrlSetting.TYPE_COUNT);
    /**
     * 云端响应头拦截器，用来配置缓存策略,需要注意: 我们的缓存策略只限于get请求
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtil.isNetworkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                Logger.e("no network,so request by cache");
            }

            Response originalResponse = chain.proceed(request);
            if (originalResponse.code() == 504 && NetUtil.isNetworkAvailable()) {
                //如果采用读取缓存的方式请求,当读取不到缓存时,并且有活动网络,那么重新发起网络请求
                Logger.e("504,无法读取到缓存,重新请求最新数据");
                //缓存不存在,先尝试读取缓存
                request = request.newBuilder().cacheControl(new CacheControl.Builder().maxStale(
                        (int) NetworkSetting.CACHE_STALE_SEC, TimeUnit.SECONDS).build()).build();
                originalResponse = chain.proceed(request);
            }

            // FIXME: 2017/3/27 0027
            //这里需要注意,按照正常逻辑来说,缓存时间是由服务器配置的header控制,但是我们后台没有做处理
            //所以统一假定缓存的stale为配置里的值，在这里进行统一的设置
            if (NetUtil.isNetworkAvailable()) {
                String cacheControl = request.cacheControl().toString();
                if (!TextUtils.isEmpty(cacheControl)) {
                    originalResponse = originalResponse.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build();
                }
                return originalResponse;
            } else {
                Logger.e("no network");
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + NetworkSetting
                                .CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };
    private final Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            //the request url
            String url = request.url().toString();
            //the request method
            String method = request.method();
            long t1 = System.nanoTime();

            Logger.i(String.format("Sending %s request [url = %s] %n [connection=%s] %n " +
                    "[headerInfo= %s]", method, request.url(), chain.connection(), request.headers()));
            //the request body
            RequestBody requestBody = request.body();
            StringBuilder RequestBodysb = new StringBuilder("Request Body [");
            if (requestBody != null) {
                okio.Buffer buffer = new okio.Buffer();
                requestBody.writeTo(buffer);
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                if (isPlaintext(buffer)) {
                    RequestBodysb.append(buffer.readString(charset));
                    RequestBodysb.append(" (Content-Type = ").append((contentType == null) ? "" :
                            contentType.toString()).append(",").append(requestBody.contentLength())
                            .append("-byte body)");
                } else {
                    RequestBodysb.append(" (Content-Type = ").append((contentType == null) ? "" :
                            contentType.toString()).append(",binary ").append(requestBody.contentLength())
                            .append("-byte body omitted)");
                }
            }
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            //the response time
            Logger.i(String.format(Locale.getDefault(), "Received response for [url = %s] in %.1f ms %n [headers = %s]"
                    , url, (t2 - t1) / 1e6d, response.headers().toString()));

            Logger.i(String.format(Locale.getDefault(), "Received response is %s ,message[%s],code[%d]", response
                    .isSuccessful() ? "success" : "fail", response.message(), response.code()));

            RequestBodysb.append("]");
            Logger.w(String.format("%s %s", method, RequestBodysb.toString()));
            //the response data
            ResponseBody body = response.body();

            BufferedSource source = body.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = Charset.defaultCharset();
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            String bodyString = buffer.clone().readString(charset);
            Logger.i(String.format("Received response json string [%s]", bodyString));

            return response;
        }
    };

    private final Interceptor DownloadProgressInterceptor=new Interceptor(){
        //// FIXME: 2017/5/15 0015   下载功能未完善
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            return originalResponse.newBuilder()
//                    .body(new DownloadProgressResponseBody(originalResponse.body(), listener))
                    .build();
        }
    };


    @EnvironmentType
    private int mEnvType;

    /**
     * 传入环境类型,会根据环境类型设置不同的baseurl
     *
     * @param env 环境类型
     */
    public NetModule(@EnvironmentType int env) {
        mEnvType = env;
    }

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    @Provides
    @PerApp
    OkHttpClient getOkHttpClient() {
        Cache cache = new Cache(new File(CacheSetting.CACHE_DIR, CacheSetting.CACHE_FILE_NAME),
                CacheSetting.HTTP_CACHE_SIZE);
        return new OkHttpClient.Builder().cache(cache)
                .connectTimeout(NetworkSetting.CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(NetworkSetting.READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(NetworkSetting.WRITE_TIME_OUT, TimeUnit.SECONDS)
                .addInterceptor(mRewriteCacheControlInterceptor)
                .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                .addInterceptor(mLoggingInterceptor).build();
    }

    @Provides
    @PerApp
    public Retrofit getRetrofit() {
        Retrofit retrofit = sRetrofitManager.get(mEnvType);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BaseUrlSetting.getBaseUrl(mEnvType))
                    .client(getOkHttpClient()).addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
            sRetrofitManager.put(mEnvType, retrofit);
        }
        return retrofit;
    }
}
