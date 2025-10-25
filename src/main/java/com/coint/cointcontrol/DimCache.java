package com.coint.cointcontrol;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.World;

import com.google.gson.Gson;

public class DimCache {

    public static final Gson GSON = new Gson();

    public static File getDimFolder(World world) {
        File worldDir = world.getSaveHandler()
            .getWorldDirectory();
        String dim = "DIM" + world.provider.dimensionId;
        return new File(worldDir, dim);
    }

    public static void writeData(World world, DimData data) throws IOException {
        if (data == null || data.getData() == null) return;

        File saveFile = new File(getDimFolder(world), "cointcontrolcnuks.json");
        if (!saveFile.getParentFile()
            .exists())
            saveFile.getParentFile()
                .mkdirs();

        Map<String, Map<Integer, Integer>> rawMap = new HashMap<>();

        for (Map.Entry<String, chunkdata> e : data.getData()
            .entrySet()) {
            String key = e.getKey();
            chunkdata cd = e.getValue();
            Map<Integer, Integer> lst = new HashMap<>();
            if (cd != null && cd.restrictions != null) {
                lst.putAll(cd.restrictions);
            }
            rawMap.put(key, lst);
        }

        try (FileWriter writer = new FileWriter(saveFile)) {
            GSON.toJson(rawMap, writer);
        }
    }

    public static DimData readData(World world) {
        File saveFile = new File(getDimFolder(world), "cointcontrolcnuks.json");
        if (!saveFile.exists()) return null;

        try (FileReader fileReader = new FileReader(saveFile)) {
            Object raw = GSON.fromJson(fileReader, Object.class);
            if (!(raw instanceof Map)) return null;

            Map<?, ?> rawmap = (Map<?, ?>) raw;
            Map<String, chunkdata> result = new HashMap<>();

            for (Map.Entry<?, ?> e : rawmap.entrySet()) {
                String key = String.valueOf(e.getKey());
                Object value = e.getValue();
                Map<Integer, Integer> mapa = new HashMap<>();
                if (value instanceof Map) {
                    Map<?, ?> rawMap = (Map<?, ?>) value;
                    for (Map.Entry<?, ?> el : rawMap.entrySet()) {
                        try {
                            Integer keya = Integer.parseInt(String.valueOf(el.getKey()));
                            Integer vala = Integer.parseInt(String.valueOf(el.getValue()));
                            mapa.put(keya, vala);
                        } catch (NumberFormatException ignored) {}
                    }
                }
                result.put(key, new chunkdata(mapa));
            }

            return new DimData(result);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
