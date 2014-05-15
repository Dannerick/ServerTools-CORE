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

import com.matthewprenger.servertools.core.CoreConfig;
import com.matthewprenger.servertools.core.ServerTools;
import com.matthewprenger.servertools.core.lib.Reference;
import com.matthewprenger.servertools.core.lib.Strings;
import com.matthewprenger.servertools.core.util.Util;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Motd {

    private static final String FILE_ENCODING = "UTF-8";

    private String[] motd;
    private final File motdFile;

    public Motd(File motdFile) {

        this.motdFile = motdFile;
        loadMotd();
        FMLCommonHandler.instance().bus().register(this);
    }

    public void loadMotd() {

        try {
            if (!motdFile.exists()) {
                Writer out = new OutputStreamWriter(new FileOutputStream(motdFile), FILE_ENCODING);

                for (String line : Strings.MOTD_DEFAULT) {
                    out.write(line);
                    out.write(Reference.LINE_SEPARATOR);
                }
                out.close();
            }

            BufferedReader reader = new BufferedReader(new FileReader(motdFile));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
            motd = lines.toArray(new String[lines.size()]);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            ServerTools.log.warn("Unable to read the MOTD from file");
        }
    }

    public void serveMotd(EntityPlayer player) {

        for (String line : motd) {
            line = line.replace("$PLAYER$", player.getDisplayName());
            player.addChatComponentMessage(Util.getChatComponent(line, EnumChatFormatting.WHITE));
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if (CoreConfig.SEND_MOTD_ON_LOGIN) {
            serveMotd(event.player);
        }
    }
}
