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

import static net.minecraft.util.EnumChatFormatting.*;

public class CommandMemory extends ServerToolsCommand {

    public CommandMemory(String defaultName) {
        super(defaultName);
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 3;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {

        return "/" + name;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        long totalMemory = Runtime.getRuntime().totalMemory() / 1048576L;
        long freeMemory = Runtime.getRuntime().freeMemory() / 1048576L;
        long usedMemory = totalMemory - freeMemory;

        sender.addChatMessage(Util.getChatComponent(String.format("%s%s MB%s out of %s%s MB%s Used", AQUA, usedMemory, RESET, AQUA, totalMemory, RESET), WHITE));
    }
}
