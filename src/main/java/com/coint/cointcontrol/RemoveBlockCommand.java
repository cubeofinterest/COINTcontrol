package com.coint.cointcontrol;

import static com.coint.cointcontrol.COINTcontrol.toolTips;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class RemoveBlockCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "removeblock";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/removeblock <blockID>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.addChatMessage(new ChatComponentText("usage: /removeblock <blockID>"));
            return;
        }

        try {
            int blockID = Integer.parseInt(args[0]);
            Config.removeBlock(blockID);
            toolTips.remove(blockID);
            PacketHandler.INSTANCE.sendToAll(new PacketToolTip(toolTips));
            sender.addChatMessage(new ChatComponentText("block " + blockID + " removed from config"));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText("incorrect block id"));
        }
    }
}
