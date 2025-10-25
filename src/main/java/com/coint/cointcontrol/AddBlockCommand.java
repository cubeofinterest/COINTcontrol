package com.coint.cointcontrol;

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
            sender.addChatMessage(new ChatComponentText("Использование: /addblock <blockID> <count>"));
            return;
        }

        try {
            int blockID = Integer.parseInt(args[0]);
            int count = Integer.parseInt(args[1]);

            Config.addBlock(blockID, count);
            sender.addChatMessage(new ChatComponentText("Блок " + blockID + " установлен на " + count));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText("Неверные числа"));
        }
    }
}
