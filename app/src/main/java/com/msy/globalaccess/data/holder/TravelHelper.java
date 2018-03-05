package com.msy.globalaccess.data.holder;

import android.util.Log;

import com.msy.globalaccess.base.App;
import com.msy.globalaccess.data.bean.tourism.TourismDropListBean;
import com.msy.globalaccess.data.bean.travel.TravelAgentListBean;
import com.msy.globalaccess.data.db.TravelAgentListBeanDao;

import java.util.ArrayList;
import java.util.List;

import cn.msy.zc.commonutils.StringUtils;

/**
 * 旅游局-帮助类
 * Created by chensh on 2017/5/16 0016.
 */

public class TravelHelper {
    private static String TAG = TravelHelper.class.getSimpleName();

    public static TravelHelper getInstance() {
        return Holder.instance;
    }

    private static class Holder {
        private static TravelHelper instance = new TravelHelper();
    }

    /**
     * 关键字查询
     *
     * @param key 关键字
     * @return 旅行社列表
     */
    public List<TravelAgentListBean> getKeyList(String key) {
        List<TravelAgentListBean> list = null;
        try {
            TravelAgentListBeanDao listDao = App.getDaoSession().getTravelAgentListBeanDao();
            if (StringUtils.isEmpty(key)) {
                list = listDao.loadAll();
            } else {
                list = listDao.queryBuilder().where(TravelAgentListBeanDao.Properties.TravelAgentName.like("%" + key + "%")).list();
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
    public List<TravelAgentListBean> getTravelAll() {
        List<TravelAgentListBean> list = null;
        try {
            TravelAgentListBeanDao listDao = App.getDaoSession().getTravelAgentListBeanDao();
            list = listDao.loadAll();
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        if(list.size() == 0 ){
            TravelAgentListBean allTravel = new TravelAgentListBean();
            allTravel.setTravelAgentId("");
            allTravel.setTravelAgentName("全部");
            list.add(0,allTravel);
        }
        return list;
    }


    /**
     * 插入旅行社列表数据
     *
     * @param list 旅行社数据
     */
    public void insertTravelAgentList(List<TravelAgentListBean> list) {
        try {
            TravelAgentListBeanDao listDao = App.getDaoSession().getTravelAgentListBeanDao();
            if (list != null && list.size() > 0) {
                listDao.deleteAll();
                TravelAgentListBean allTravel = new TravelAgentListBean();
                allTravel.setTravelAgentId("");
                allTravel.setTravelAgentName("全部");
                list.add(0,allTravel);
                listDao.insertInTx(list);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 团队类型数据
     */
    private ArrayList<TourismDropListBean.DropSelectableBean> StatusList;

    /**
     * 客源地数据
     */
    private ArrayList<TourismDropListBean.DropSelectableBean> TouristsList;

    /**
     * 返回团队类型数据
     *
     * @return list
     */
    public ArrayList<TourismDropListBean.DropSelectableBean> getStatusList() {
        if (StatusList == null) {
            StatusList();
        }
        return StatusList;
    }

    /**
     * 返回团客源地数据
     *
     * @return list
     */
    public ArrayList<TourismDropListBean.DropSelectableBean> getTouristsList() {
        if (TouristsList == null) {
            TouristList();
        }
        return TouristsList;
    }

    /**
     * 团队查询  状态对应的值
     */
    private void StatusList() {
        StatusList = new ArrayList<>();
        StatusList.add(new TourismDropListBean.DropSelectableBean("编辑中", "0", false));
        StatusList.add(new TourismDropListBean.DropSelectableBean("已提交", "1", false));
        StatusList.add(new TourismDropListBean.DropSelectableBean("生效", "2", false));
        StatusList.add(new TourismDropListBean.DropSelectableBean("作废", "3", false));
    }


    /**
     * 团队查询  状态对应的值
     */
    private void TouristList() {
        TouristsList = new ArrayList<>();
        TouristsList.add(new TourismDropListBean.DropSelectableBean("国内", "0", false));
        TouristsList.add(new TourismDropListBean.DropSelectableBean("入境", "1", false));
    }
}
