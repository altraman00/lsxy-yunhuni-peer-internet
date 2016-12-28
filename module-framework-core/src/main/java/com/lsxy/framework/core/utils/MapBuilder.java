package com.lsxy.framework.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuws on 2016/8/29.
 */
public class MapBuilder<K,V> {

    private Map<K,V> map = null;

    public MapBuilder(){
        this(new HashMap<K,V>());
    }

    public MapBuilder(Map<K,V> m){
        map = m;
    }

    public MapBuilder<K,V> put(K k,V v){
        map.put(k,v);
        return this;
    }

    public MapBuilder<K,V> put(K k,V v,V defaultValue){
        if(v == null){
            v = defaultValue;
        }
        map.put(k,v);
        return this;
    }

    public MapBuilder<K,V> putIfNotEmpty(K k,V v){
        if(v != null && !v.toString().trim().equals("")){
            put(k,v);
        }
        return this;
    }

    public MapBuilder<K,V> putIfWhere(K k,boolean where,V v){
        if(v != null && where && !v.toString().trim().equals("")){
            put(k,v);
        }
        return this;
    }

    public Map<K,V> build(){
        return map;
    }
}
