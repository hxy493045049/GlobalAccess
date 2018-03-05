package com.msy.globalaccess.utils.helper;

/**
 * Created by hxy on 2017/1/18 0018.
 * <p>
 * description :
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.msy.globalaccess.BuildConfig;
import com.msy.globalaccess.R;

import java.io.IOException;
import java.io.InputStream;

import cn.msy.zc.commonutils.glideconfig.GlideCircleTransform;
import cn.msy.zc.commonutils.glideconfig.GlideRoundTransform;

public class GlideHelper {
    /**
     * 正常
     */
    public static final int Glide_Imgae_Normal = 0;
    /**
     * 圆形
     */
    public static final int Glide_Imgae_Circle = 1;
    /**
     * 圆角
     */
    public static final int Glide_Imgae_Round = 2;
    private static final StreamModelLoader<String> cacheOnlyStreamLoader = new StreamModelLoader<String>() {
        @Override
        public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
            return new DataFetcher<InputStream>() {
                @Override
                public InputStream loadData(Priority priority) throws Exception {
                    throw new IOException();
                }

                @Override
                public void cleanup() {

                }

                @Override
                public String getId() {
                    return model;
                }

                @Override
                public void cancel() {

                }
            };
        }
    };
    /**
     *
     */
    public static BitmapTransformation bitmapTransformation;
    public static int errorResID;

    public GlideHelper() {
        if (BuildConfig.ENVIRONMENT.equals("qyt")) {
            errorResID = R.mipmap.bg_navigation_qyt;
        } else if (BuildConfig.ENVIRONMENT.equals("qyb")) {
            errorResID = R.mipmap.bg_navigation_qyb;
        } else {
            errorResID = R.mipmap.bg_navigation_qyb;
        }
    }

    /**
     * Fill an ImageView with a picture from the resources using Glide.
     *
     * @param context       the Context for where to load
     * @param imageView     the ImageView to fill with an image
     * @param resDrawableId the resource drawable id
     */
    public static void resDrawableToImageView(Context context, ImageView imageView, int resDrawableId) {
        if (context == null || imageView == null) {
            return;
        }
        Glide.with(context)
                .load(resDrawableId)
                .thumbnail(0.1f)
                .error(errorResID)
                .centerCrop()
                .into(imageView);
    }


    /**
     * @param context   the Context for where to load
     * @param imageView the ImageView to fill with an image
     * @param imageUrl  the image url from which Glide should download and cache the image
     */
    public static void urlToImageView(Context context, ImageView imageView, @NonNull String imageUrl, int imageType) {
        urlToImageView(context, imageView, imageUrl, imageType, 0, false);
    }

    /**
     * Fill an ImageView with a picture from an http link using Glide.
     *
     * @param context                  the Context for where to load
     * @param imageView                the ImageView to fill with an image
     * @param imageUrl                 the image url from which Glide should download and cache the image
     * @param placeholderDrawableResId the resource id of the image that should be used as a placeholder image
     */
    public static void urlToImageView(Context context, ImageView imageView, @NonNull String imageUrl, int imageType,
                                      int placeholderDrawableResId) {
        urlToImageView(context, imageView, imageUrl, imageType, placeholderDrawableResId, false);
    }

    /**
     * Fill an ImageView with a picture from an Http link using Glide.
     *
     * @param context                  the Context for where to load
     * @param imageView                the ImageView to fill with an image
     * @param imageUrl                 the image url from which Glide should download and cache the image
     * @param placeholderDrawableResId the resource id of the image that should be used as a placeholder image
     * @param useCacheOnly             whether to only use the cache to load the pictures or allow downloading the
     *                                 picture if the picture is not found in the cache.
     */
    public static void urlToImageView(Context context, ImageView imageView, @NonNull String imageUrl, int imageType,
                                      int placeholderDrawableResId, boolean useCacheOnly) {
        if (context == null || imageView == null) {
            return;
        }
        if (placeholderDrawableResId == 0 && BuildConfig.ENVIRONMENT.equals("qyt")) {
            placeholderDrawableResId = R.mipmap.bg_navigation_qyt;
        } else if (placeholderDrawableResId == 0 && BuildConfig.ENVIRONMENT.equals("qyb")) {
            placeholderDrawableResId = R.mipmap.bg_navigation_qyb;
        } else if (placeholderDrawableResId == 0) {
            placeholderDrawableResId = R.mipmap.bg_navigation_qyb;
        }
        switch (imageType) {
            case Glide_Imgae_Circle:
                bitmapTransformation = new GlideCircleTransform(context);
                break;
            case Glide_Imgae_Round:
                bitmapTransformation = new GlideRoundTransform(context);
                break;
        }
        if (imageType == Glide_Imgae_Normal) {
            if (useCacheOnly) {
                Glide.with(context)
                        .using(cacheOnlyStreamLoader)
                        .load(imageUrl)
                        .placeholder(placeholderDrawableResId)
                        .thumbnail(0.1f)
                        .error(errorResID)
                        .into(imageView);
                return;
            }
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(placeholderDrawableResId)
                    .thumbnail(0.1f)
                    .error(errorResID)
                    .into(imageView);
        } else {
            if (useCacheOnly) {
                Glide.with(context)
                        .using(cacheOnlyStreamLoader)
                        .load(imageUrl)
                        .placeholder(placeholderDrawableResId)
                        .error(errorResID)
                        .fitCenter()
                        .transform(bitmapTransformation)
                        .into(imageView);
                return;
            }
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(placeholderDrawableResId)
                    .fitCenter()
                    .transform(bitmapTransformation)
                    .error(errorResID)
                    .into(imageView);
        }
    }
}