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

package com.matthewprenger.servertools.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public abstract class ServerToolsCommand extends CommandBase {

    public String name;
    public final String defaultName;

    public ServerToolsCommand(String defaultName) {

        this.defaultName = defaultName;
    }

    @Override
    public final String getCommandName() {

        return name;
    }

    @Override
    public abstract int getRequiredPermissionLevel();

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {

        MinecraftServer server = MinecraftServer.getServer();

        return !(sender instanceof EntityPlayerMP) || this.getRequiredPermissionLevel() <= 1 ||
                server.getConfigurationManager().isPlayerOpped(sender.getCommandSenderName()) &&
                        server.getOpPermissionLevel() >= this.getRequiredPermissionLevel();

    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Object o) {
        if (o instanceof ICommand) {
            return this.compareTo((ICommand) o);
        } else {
            return 0;
        }
    }
}
