package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * Created by shawn on 2017/3/28 0028.
 * <p>
 * description :
 */

public interface IKeyMapInteractor<T> {
    Subscription getDataByType(@NotNull RequestCallBack<List<T>> callBack, @NotNull HashMap<String,
            String> params);
}
