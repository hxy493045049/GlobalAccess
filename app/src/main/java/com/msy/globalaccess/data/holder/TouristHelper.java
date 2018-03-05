package com.msy.globalaccess.data.holder;

import android.util.Log;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.scenic.ScenicListBean;
import com.msy.globalaccess.data.db.ScenicListBeanDao;

import java.util.List;

import cn.msy.zc.commonutils.StringUtils;

/**
 * 景区-数据库帮助类
 * Created by chensh on 2017/5/16 0016.
 */

public class TouristHelper {
    private static String TAG = TouristHelper.class.getSimpleName();

    public TouristHelper() {
    }

    public static TouristHelper getInstance() {
        return TouristHelper.Holder.instance;
    }

    public static class Holder {
        private static TouristHelper instance = new TouristHelper();
    }

    /**
     * 关键字查询
     *
     * @param key 关键字
     * @return 旅行社列表
     */
    public List<ScenicListBean> getKeyList(String key) {
        List<ScenicListBean> list = null;
        try {
            ScenicListBeanDao listDao = App.getDaoSession().getScenicListBeanDao();
            if (StringUtils.isEmpty(key)) {
                list = listDao.loadAll();
            } else {
                list = listDao.queryBuilder().where(ScenicListBeanDao.Properties.ScenicName.like("%" + key + "%")).list();
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return list;
    }


    /**
     * 获取全部数据
     *
     * @return 列表
     */
    public List<ScenicListBean> getTravelAll() {
        List<ScenicListBean> list = null;
        try {
            ScenicListBeanDao listDao = App.getDaoSession().getScenicListBeanDao();
            list = listDao.loadAll();
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        if(list.size() == 0 ){
            ScenicListBean  allTravel = new ScenicListBean();
            allTravel.setScenicId("");
            allTravel.setScenicName("全部");
            list.add(0,allTravel);
        }
        return list;
    }


    /**
     * 插入旅行社列表数据
     *
     * @param list 旅行社数据
     */
    public void insertTravelAgentList(List<ScenicListBean> list) {
        ScenicListBeanDao listDao = App.getDaoSession().getScenicListBeanDao();
        if (list != null && list.size() > 0) {
            listDao.deleteAll();
            ScenicListBean  allTravel = new ScenicListBean();
            allTravel.setScenicId("");
            allTravel.setScenicName("全部");
            list.add(0,allTravel);
            listDao.insertInTx(list);
        }
    }
}
