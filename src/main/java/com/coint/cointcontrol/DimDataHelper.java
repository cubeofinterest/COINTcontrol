package com.coint.cointcontrol;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.BlockEvent;

public class DimDataHelper {

    public void CacheChunk(int xs, int zs, World w) {
        if (w == null) return;

        int xc = xs / 16;
        int zc = zs / 16;
        String chunkKey = xc + "," + zc;

        if (!COINTcontrol.DATA.containsKey(w) || COINTcontrol.DATA.get(w) == null)
            COINTcontrol.DATA.put(w, new DimData());

        if (!COINTcontrol.DATA.get(w)
            .getData()
            .containsKey(chunkKey)
            || COINTcontrol.DATA.get(w)
                .getData()
                .get(chunkKey) == null)
            COINTcontrol.DATA.get(w)
                .getData()
                .put(chunkKey, new chunkdata());

        Map<Integer, Integer> chunkData = COINTcontrol.DATA.get(w)
            .getData()
            .get(chunkKey)
            .getData();
        Chunk chunk = w.getChunkFromChunkCoords(xc, zc);
        if (chunk == null) return;

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < w.getHeight(); y++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    int id = Block.getIdFromBlock(block);
                    if (Config.getBlocks()
                        .containsKey(id)) {
                        chunkData.put(id, chunkData.getOrDefault(id, 0) + 1);
                    }
                }
            }
        }
    }

    public void RemoveFromChunk(BlockEvent.BreakEvent event, int id) {
        int x = event.x;
        int z = event.z;

        World w = event.world;

        final String chunkKey = (int) (x / 16) + "," + (int) (z / 16);
        if (

        !COINTcontrol.DATA.containsKey(w) || COINTcontrol.DATA.get(w) == null
            || COINTcontrol.DATA.get(w)
                .getData() == null
            || COINTcontrol.DATA.get(w)
                .getData()
                .get(chunkKey) == null) {
            COINTcontrol.datahelper.CacheChunk(x, z, w);
        }
        Map<Integer, Integer> map = COINTcontrol.DATA.get(w)
            .getData()
            .get(x / 16 + "," + z / 16)
            .getData();
        map.put(id, map.getOrDefault(id, 0) - 1);

    }

    public void AddToChunk(int x, int z, World w, int id, EntityPlayer player) {

        final String chunkKey = (int) (x / 16) + "," + (int) (z / 16);
        if (

        !COINTcontrol.DATA.containsKey(w) || COINTcontrol.DATA.get(w) == null
            || COINTcontrol.DATA.get(w)
                .getData() == null
            || COINTcontrol.DATA.get(w)
                .getData()
                .get(chunkKey) == null) {
            COINTcontrol.datahelper.CacheChunk(x, z, w);
        }
        Map<Integer, Integer> map = COINTcontrol.DATA.get(w)
            .getData()
            .get(x / 16 + "," + z / 16)
            .getData();
        map.put(id, map.getOrDefault(id, 0) + 1);

    }
}
