/*
 * Copyright 2014 ServerTools
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
package info.servertools.core.command.corecommands;

import info.servertools.core.chat.NickHandler;
import info.servertools.core.command.CommandLevel;
import info.servertools.core.command.ServerToolsCommand;
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
