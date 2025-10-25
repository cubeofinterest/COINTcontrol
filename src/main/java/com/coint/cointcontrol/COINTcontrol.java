package com.coint.cointcontrol;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = COINTcontrol.MODID, version = Tags.VERSION, name = "COINTcontrol", acceptedMinecraftVersions = "[1.7.10]")
public class COINTcontrol {

    public static final String MODID = "cointcontrol";
    public static final Logger LOG = LogManager.getLogger(MODID);
    public static final Map<World, DimData> DATA = new HashMap<>();
    public static final DimDataHelper datahelper = new DimDataHelper();
    @SidedProxy(clientSide = "com.coint.cointcontrol.ClientProxy", serverSide = "com.coint.cointcontrol.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new AddBlockCommand());
        event.registerServerCommand(new ListBlocksCommand());
        event.registerServerCommand(new RemoveBlockCommand());
        proxy.serverStarting(event);
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.PlaceEvent event) {
        final EntityPlayer player = event.player;
        final World w = event.world;
        final int x = event.x;
        final int y = event.y;
        final int z = event.z;
        final int id = Block.getIdFromBlock(event.block);
        if (!Config.getBlocks()
            .containsKey(id)) return;
        final String chunkKey = (int) (x / 16) + "," + (int) (z / 16);
        datahelper.AddToChunk(x, z, w, id, player);

        DimData dimData = COINTcontrol.DATA.get(w);

        chunkdata chunk = dimData.getData()
            .get(chunkKey);

        Map<Integer, Integer> map = chunk.getData();
        if (map == null) {
            map = new HashMap<Integer, Integer>();
            try {
                chunk.restrictions = map;
            } catch (Throwable t) {
                LOG.debug("onBlockPlaced: cannot setData on chunk, continuing with local map");
            }
        }

        int max = Config.getBlocks()
            .get(id);

        player.addChatMessage(new ChatComponentText(String.format("%d/%d", map.get(id), max)));
        if (map.get(id) > max) {
            int meta = w.getBlockMetadata(x, y, z);
            event.block.dropBlockAsItem(w, x, y, z, meta, 0);
            w.setBlockToAir(x, y, z);
            map.put(id, Math.max(map.getOrDefault(id, 0) - 1, 0));
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        int blockid = Block.getIdFromBlock(event.block);
        if (!Config.getBlocks()
            .containsKey(blockid)) return;
        datahelper.RemoveFromChunk(event, blockid);
    }

    @Mod.EventHandler
    public void onServerStopping(FMLServerStoppingEvent event) throws IOException {
        if (event == null) return;
        for (Object o : MinecraftServer.getServer().worldServers) {
            WorldServer ws = (WorldServer) o;
            if (COINTcontrol.DATA.containsKey(ws)) {
                DimCache.writeData(ws, COINTcontrol.DATA.get(ws));
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) throws IOException {
        World world = event.world;
        if (world == null || world.isRemote) return;
        if (COINTcontrol.DATA.containsKey(world)) {
            DimCache.writeData(world, COINTcontrol.DATA.get(world));

        }
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event) throws IOException {
        World world = event.world;
        if (world == null || world.isRemote) return;
        if (COINTcontrol.DATA.containsKey(world)) {
            DimData data = COINTcontrol.DATA.get(world);
            DimCache.writeData(world, data);
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        MinecraftServer server = MinecraftServer.getServer();
        for (WorldServer world : server.worldServers) {
            COINTcontrol.DATA.put(world, DimCache.readData(world));
        }
        File configFile = new File("config/cointcontrol.cfg");
        Config.synchronizeConfiguration(configFile);

    }

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        proxy.preInit(event);

    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

}
