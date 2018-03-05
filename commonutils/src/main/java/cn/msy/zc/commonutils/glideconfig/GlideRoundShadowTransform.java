package cn.msy.zc.commonutils.glideconfig;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.View;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import cn.msy.zc.commonutils.DisplayUtil;

/**
 * 鹏鹏写的圆形带阴影的图片转换器
 */
public class GlideRoundShadowTransform extends BitmapTransformation {
    private int viewWidth = 0;
    private int viewHeight = 0;
    private View view;
    private float radius = 10;
    private String uri;

    public GlideRoundShadowTransform(Context context) {
        super(context);
    }

    public GlideRoundShadowTransform(Context context, View view, String uri) {
        super(context);
        this.view = view;
        this.uri = uri;
    }

    public GlideRoundShadowTransform(Context context, View view, int designRound) {
        this(context);
        this.view = view;
        // 设计图按750算的圆角
        if (context != null) {
            int screenWidth = DisplayUtil.getScreenWidth();
            radius = designRound * screenWidth / 750;
        } else {
            radius = designRound;
        }

    }

    public static Bitmap getShowdowImage(Bitmap bitmap) {
        if (bitmap == null) {
            return bitmap;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 去除锯齿。
        paint.setShadowLayer(15f, 10f, 10f, Color.rgb(180, 180, 180)); // 设置阴影层，这是关键。分别是阴影层半径，X轴偏移量，Y轴偏移量，阴影颜色
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(bitmap, 0, 0, paint);// 画上原图。
        canvas.save();
        return output;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    public Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;
        if (view != null) {
            viewWidth = view.getMeasuredWidth();
            viewHeight = view.getMeasuredHeight();
        }
        int mBitmapWidth = source.getWidth();
        int mBitmapHeight = source.getHeight();
        int resultWidth = 0;
        int resultHeight = 0;
        int x = 0;
        int y = 0;
        if (viewWidth == 0 || viewHeight == 0) {
            resultWidth = source.getWidth();
            resultHeight = source.getHeight();
            x = 0;
            y = 0;
        } else {
            float scale = Math.max(viewWidth * 1.0f / mBitmapWidth, viewHeight * 1.0f / mBitmapHeight);
            resultWidth = (int) (viewWidth / scale);
            resultHeight = (int) (viewHeight / scale);
            x = Math.abs((mBitmapWidth - resultWidth) / 2);
            y = Math.abs((mBitmapHeight - resultHeight) / 2);
        }

        Bitmap squared = Bitmap.createBitmap(source, x, y, resultWidth, resultHeight);

        Bitmap result = pool.get(resultWidth, resultHeight, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(resultWidth, resultHeight, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, resultWidth, resultHeight);
        if (view != null) {
            canvas.drawRoundRect(rectF, radius * resultWidth / viewWidth, radius * resultWidth / viewWidth, paint);
        } else {
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }

        Bitmap outBitmap = getShowdowImage(result);

        return outBitmap;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }

}