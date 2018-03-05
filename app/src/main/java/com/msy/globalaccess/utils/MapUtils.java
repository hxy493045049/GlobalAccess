package com.msy.globalaccess.utils;

import com.msy.globalaccess.data.bean.search.PerequisiteBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * map工具类
 * Created by chensh on 2017/2/11 0011.
 */

public class MapUtils {
    /**
     * 对比map1中是否包含map2中的key
     *
     * @param map1
     * @param map2
     * @return
     */
    public static Object diff(Map<Object, Object> map1, Map<Object, Object> map2) {
        Set<Object> set1 = map1.keySet();
        Iterator<Object> it = set1.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (!map2.containsKey(obj)) {

                return obj;
            }
        }
        return null;
    }

    public static ArrayList<PerequisiteBean> convertList(Map<String, PerequisiteBean> map) {
        Collection<PerequisiteBean> valueCollection = map.values();
        ArrayList<PerequisiteBean> valueList = new ArrayList<>(valueCollection);
        return valueList;
    }
}
