package com.coint.cointcontrol;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketOverlay implements IMessage {

    private String data;

    public PacketOverlay() {}

    public PacketOverlay(String data) {
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, data);
    }

    public static class Handler implements IMessageHandler<PacketOverlay, IMessage> {

        @Override
        public IMessage onMessage(PacketOverlay message, MessageContext ctx) {
            ClientProxy.currentMessage = message.data;
            ClientProxy.displayTicks = 40;
            ClientProxy.color = (byte) 0xFFFFFF;
            return null;
        }
    }
}
