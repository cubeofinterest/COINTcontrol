package com.coint.cointcontrol;

import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ListBlocksCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "listblocks";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/listblocks";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Map<Integer, Integer> blocks = Config.getBlocks();
        if (blocks.isEmpty()) {
            sender.addChatMessage(new ChatComponentText("Блоков нет"));
            return;
        }
        sender.addChatMessage(new ChatComponentText("Блоки:"));
        for (Map.Entry<Integer, Integer> e : blocks.entrySet()) {
            sender.addChatMessage(new ChatComponentText("ID " + e.getKey() + " → " + e.getValue()));
        }
    }
}
