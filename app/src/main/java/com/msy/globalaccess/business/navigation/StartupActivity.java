package com.msy.globalaccess.business.navigation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.msy.globalaccess.BuildConfig;
import com.msy.globalaccess.R;
import com.msy.globalaccess.base.BaseActivity;
import com.msy.globalaccess.business.navigation.contract.NewVersionContract;
import com.msy.globalaccess.business.navigation.contract.impl.NewVersionPresenterImpl;
import com.msy.globalaccess.data.bean.UpdateVersionBean;
import com.msy.globalaccess.utils.ToastUtils;
import com.msy.globalaccess.widget.dialog.PopUpWindowAlertDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import cn.msy.zc.commonutils.FileUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.msy.globalaccess.config.DataSetting.apkPath;
import static com.msy.globalaccess.config.DataSetting.path;

public class StartupActivity extends BaseActivity implements NewVersionContract.View {

    @BindView(R.id.startup_img_main)
    ImageView startup_img_main;

    @Inject
    NewVersionPresenterImpl presenter;

    private String downNewAppUrl = "";

    private String downNewPath = "";

    /**
     * 下载的pop
     */
    private PopUpWindowAlertDialog popDownApk;
    /**
     * 下载View
     */
    private View downApkView;
    /**
     * 下载进度条
     */
    private ProgressBar down_apk_progressBar;
    /**
     * 下载进度
     */
    private TextView down_apk_total;
    /**
     * 下载速度
     */
    private TextView down_apk_speed;
    /**
     * 暂停/开始
     */
    private TextView down_apk_btn_pause;
    /**
     * 暂停/开始区域
     */
    private LinearLayout down_apk_ll_pause;
    /**
     * 下载完成后安装
     */
    private TextView down_apk_btn_close;
    /**
     * 下载完成后安装区域
     */
    private LinearLayout down_apk_ll_close;
    /**
     * 动态权限
     */
    private RxPermissions rxPermissions;
    /**
     * 要申请的权限
     */
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 是否有写入权限
     */
    private boolean isWriteExteanal = false;
    /**
     * 是否暂停
     */
    private boolean isPause = false;
    /**
     * 数字格式化
     */
    private DecimalFormat df=new DecimalFormat("0.00");

    private PopUpWindowAlertDialog newVersionPop;

    private UpdateVersionBean mUpdateVersionBean;

