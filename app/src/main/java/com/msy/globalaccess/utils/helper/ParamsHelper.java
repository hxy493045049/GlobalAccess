package com.msy.globalaccess.utils.helper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.msy.globalaccess.common.Constants;
import com.msy.globalaccess.config.ServiceSetting;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import cn.msy.zc.commonutils.StringToMD5;

/**
 * Created by shawn on 2017/2/10.
 * description:
 */
public class ParamsHelper {
    private HashMap<String, String> mDefaultParam;
    private static boolean mHasVersion = true;
    private ParamsHelper() {
        mDefaultParam = new HashMap<>();
        mDefaultParam.put("uname", Constants.UNAME);
        if(mHasVersion){
            mDefaultParam.put("version", ServiceSetting.SERVICE_VERSION);
//            mDefaultParam.put("version", ApplicationUtils.getVersion()[0]);
        }
    }

    public HashMap<String, String> getParam() {
        return mDefaultParam;
    }

    // TODO: 2017/2/11 这里可以添加其他操作参数的方式,如排序,去重复等,请自行补充

    public static class Builder {
        private HashMap<String, Object> mParams;
        private Gson gson;
        private String methodValue;

        public Builder() {
            mParams = new HashMap<>();
            gson = new Gson();
        }

        /**
         * 设置api的固定字段method的值
         */
        public Builder setMethod(String methodValue) {
            this.methodValue = methodValue;
            return this;
        }

        public Builder setParam(String key, Object value) {
            this.mParams.put(key, value);
            return this;
        }

        public Builder setParams(HashMap<String, Object> params) {
            this.mParams.putAll(params);
            return this;
        }

        public ParamsHelper build() {
            mHasVersion = true;
            return buildSign();
        }
        public ParamsHelper build(boolean hasVersion) {
            mHasVersion = hasVersion;
            return buildSign();
        }

        public ParamsHelper buildSign(){
            ParamsHelper helper = new ParamsHelper();
            if (!TextUtils.isEmpty(methodValue)) {
                helper.mDefaultParam.put("method", methodValue);
            } else {
                if (!helper.mDefaultParam.containsKey("method")) {
                    Logger.e("参数缺少method字段");
                }
            }
            for (Map.Entry<String, Object> stringVEntry : mParams.entrySet()) {
                String key = stringVEntry.getKey();
                Object val = stringVEntry.getValue();
                if (val instanceof String) {
                    helper.mDefaultParam.put(key, (String) val);
                } else {
                    helper.mDefaultParam.put(key, gson.toJson(val));
                }
            }
            try {
                //生成签名
                String sign = StringToMD5.getSignature(helper.mDefaultParam);
                helper.mDefaultParam.put("sign", sign);
            } catch (Exception e) {
                Logger.e(e, "签名参数生成失败");
            }
            return helper;
        }
    }

}
