package com.coint.cointcontrol;

import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ClientProxy extends CommonProxy {

    public static final ConcurrentHashMap<Integer, String> clientToolTips = new ConcurrentHashMap<>();
    public static int displayTicks = 0;
    public static String currentMessage = "";
    public static byte color;

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (clientToolTips.keySet()
            .contains(Item.getIdFromItem(event.itemStack.getItem()))) {
            event.toolTip.add(clientToolTips.get(Item.getIdFromItem(event.itemStack.getItem())));

            COINTcontrol.LOG.debug("Added tooltip for item " + Item.getIdFromItem(event.itemStack.getItem()));
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && displayTicks > 0) {
            displayTicks--;
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR) {
            return;
        }

        if (displayTicks <= 0 || currentMessage.isEmpty()) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        FontRenderer fontRenderer = mc.fontRenderer;

        int width = scaledRes.getScaledWidth();
        int height = scaledRes.getScaledHeight();

        int textWidth = fontRenderer.getStringWidth(currentMessage);
        int x = (width - textWidth) / 2;
        int y = height - 60;

        int alpha = 255;
        if (displayTicks < 10) {
            alpha = (int) (255 * (displayTicks / 10.0));
        }

        int finalColor = (alpha << 24) | (color & 0xFFFFFF);

        fontRenderer.drawStringWithShadow(currentMessage, x, y, finalColor);
    }
}
