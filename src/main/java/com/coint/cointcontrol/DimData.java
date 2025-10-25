package com.coint.cointcontrol;

import java.util.HashMap;
import java.util.Map;

public class DimData {

    public Map<String, chunkdata> data;

    public DimData(int chunckx, int chunckz, chunkdata cd) {
        String s = chunckx + "," + chunckz;
        this.data.put(s, cd);
    }

    public DimData(Map<String, chunkdata> data) {
        this.data = data;
    }

    public DimData() {
        this.data = new HashMap<>();
    }

    public Map<String, chunkdata> getData() {
        return this.data;
    }
}
