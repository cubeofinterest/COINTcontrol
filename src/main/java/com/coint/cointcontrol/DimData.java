package com.coint.cointcontrol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DimData {

    public ConcurrentHashMap<String, chunkdata> data;

    public DimData(int chunckx, int chunckz, chunkdata cd) {
        String s = chunckx + "," + chunckz;
        this.data.put(s, cd);
    }

    public DimData(Map<String, chunkdata> data) {
        this.data = new ConcurrentHashMap<>(data);
    }

    public DimData() {
        this.data = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, chunkdata> getData() {
        return this.data;
    }
}
