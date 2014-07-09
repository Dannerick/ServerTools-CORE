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

import com.matthewprenger.servertools.core.STVersion;
import com.matthewprenger.servertools.core.command.CommandLevel;
import com.matthewprenger.servertools.core.command.ServerToolsCommand;
import com.matthewprenger.servertools.core.lib.Reference;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

import java.util.Collections;
import java.util.List;

public class CommandServerTools extends ServerToolsCommand {

    public CommandServerTools(String defaultName) {
        super(defaultName);
    }

    @Override
    public CommandLevel getCommandLevel() {
        return CommandLevel.ANYONE;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + name;
    }

    @Override
    public List getCommandAliases() {

        return Collections.singletonList("st");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        addChatMessage(sender, "ServerTools-CORE Version: " + STVersion.VERSION, EnumChatFormatting.GRAY);
        addChatMessage(sender, "For Minecraft: " + STVersion.MCVERSIONN, EnumChatFormatting.GRAY);
        addChatMessage(sender, "By: " + Reference.AUTHORS, EnumChatFormatting.GRAY);
    }
}
