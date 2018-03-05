package com.msy.globalaccess.business.navigation.contract.impl;

import com.liulishuo.filedownloader.FileDownloader;
import com.msy.globalaccess.base.impl.BasePresenterProgressImpl;
import com.msy.globalaccess.business.navigation.contract.NewVersionContract;
import com.msy.globalaccess.common.enums.ResultCode;
import com.msy.globalaccess.data.RequestCallBackProgress;
import com.msy.globalaccess.data.bean.UpdateVersionBean;
import com.msy.globalaccess.data.interactor.impl.LoadNewVersionInteractorImpl;
import com.msy.globalaccess.utils.ApplicationUtils;

import javax.inject.Inject;

import rx.Subscription;


/**
 * Created by pepys on 2017/5/15.
 * description:
 */
public class NewVersionPresenterImpl extends BasePresenterProgressImpl<NewVersionContract.View> implements NewVersionContract.Presenter {

    private LoadNewVersionInteractorImpl newVersionInteractor;
    private int downApkID;
    @Inject
    public NewVersionPresenterImpl(LoadNewVersionInteractorImpl newVersionInteractor) {
        this.newVersionInteractor = newVersionInteractor;
    }

    @Override
    public void onStart() {
    }


    @Override
    public void loadNewVersion() {
        mView.showProgress();
        Subscription subscription = newVersionInteractor.loadNewVersion(new RequestCallBackAdapter<UpdateVersionBean>
                () {
            @Override
            public void success(UpdateVersionBean data) {
                mView.hideProgress();
                try {
                    int serviceVersionCode = Integer.parseInt(data.getVersionCode());
                    int locationVersionCode = Integer.parseInt(ApplicationUtils.getVersion()[1]);
                    if(serviceVersionCode>locationVersionCode){
//                if(!data.getVersionCode().equals("1")){
                        mView.loadNewVersionSuccess(data);
                    }else{
                        mView.nextActivity();
                    }
                }catch(Exception e){
                    mView.nextActivity();
                }

            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                super.onError(resultCode, errorMsg);
                mView.loadNewVersionFail();
                mView.hideProgress();
            }
        });
        cacheSubscription.add(subscription);
    }

    @Override
    public void downNewApp() {
        downApkID = newVersionInteractor.downNewApp(new RequestCallBackProgress<String>() {
            @Override
            public void beforeRequest() {

            }

            @Override
            public void progress(int soFarBytes, int totalBytes, int speed) {
                mView.setProgress(soFarBytes, totalBytes,speed);
            }

            @Override
            public void success(String data) {
                mView.downSuccess();
            }

            @Override
            public void onError(@ResultCode int resultCode, String errorMsg) {
                mView.downFail();
            }

            @Override
            public void after() {

            }
        },mView.getdownUrl(),mView.getPath());
    }

    @Override
    public void paushDown() {
        if(downApkID != 0){
            FileDownloader.getImpl().pause(downApkID);
        }
    }

}
