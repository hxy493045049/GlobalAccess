package com.msy.globalaccess.data.bean.base;

import java.util.ArrayList;

/**
 * Created by shawn on 2017/3/28 0028.
 * <p>
 * description :
 */

public class MapBeanWrapper<T> {
    private ArrayList<T> pubmapList;

    public ArrayList<T> getPubmapList() {
        return pubmapList;
    }

    public void setPubmapList(ArrayList<T> pubmapList) {
        this.pubmapList = pubmapList;
    }
}
