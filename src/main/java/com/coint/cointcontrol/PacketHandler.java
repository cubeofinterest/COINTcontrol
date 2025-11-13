package com.coint.cointcontrol;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {

    public static SimpleNetworkWrapper INSTANCE;
    public static SimpleNetworkWrapper INSTANCEB;
    private static int packetid = 0;
    private static int packetbid = 0;

    public static void init() {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(COINTcontrol.MODID);
        INSTANCE.registerMessage(PacketToolTip.Handler.class, PacketToolTip.class, packetid++, Side.CLIENT);

        INSTANCEB = NetworkRegistry.INSTANCE.newSimpleChannel(COINTcontrol.MODID + "b");
        INSTANCEB.registerMessage(PacketOverlay.Handler.class, PacketOverlay.class, packetbid++, Side.CLIENT);
    }
}
