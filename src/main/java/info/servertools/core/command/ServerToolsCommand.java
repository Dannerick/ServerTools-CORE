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
package info.servertools.core.command;

import info.servertools.core.util.ServerUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

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

    /**
     * Get the required access level to use this command
     *
     * @return the Access Level
     */
    public abstract CommandLevel getCommandLevel();

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {

        if (!(sender instanceof EntityPlayerMP))
            return true;

        EntityPlayerMP player = (EntityPlayerMP) sender;

        if (getCommandLevel().equals(CommandLevel.ANYONE))
            return true;

        if (getCommandLevel().equals(CommandLevel.OP) && ServerUtils.isOP(player.getGameProfile()))
            return true;

        return false;
    }

    @Override
    public int getRequiredPermissionLevel() {

        switch (getCommandLevel()) {
            case OP:
                return 4;
            case ANYONE:
                return 0;
            default:
                return 4;
        }
    }

    public static void addChatMessage(ICommandSender sender, Object message) {

        sender.addChatMessage(new ChatComponentText(String.valueOf(message)));
    }

    public static void addChatMessage(ICommandSender sender, Object message, EnumChatFormatting formatting) {

        ChatComponentText componentText = new ChatComponentText(String.valueOf(message));
        componentText.getChatStyle().setColor(formatting);
        sender.addChatMessage(componentText);
    }
}
