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

import com.matthewprenger.servertools.core.command.ServerToolsCommand;
import com.matthewprenger.servertools.core.util.Util;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CommandWhereIs extends ServerToolsCommand {

    public CommandWhereIs(String defaultName) {
        super(defaultName);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {

        return par2 == 0;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {

        return par2ArrayOfStr.length >= 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " [username]";
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) {

        if (astring.length < 1)
            throw new WrongUsageException(getCommandUsage(icommandsender));

        EntityPlayerMP player = getPlayer(icommandsender, astring[0]);
        NumberFormat f = new DecimalFormat("#");

        String str = String.format("Player: %s is at X:%s Y:%s Z:%s in Dim:%s", player.getDisplayName(),
                f.format(player.posX), f.format(player.posY), f.format(player.posZ),
                player.worldObj.provider.dimensionId + "-" + player.worldObj.provider.getDimensionName());

        icommandsender.addChatMessage(Util.getChatComponent(str, EnumChatFormatting.WHITE));
    }
}
