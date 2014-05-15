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

import com.matthewprenger.servertools.core.ServerTools;
import com.matthewprenger.servertools.core.command.ServerToolsCommand;
import com.matthewprenger.servertools.core.lib.Strings;
import com.matthewprenger.servertools.core.util.Util;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class CommandVoice extends ServerToolsCommand {

    public CommandVoice(String defaultName) {
        super(defaultName);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return String.format("/%s [add|remove|reload} {username}", name);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] par2) {

        if (par2.length == 1) {
            return getListOfStringsMatchingLastWord(par2, "add", "remove", "reload");
        } else if (par2.length == 2) {
            if (!"reload".equalsIgnoreCase(par2[0]))
                return getListOfStringsMatchingLastWord(par2, MinecraftServer.getServer().getAllUsernames());
        }

        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int index) {

        return index == 1;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length >= 1) {

            if ("add".equalsIgnoreCase(args[0])) {
                if (args.length == 2) {
                    ServerTools.instance.voiceHandler.giveVoice(args[1]);
                    notifyAdmins(sender, String.format(Strings.COMMAND_VOICE_ADD, args[1]));
                } else
                    throw new WrongUsageException(getCommandUsage(sender));

            } else if ("remove".equalsIgnoreCase(args[0])) {
                if (args.length == 2) {
                    if (ServerTools.instance.voiceHandler.removeVoice(args[1])) {
                        notifyAdmins(sender, String.format(Strings.COMMAND_VOICE_REMOVE, args[1]));
                    } else {
                        sender.addChatMessage(Util.getChatComponent(Strings.COMMAND_VOICE_REMOVE_NOUSER, EnumChatFormatting.RED));
                    }
                } else
                    throw new WrongUsageException(getCommandUsage(sender));

            } else if ("reload".equalsIgnoreCase(args[0])) {

                ServerTools.instance.voiceHandler.loadVoiceList();
                notifyAdmins(sender, Strings.COMMAND_VOICE_RELOAD);
            } else
                throw new WrongUsageException(getCommandUsage(sender));
        } else
            throw new WrongUsageException(getCommandUsage(sender));
    }
}