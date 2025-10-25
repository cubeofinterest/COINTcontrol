package com.coint.cointcontrol;

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
            sender.addChatMessage(new ChatComponentText("Использование: /removeblock <blockID>"));
            return;
        }

        try {
            int blockID = Integer.parseInt(args[0]);
            Config.removeBlock(blockID);
            sender.addChatMessage(new ChatComponentText("Блок " + blockID + " удалён"));
        } catch (NumberFormatException e) {
            sender.addChatMessage(new ChatComponentText("Неверный ID блока"));
        }
    }
}
