package com.msy.globalaccess.data.bean.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WuDebin on 2017/2/15.
 * 序列化map供Bundle传递map使用
 */

public class SerializableHashMap<T,K> implements Serializable {
    private HashMap<T,K> map;

    public HashMap<T,K> getHashMap() {
        return map;
    }

    public void setHashMap(HashMap<T,K> map) {
        this.map = map;
    }
}
