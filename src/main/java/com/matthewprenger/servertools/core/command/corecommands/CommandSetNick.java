package com.matthewprenger.servertools.core.command.corecommands;

import com.matthewprenger.servertools.core.chat.NickHandler;
import com.matthewprenger.servertools.core.command.CommandLevel;
import com.matthewprenger.servertools.core.command.ServerToolsCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CommandSetNick extends ServerToolsCommand {

    public CommandSetNick(String defaultName) {
        super(defaultName);
    }

    @Override
    public CommandLevel getCommandLevel() {
        return CommandLevel.OP;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + name + " [username] {nickname}";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length == 1) {
            EntityPlayer player = getPlayer(sender, args[0]);
            NickHandler.instance.setNick(player, player.getGameProfile().getName());
        } else if (args.length == 2) {
            EntityPlayer player = getPlayer(sender, args[0]);
            NickHandler.instance.setNick(player, args[1]);
        } else {
            throw new WrongUsageException(getCommandUsage(sender));
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {

        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
        }

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }
}
