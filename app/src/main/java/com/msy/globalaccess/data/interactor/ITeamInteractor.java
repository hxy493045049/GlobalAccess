package com.msy.globalaccess.data.interactor;

import com.msy.globalaccess.data.RequestCallBack;
import com.msy.globalaccess.data.bean.base.NoDataBean;
import com.msy.globalaccess.data.bean.team.TeamDetailBean;
import com.msy.globalaccess.data.bean.team.TeamListBean;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * Created by pepys on 2017/2/10.
 * description: 团队详情    相当于model接口
 */
public interface ITeamInteractor {
    Subscription getTeamDetail(@NotNull RequestCallBack<TeamDetailBean> callBack, @NotNull HashMap<String, String> param);
    Subscription checkIsChange(@NotNull RequestCallBack<NoDataBean> callBack, @NotNull HashMap<String, String> param);
    Subscription getDataByPage(@NotNull RequestCallBack<List<TeamListBean>> callBack, @NotNull HashMap<String, String> param);
}
