package com.msy.globalaccess.base;

import android.support.annotation.NonNull;

/**
 * Created by hxy on 2016/12/13 0013.
 * <p>
 * description :
 */

public interface BaseContract {
    interface View {
        void showProgress();

        void hideProgress();
    }

    interface Presenter {
        void onStart();

        void attachView(@NonNull View view);

        void onDestroy();

        void cancelRequest();
    }
}
