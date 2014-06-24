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

package com.matthewprenger.servertools.core.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentStyle;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Util {

    /**
     * Get a ChatComponent to send to an ICommandSender
     *
     * @param message    the message to add to the component
     * @param formatting any formatting to add
     * @return the component
     */
    public static ChatComponentStyle getChatComponent(Object message, EnumChatFormatting formatting) {

        ChatComponentText componentText = new ChatComponentText(message.toString());
        componentText.getChatStyle().setColor(formatting);
        return componentText;
    }

    /**
     * Check if a string matches a username
     *
     * @param string a string
     * @return {@code true} if the string matches a username
     */
    public static boolean matchesPlayer(String string) {

        for (String user : MinecraftServer.getServer().getAllUsernames())
            if (user.equalsIgnoreCase(string))
                return true;

        return false;
    }

    public static void teleportPlayer(EntityPlayerMP entityPlayer, Location location) {

        if (entityPlayer.worldObj.provider.dimensionId != location.dimID)
            entityPlayer.travelToDimension(location.dimID);

        entityPlayer.setPositionAndUpdate(location.x, location.y, location.z);
    }
}
