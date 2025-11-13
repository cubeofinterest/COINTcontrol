package com.coint.cointcontrol;

import java.io.File;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraftforge.common.config.Configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Config {

    public static String greeting = "Hello World";

    // JSON строка с глобальными блоками
    private static String blocksJson = "{}";

    // В памяти - карта blockID -> count
    public static ConcurrentHashMap<Integer, Integer> blocks = new ConcurrentHashMap<>();

    private static final Gson GSON = new Gson();

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?");
        blocksJson = configuration.getString("blocks", Configuration.CATEGORY_GENERAL, blocksJson, "JSON с блоками");

        // Парсим JSON в карту
        try {
            Type type = new TypeToken<ConcurrentHashMap<Integer, Integer>>() {}.getType();
            ConcurrentHashMap<Integer, Integer> map = GSON.fromJson(blocksJson, type);
            if (map != null) blocks = map;
        } catch (Exception e) {
            blocks = new ConcurrentHashMap<>();
        }

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    // Получить все блоки
    public static ConcurrentHashMap<Integer, Integer> getBlocks() {
        return blocks;
    }

    // Добавить или обновить блок
    public static void addBlock(int blockID, int count) {
        blocks.put(blockID, count);
        saveBlocks();
    }

    // Удалить блок
    public static void removeBlock(int blockID) {
        blocks.remove(blockID);
        saveBlocks();
    }

    // Сохраняем JSON обратно в конфиг
    private static void saveBlocks() {
        blocksJson = GSON.toJson(blocks);
        File configFile = new File("config/cointcontrol.cfg"); // путь к конфигу
        Configuration configuration = new Configuration(configFile);
        configuration.load();
        configuration.get(Configuration.CATEGORY_GENERAL, "blocks", "{}")
            .set(blocksJson);
        configuration.save();
    }
}
