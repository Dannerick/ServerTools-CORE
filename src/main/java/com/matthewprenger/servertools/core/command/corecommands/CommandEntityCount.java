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
import com.matthewprenger.servertools.core.lib.Strings;
import com.matthewprenger.servertools.core.util.Util;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandEntityCount extends ServerToolsCommand {

    public CommandEntityCount(String defaultName) {
        super(defaultName);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] par2ArrayOfStr) {

        return par2ArrayOfStr.length >= 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, (String[]) EntityList.classToStringMapping.values().toArray()) : null;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " {dimension} {entityname}";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (!(sender instanceof EntityPlayer))
            throw new WrongUsageException(Strings.COMMAND_ERROR_ONLYPLAYER);

        World world = ((EntityPlayer) sender).worldObj;

        Map<String, Integer> entityCount = new HashMap<>();

        for (Object obj : world.loadedEntityList) {
            if (obj instanceof Entity) {
                String name = EntityList.getEntityString((Entity) obj);
                if (name != null) {
                    if (entityCount.containsKey(name))
                        entityCount.put(name, entityCount.get(name) + 1);
                    else
                        entityCount.put(name, 1);
                }
            }
        }

        if (args.length == 0) {
            int count = 0;
            for (Map.Entry<String, Integer> entry : entityCount.entrySet()) {
                count += entry.getValue();
            }

            sender.addChatMessage(Util.getChatComponent("Entity Count: " + count, EnumChatFormatting.WHITE));
        } else if ("all".equalsIgnoreCase(args[0])) {

            sender.addChatMessage(Util.getChatComponent("Entity Count:", EnumChatFormatting.WHITE));

            for (Map.Entry<String, Integer> entry : entityCount.entrySet()) {
                sender.addChatMessage(Util.getChatComponent(String.format("  %s: %s", entry.getKey(), entry.getValue()), EnumChatFormatting.WHITE));
            }
        } else {

            int count = 0;
            String name = null;
            String pname = func_82360_a(sender, args, 0);
            for (Object obj : EntityList.stringToClassMapping.keySet()) {
                if (obj.toString().equalsIgnoreCase(pname)) name = obj.toString();
            }

            if (name == null) throw new PlayerNotFoundException(Strings.COMMAND_ERROR_ENTITY_NOEXIST);

            for (Object obj : world.loadedEntityList) {
                if (obj instanceof Entity) {
                    Entity ent = (Entity) obj;

                    String string = EntityList.getEntityString(ent);
                    if (string != null && string.equalsIgnoreCase(name)) {
                        count++;
                    }
                }
            }

            sender.addChatMessage(Util.getChatComponent(String.format("Entity: %s, Count: %s", name, count), EnumChatFormatting.WHITE));
        }

    }
}
