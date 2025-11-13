package com.coint.cointcontrol;

import static com.coint.cointcontrol.COINTcontrol.toolTips;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class AddBlockCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "addblock";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/addblock <blockID> <count>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.addChatMessage(new ChatComponentText("usage: /addblock <blockID> <count>"));
            return;
        }

        try {
            int blockID = Integer.parseInt(args[0]);
            int count = Integer.parseInt(args[1]);

            Config.addBlock(blockID, count);
            sender.addChatMessage(new ChatComponentText("block " + blockID + " is set to " + count));

            toolTips.put(blockID, "You can place only " + count + " blocks of this type in one chunk");
            PacketHandler.INSTANCE.sendToAll(new PacketToolTip(toolTips));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText("format error"));
        }
    }
}
