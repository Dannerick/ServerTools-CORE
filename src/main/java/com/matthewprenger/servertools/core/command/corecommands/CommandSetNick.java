/*
 * Copyright 2014 Matthew Prenger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
