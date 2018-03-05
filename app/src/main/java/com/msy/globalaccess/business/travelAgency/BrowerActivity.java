package com.msy.globalaccess.business.travelAgency;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.msy.globalaccess.BuildConfig;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.config.BaseUrlSetting;
import com.msy.globalaccess.utils.ToastUtils;

import butterknife.BindView;
import cn.msy.zc.commonutils.StringUtils;

/**
 * H5
 * Created by chensh on 2017/3/22 0022.
 */

public class BrowerActivity extends BaseActivity {
    private final static String POSTDATA = "postData";
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.tvToolbarCenter)
    AppCompatTextView AppCompatTextView;
    /**
     * 域名
     */
    private String host;
    /**
     * param(key=value)
     */
    private byte[] postData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_brower;
    }

    @Override
    protected void initInjector() {
    }

    /**
     * 添加网页连接
     *
     * @param context  上下文
     * @param postData param(key=value)
     */
    public static void callactivity(Context context, String postData) {
        Intent intent = new Intent(context, BrowerActivity.class);
        intent.putExtra(POSTDATA, postData);
        context.startActivity(intent);
    }

    @Override
    protected void init() {
        environment();
        if (getIntent().hasExtra(POSTDATA)) {
            String data = getIntent().getStringExtra(POSTDATA);
            if (!StringUtils.isEmpty(data)) {
                postData = data.getBytes();
            } else {
                ToastUtils.showToast("网页不可用");
                return;
            }
        } else {
            ToastUtils.showToast("暂无网页连接");
            return;
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");
        webView.clearCache(true);
        webView.postUrl(host, postData);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                AppCompatTextView.setVisibility(View.VISIBLE);
                AppCompatTextView.setText(title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                webView.loadUrl(url);
                return true;
            }

        });
        webView.getSettings().setSavePassword(false);
    }

    /**
     * 获取当前域名
     */
    private void environment() {
        if (BuildConfig.ENVIRONMENT.equals("debug_test")) {
            host = BaseUrlSetting.DEBUG_BASE_URL + "appInterface.do";
        } else if (BuildConfig.ENVIRONMENT.equals("qyb")) {
            host = BaseUrlSetting.INTEGRATION_BASE_URL + "appInterface.do";
        } else if (BuildConfig.ENVIRONMENT.equals("qyt")) {
            host = BaseUrlSetting.RELEASE_BASE_URL + "appInterface.do";
        }
    }
}
