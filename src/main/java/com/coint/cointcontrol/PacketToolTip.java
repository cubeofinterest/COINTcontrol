package com.coint.cointcontrol;

import static com.coint.cointcontrol.COINTcontrol.LOG;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketToolTip implements IMessage {

    private Map<Integer, String> dataMap;

    public PacketToolTip() {}

    public PacketToolTip(Map<Integer, String> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();

        dataMap = new HashMap<Integer, String>();

        for (int i = 0; i < size; i++) {
            int key = buf.readInt();
            String value = ByteBufUtils.readUTF8String(buf);
            dataMap.put(key, value);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.dataMap.size());
        for (Map.Entry<Integer, String> entry : this.dataMap.entrySet()) {
            buf.writeInt(entry.getKey());
            ByteBufUtils.writeUTF8String(buf, entry.getValue());
        }
    }

    public static class Handler implements IMessageHandler<PacketToolTip, IMessage> {

        @Override
        public IMessage onMessage(PacketToolTip message, MessageContext ctx) {

            ClientProxy.clientToolTips.clear();
            ClientProxy.clientToolTips.putAll(message.dataMap);
            LOG.info("Sending " + ClientProxy.clientToolTips + " tooltips to clients");

            return null;
        }
    }
}
