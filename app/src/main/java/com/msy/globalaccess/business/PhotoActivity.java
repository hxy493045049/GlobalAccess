package com.msy.globalaccess.business;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by pepys on 2017/5/21.
 * description:展示单张图片
 */
public class PhotoActivity extends BaseActivity {


    @BindView(R.id.photo_img_content)
    ImageView photoImgContent;


    private String imgUrl="";

    @Override
    protected void initInjector() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo2;
    }

    private static String IMGURL = "imgUrl";

    public static void callActivity(Context context,String imgUrl){
        Intent intent = new Intent(context,PhotoActivity.class);
        intent.putExtra(IMGURL, imgUrl);
        context.startActivity(intent);
    }

    @Override
    protected int getStatusColor() {
        return R.color.black;
    }

    private void getIntentData(){
        if(getIntent().hasExtra(IMGURL)){
            imgUrl = getIntent().getStringExtra(IMGURL);
        }
    }

    @Override
    protected void init() {
        getIntentData();
        initView();
    }

    private void initView(){
        Glide.with(PhotoActivity.this)
                .load(imgUrl)
                .fitCenter()
                .crossFade()
                .error(R.mipmap.icon_datouxiang)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoImgContent);
    }

    @OnClick({R.id.photo_bt_exit,R.id.photo_img_content})
    public void btnOnclick(View view){
        switch (view.getId()){
            case R.id.photo_img_content:
            case R.id.photo_bt_exit:
                finish();
                break;
        }
    }
}
