package com.msy.globalaccess.utils.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.msy.globalaccess.R;

/**
 * Created by hxy on 2017/3/15 0015.
 * <p>
 * description : 用于帮助生成一些模板view
 */

public class ViewHelper {

    public static View getEmptyView(Context ctx,ViewGroup group){
        return LayoutInflater.from(ctx).inflate(R.layout.empty_view, group, false);
    }

    public static View getErrorView(Context ctx,ViewGroup group){
        return LayoutInflater.from(ctx).inflate(R.layout.error_view, group, false);
    }

    public static View getLoadingView(Context ctx,ViewGroup group){
        return LayoutInflater.from(ctx).inflate(R.layout.loading_view, group, false);
    }
}