    public static void callActivity(Context ctx) {
        Intent intent = new Intent(ctx, StartupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ctx.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_startup;
    }

    @Override
    protected void initInjector() {
        mActivityComponent.inject(this);
        //让presenter保持view接口的引用
        presenter.attachView(this);
        //让baseactivity自动执行oncreate 以及 在activitydestroy时能及时释放subscribe
        basePresenter = presenter;
    }

    @Override
    protected void init() {
        initPermissions();
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        finishActivity();
    }
    private void initPermissions(){
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        isWriteExteanal = granted;
                        if(!granted){
                            //点击拒绝
                            showAlertDialog("需要开启读写权限",1);
                        }else{
                            presenter.loadNewVersion();
                        }
                    }
                });
    }
    private void initView() {
        if (BuildConfig.ENVIRONMENT.equals("qyt")) {
            startup_img_main.setImageResource(R.mipmap.icon_startup_qyt);
        } else if (BuildConfig.ENVIRONMENT.equals("qyb")) {
            startup_img_main.setImageResource(R.mipmap.icon_startup_qyb);
        }
        downApkView = LayoutInflater.from(this).inflate(R.layout.pop_down_apk_progress,null,false);
        down_apk_progressBar = (ProgressBar) downApkView.findViewById(R.id.down_apk_progressBar);
        down_apk_total = (TextView) downApkView.findViewById(R.id.down_apk_total);
        down_apk_speed = (TextView) downApkView.findViewById(R.id.down_apk_speed);
        down_apk_ll_pause = (LinearLayout) downApkView.findViewById(R.id.down_apk_ll_pause);
        down_apk_btn_pause = (TextView) downApkView.findViewById(R.id.down_apk_btn_pause);
        down_apk_ll_close = (LinearLayout) downApkView.findViewById(R.id.down_apk_ll_colse);
        down_apk_ll_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPause){
                    down_apk_btn_pause.setText("暂停");
                    presenter.downNewApp();
                }else{
                    down_apk_btn_pause.setText("开始");
                    presenter.paushDown();
                }
                isPause = !isPause;
            }
        });
        down_apk_ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.paushDown();
                popDownApk.dismiss();
                finish();
            }
        });
    }


    @Override
    public String getdownUrl() {
        return downNewAppUrl;
    }
    @Override
    public String getPath() {
        return downNewPath;
    }

    @Override
    public void setProgress(int soFarBytes, int totalBytes,int speed) {
        down_apk_progressBar.setMax(totalBytes);
        down_apk_progressBar.setProgress(soFarBytes);
        down_apk_total.setText("总大小："+(df.format((float)totalBytes/1024/1024))+"M   已下载"+(df.format(((float)soFarBytes/1024/1024)))+"M");
        down_apk_speed.setText("速度："+(df.format((speed)))+"Kb/s");
    }

    @Override
    public void nextActivity() {
        Observable.timer(300, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startActivity(new Intent(StartupActivity.this, NavigationActivity.class));
                        finish();
                        overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
                    }
                });
    }

    @Override
    public void loadNewVersionSuccess(UpdateVersionBean updateVersionBean) {
        mUpdateVersionBean = updateVersionBean;
        downNewAppUrl = updateVersionBean.getDownloadUrl();
        downNewPath = path+ updateVersionBean.getVersion()+".apk";
        if(isWriteExteanal){
            showAlertDialog();
        }
    }

    @Override
    public void loadNewVersionFail() {
        nextActivity();
    }

    @Override
    public void downSuccess() {
        isPause = true;
        down_apk_total.setText("下载完成");
        down_apk_speed.setText("速度：0Kb/s");
        down_apk_btn_pause.setText("完成");
        popDownApk.dismiss();
        ToastUtils.showToast("下载成功");
        installApk();
        finish();
    }

    @Override
    public void downFail() {

    }

    private void finishActivity() {
        Observable.timer(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        startActivity(new Intent(StartupActivity.this, NavigationActivity.class));
                        finish();
                        overridePendingTransition(R.anim.right_in_x, R.anim.left_out_x);
                    }
                });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }


    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showAlertDialog("请在-应用设置-权限中，允许使用",2);
                    } else {
                        finish();
                    }
                } else {
                    ToastUtils.showToast("权限获取成功");
                }
            }
        }
    }
    /**
     * 展示Dialog
     */
    private void showAlertDialog(String message, final int type) {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        builder.setMessage(message, 18);
        builder.setTitle(null, 0);
        builder.setPositiveButton("立即开启", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (type){
                    case 1:
                        ActivityCompat.requestPermissions(StartupActivity.this, permissions, 1);
                        break;
                    case 2:
                        goToAppSetting();
                        break;
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    finish();
            }
        });
        builder.create().show();
    }
    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 1);
    }
    /**
     * 展示Dialog
     *
     */
    private void showAlertDialog() {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_new_version,null,false);
        TextView new_version_pop_tv_content = (TextView) view.findViewById(R.id.new_version_pop_tv_content);
        new_version_pop_tv_content.setText(mUpdateVersionBean.getContent().replace("\\n", "\n"));
        AppCompatButton new_version_pop_btn_confirm = (AppCompatButton) view.findViewById(R.id.new_version_pop_btn_confirm);
        new_version_pop_btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FileUtils.isExist(apkPath)){
                    try {
                        FileUtils.deleteDir(new File(apkPath),false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                newVersionPop.dismiss();
                updateApkProgress();
                presenter.downNewApp();
            }
        });
        builder.setContentView(view);
        builder.setBackKeyPopDismiss(false);
        builder.setContentHeight(400);
        newVersionPop = builder.create();
        newVersionPop.show();
        /*PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
                String title = "发现新版本，马上更新一下！";
                builder.setTitle(title, 0);
                builder.setMessage("发现新版本，马上更新一下！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(FileUtils.isExist(path)){
                            try {
                                FileUtils.deleteFolder(path);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                updateApkProgress();
                presenter.downNewApp();
            }
        });
        builder.setNegativeButton("略过", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nextActivity();
            }
        });
        builder.create().show();*/
    }

    /**
     * 展示Dialog
     *
     */
    private void updateApkProgress() {
        PopUpWindowAlertDialog.Builder builder = new PopUpWindowAlertDialog.Builder(this);
        String title = "更新中....";
        builder.setContentView(downApkView);
        builder.setTitle(title, 0);
        popDownApk = builder.create();
        popDownApk.show();
    }
    public void installApk(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, this.getPackageName()+".fileprovider", new File(downNewPath));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(downNewPath)), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }
}
