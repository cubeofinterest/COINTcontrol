package com.coint.cointcontrol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class chunkdata {

    public ConcurrentHashMap<Integer, Integer> restrictions = new ConcurrentHashMap<>();

    public chunkdata(int a, int b) {
        this.restrictions.put(a, b);
    }

    public chunkdata(Map<Integer, Integer> map) {
        this.restrictions.putAll(map);
    }

    public chunkdata() {}

    public ConcurrentHashMap<Integer, Integer> getData() {
        return restrictions;
    }
}
