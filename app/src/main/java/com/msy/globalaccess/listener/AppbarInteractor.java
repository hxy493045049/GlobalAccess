package com.msy.globalaccess.listener;

/**
 * 设置AppbarLayout
 * Created by hxy on 2017/1/22 0022.
 * <p>
 * description : 变更appbar的状态,用于相应fragment的回调
 */
public interface AppbarInteractor {
    /**
     * @param expaned 展开状态还是折叠状态
     * @param anim    控制切换到新的状态时是否需要动画
     */
    void changeAppbar(boolean expaned, boolean anim);
}
