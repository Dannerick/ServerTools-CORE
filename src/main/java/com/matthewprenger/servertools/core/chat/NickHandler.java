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

package com.matthewprenger.servertools.core.chat;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.matthewprenger.servertools.core.util.GsonUtils;
import com.matthewprenger.servertools.core.util.LogHelper;
import com.matthewprenger.servertools.core.util.Util;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gnu.trove.map.hash.THashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.apache.logging.log4j.Level;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class NickHandler {

    private Map<UUID, String> nickMap = new THashMap<>();

    private File saveFile;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static final NickHandler instance = new NickHandler();

    public NickHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void init(File saveFile) {
        this.saveFile = saveFile;

        if (saveFile.exists()) {
            nickMap = GsonUtils.fromJson(saveFile, LogHelper.getLog());
        } else {
            save();
        }
    }

    public void save() {
        GsonUtils.writeToFile(nickMap, saveFile, LogHelper.getLog(), true);
    }

    @SubscribeEvent
    public void nameFormat(PlayerEvent.NameFormat event) {

        if (nickMap.containsKey(event.entityPlayer.getPersistentID())) {
            String nick = nickMap.get(event.entityPlayer.getPersistentID());

            event.displayname = event.displayname.replace(event.entityPlayer.getGameProfile().getName(), nick);
        }
    }

    public void setNick(EntityPlayer player, String nick) {
        nickMap.put(player.getPersistentID(), nick);
        player.refreshDisplayName();
        save();
        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(Util.getChatComponent(
                String.format("%s is now known as %s", player.getGameProfile().getName(), nick), EnumChatFormatting.GRAY));
    }
}
