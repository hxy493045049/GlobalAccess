package com.msy.globalaccess.data.interactor.impl;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.msy.globalaccess.R;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.RequestCallBackProgress;
import com.msy.globalaccess.data.api.UpdataVersionApi;
import com.msy.globalaccess.data.bean.base.BaseBean;
import com.msy.globalaccess.data.bean.UpdateVersionBean;
import com.msy.globalaccess.data.interactor.ILoadNewVersionInteractor;
import com.msy.globalaccess.utils.helper.ParamsHelper;
import com.msy.globalaccess.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pepys on 2017/5/15.
 * description:
 */
public class LoadNewVersionInteractorImpl implements ILoadNewVersionInteractor {

    private UpdataVersionApi updateNewVersionApi;
    @Inject
    public LoadNewVersionInteractorImpl(Retrofit retrofit){
        updateNewVersionApi = retrofit.create(UpdataVersionApi.class);
    }
    @Override
    public Subscription loadNewVersion(final RequestCallBack callBack) {
        HashMap<String, String> params = new ParamsHelper.Builder()
                .setMethod(UpdataVersionApi.method)
                .setParam("appType","1")
                .build(false).getParam();
        return updateNewVersionApi.getNewVersion(params)
                .subscribeOn(Schedulers.io())//请求在新的线程中执行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseBean<UpdateVersionBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(ResultCode.NET_ERROR, "");
                        ToastUtils.showToast(R.string.internet_error);
                        Logger.e(e, "TouristSpotsInteractorImpl");
                        callBack.after();
                    }

                    @Override
                    public void onNext(BaseBean<UpdateVersionBean> updateVersionBaseBean) {
                        if (updateVersionBaseBean.getStatus() == ResultCode.SUCCESS) {
                            callBack.success(updateVersionBaseBean.getData());
                        } else {
                            ToastUtils.showToast(updateVersionBaseBean.getMessage());
                            callBack.onError(ResultCode.NET_ERROR, updateVersionBaseBean.getMessage());
                        }
                    }
                });
    }


    @Override
    public int downNewApp(final RequestCallBackProgress callBack, String url, String path) {
        return FileDownloader.getImpl().create(url)
                .setPath(path)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .setListener(new FileDownloadSampleListener() {
                    @Override
                    protected void pending(BaseDownloadTask baseDownloadTask, int soFarBytes, int totalBytes) {
                        System.out.println("pending   soFarBytes:"+soFarBytes+"          soFarBytes:"+totalBytes);
                    }
                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                        System.out.println("connected   soFarBytes:"+soFarBytes+"   isContinue:"+isContinue+"          soFarBytes:"+totalBytes);
                    }
                    @Override
                    protected void progress(BaseDownloadTask baseDownloadTask, int soFarBytes, int totalBytes) {
                        callBack.progress(soFarBytes,totalBytes,baseDownloadTask.getSpeed());
                    }

                    @Override
                    protected void paused(BaseDownloadTask baseDownloadTask, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void completed(BaseDownloadTask baseDownloadTask) {
                        callBack.success("成功");
                    }

                    @Override
                    protected void error(BaseDownloadTask baseDownloadTask, Throwable throwable) {
                        ToastUtils.showLongToast(throwable.getMessage());
                        Logger.e(throwable,"downApk_error");
                        callBack.onError(ResultCode.NET_ERROR,throwable.getMessage());
                    }

                    @Override
                    protected void warn(BaseDownloadTask baseDownloadTask) {
                        System.out.println("warn");
                    }
                }).start();

    }
}
