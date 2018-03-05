package com.msy.globalaccess.data.bean.search;

import com.msy.globalaccess.data.bean.scenic.ScenicListBean;

import java.util.ArrayList;

/**
 * 景区
 * Created by chensh on 2017/2/14 0014.
 */

public class SearchScenicBean {


    /**
     * message : 查询成功
     * status : 0
     * data : {"totalNum":32,"scenicList":[{"scenicId":"121","scenicName":"核心景区大门"},{"scenicId":"122","scenicName":"天子山"},{"scenicId":"123","scenicName":"C线"},{"scenicId":"124","scenicName":"贺龙纪念馆"},{"scenicId":"164","scenicName":"WQ"},{"scenicId":"165","scenicName":"F线"},{"scenicId":"202","scenicName":"D线"},{"scenicId":"301","scenicName":"L线"},{"scenicId":"302","scenicName":"G线"},{"scenicId":"324","scenicName":"大峡谷C线"},{"scenicId":"333","scenicName":"安徽无需验证景点cai"},{"scenicId":"334","scenicName":"景点公司设置cai"},{"scenicId":"335","scenicName":"景区设置cai"},{"scenicId":"336","scenicName":"景点认证cai"},{"scenicId":"337","scenicName":"行程点认证不结算cai"},{"scenicId":"338","scenicName":"行程点认证cai"},{"scenicId":"339","scenicName":"测试修改功能"},{"scenicId":"345","scenicName":"东西渡口"},{"scenicId":"346","scenicName":"森林公园"},{"scenicId":"347","scenicName":"W不结算"}]}
     */
    /**
     * totalNum : 32
     * scenicList : [{"scenicId":"121","scenicName":"核心景区大门"},{"scenicId":"122","scenicName":"天子山"},{"scenicId":"123","scenicName":"C线"},{"scenicId":"124","scenicName":"贺龙纪念馆"},{"scenicId":"164","scenicName":"WQ"},{"scenicId":"165","scenicName":"F线"},{"scenicId":"202","scenicName":"D线"},{"scenicId":"301","scenicName":"L线"},{"scenicId":"302","scenicName":"G线"},{"scenicId":"324","scenicName":"大峡谷C线"},{"scenicId":"333","scenicName":"安徽无需验证景点cai"},{"scenicId":"334","scenicName":"景点公司设置cai"},{"scenicId":"335","scenicName":"景区设置cai"},{"scenicId":"336","scenicName":"景点认证cai"},{"scenicId":"337","scenicName":"行程点认证不结算cai"},{"scenicId":"338","scenicName":"行程点认证cai"},{"scenicId":"339","scenicName":"测试修改功能"},{"scenicId":"345","scenicName":"东西渡口"},{"scenicId":"346","scenicName":"森林公园"},{"scenicId":"347","scenicName":"W不结算"}]
     */

    private int totalNum;
    private ArrayList<ScenicListBean> scenicList;

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public ArrayList<ScenicListBean> getScenicList() {
        return scenicList;
    }

    public void setScenicList(ArrayList<ScenicListBean> scenicList) {
        this.scenicList = scenicList;
    }
}
