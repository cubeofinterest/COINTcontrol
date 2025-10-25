package com.coint.cointcontrol;

import java.util.HashMap;
import java.util.Map;

public class chunkdata {

    public Map<Integer, Integer> restrictions = new HashMap<>();

    public chunkdata(int a, int b) {
        this.restrictions.put(a, b);
    }

    public chunkdata(Map<Integer, Integer> map) {
        this.restrictions.putAll(map);
    }

    public chunkdata() {}

    public Map<Integer, Integer> getData() {
        return restrictions;
    }
}
