package com.msy.globalaccess.business;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by pepys on 2017/5/21.
 * description:展示多张图片
 */
public class PhotoListActivity extends BaseActivity {

    private static final String TAG = PhotoListActivity.class.getSimpleName();
    /**
     * 记录当前是多少页
     */
    private static int count;
    /**
     * 填充布局
     */
    private ArrayList<View> listViews = new ArrayList<>();
    /**
     * ViewPager
     */
    private ViewPager pager;
    /**
     * MyPageAdapter
     */
    private MyPageAdapter adapter;
    /**
     * 删除布局
     */
    private RelativeLayout photo_relativeLayout;
    /**
     * 数据源
     */
    private ArrayList<String> imgPathList = new ArrayList<>();
    /**
     * 删除按钮
     */
    private Button photo_bt_del;
    @BindView(R.id.photo_bt_exit)
    Button photo_bt_exit;
    /**
     * 进来的时候显示第几页
     */
    private int posotion;

    private static final String IMGPATHLIST ="imgPathList";
    private static final String PHOTO_POSOTION ="posotion";
    private static final String ISSHOWDEL ="isShowDel";

    private boolean isShowDel = true;

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int position) {// 页面选择响应函数
            count = position;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {// 滑动中。。。

        }

        public void onPageScrollStateChanged(int position) {// 滑动状态改变
        }
    };

    public static void callActivity(Context context,ArrayList<String> imgPathList,int posotion){
        Intent intent = new Intent(context,PhotoListActivity.class);
        intent.putExtra(PHOTO_POSOTION, posotion);
        intent.putExtra(IMGPATHLIST, imgPathList);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void init() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntentData();
        initView();

        adapter = new MyPageAdapter(this, listViews);// 构造adapter
        pager.setAdapter(adapter);// 设置适配器
        pager.setCurrentItem(posotion);
        pager.setOffscreenPageLimit(9);
    }

    private void initView() {
        photo_relativeLayout = (RelativeLayout) findViewById(R.id.photo_relativeLayout);
        photo_relativeLayout.setBackgroundColor(0x70000000);
        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.addOnPageChangeListener(pageChangeListener);
        photo_bt_del = (Button) findViewById(R.id.photo_bt_del);
        if (!isShowDel) {
            photo_bt_del.setVisibility(View.GONE);
        }
    }
    @OnClick({R.id.photo_bt_exit})
    public void initListener(View view) {
        switch (view.getId()){
            case R.id.photo_bt_exit:
                finish();
                break;
        }
        /*photo_bt_del.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (imgPathList != null && imgPathList.size() > 0) {
                    if (imgPathList.size() == 1) {
                        if (isHeadPic) {
                            //用于区分头图-- 设置为-1
                            EventBus.getDefault().post(new DeleteImgFromPhoto(-1, imgPathList.get(0)));
                        } else {
                            EventBus.getDefault().post(new DeleteImgFromPhoto(0, imgPathList.get(0)));
                        }
                        imgPathList.clear();
                        finish();
                    } else {
                        EventBus.getDefault().post(new DeleteImgFromPhoto(count, imgPathList.get(count)));
                        imgPathList.remove(count);
                        pager.removeAllViews();
                        adapter.clear();
                        pager.setAdapter(adapter);
                        if (count == 0) {
                            count = 0;
                            pager.setCurrentItem(0);
                        } else {
                            count--;
                            pager.setCurrentItem(count);
                        }
                    }
                }
            }
        });*/
    }

    private void initIntentData() {
        Intent intent = getIntent();
        imgPathList = (ArrayList<String>) intent.getSerializableExtra(IMGPATHLIST);
        posotion = intent.getIntExtra(PHOTO_POSOTION, 0);
        isShowDel = intent.getBooleanExtra(ISSHOWDEL, false);
    }


    @Override
    public void finish() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("netImage",  imgPathList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        super.finish();
    }

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;// content
        private Context context;

        public MyPageAdapter(Context context, ArrayList<View> listViews) {// 构造函数
            // 初始化viewpager的时候给的一个页面
            this.listViews = listViews;
            this.context = context;
        }

        public void remove(int position) {
            listViews.remove(position);
        }

        public void clear() {
            listViews.clear();
        }

        public int getCount() {// 返回数量
            return imgPathList.size();
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            // 销毁view对象
            if (imgPathList != null && imgPathList.size() > 0) {
                int index = position % imgPathList.size();
                if (index < listViews.size()) container.removeView(listViews.get(position % (imgPathList.size())));
            }
        }

        public Object instantiateItem(ViewGroup container, int position) {// 返回view对象
            ImageView img = new ImageView(context);// 构造textView对象
            try {
                img.setBackgroundColor(0xff000000);
                if (imgPathList != null && imgPathList.size() > 0 && position < imgPathList.size()) {
                    Glide.with(PhotoListActivity.this)
                            .load(imgPathList.get(position))
                            .fitCenter()
                            .crossFade()
                            .error(R.mipmap.icon_daoyou_default)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(img);
                }
                img.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                img.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        finish();
                    }
                });
                container.addView(img, 0);
                listViews.add(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return img;
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
}
