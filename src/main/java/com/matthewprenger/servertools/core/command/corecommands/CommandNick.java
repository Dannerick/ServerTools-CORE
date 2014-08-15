package com.matthewprenger.servertools.core.command.corecommands;

import com.matthewprenger.servertools.core.chat.NickHandler;
import com.matthewprenger.servertools.core.command.CommandLevel;
import com.matthewprenger.servertools.core.command.ServerToolsCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class CommandNick extends ServerToolsCommand {

    public CommandNick(String defaultName) {
        super(defaultName);
    }

    @Override
    public CommandLevel getCommandLevel() {
        return CommandLevel.ANYONE;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + name + " {nickname}";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        EntityPlayer player = getCommandSenderAsPlayer(sender);

        if (args.length == 0) {
            NickHandler.instance.setNick(player, player.getGameProfile().getName());
            addChatMessage(sender, "Removed nickname", EnumChatFormatting.GOLD);
        } else if (args.length == 1) {
            NickHandler.instance.setNick(player, args[0]);
            addChatMessage(sender, "Set nick to: " + args[0], EnumChatFormatting.GOLD);
        } else {
            throw new WrongUsageException(getCommandUsage(sender));
        }
    }
}
