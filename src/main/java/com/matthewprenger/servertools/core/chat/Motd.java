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
import com.matthewprenger.servertools.core.lib.Reference;
import com.matthewprenger.servertools.core.lib.Strings;
import com.matthewprenger.servertools.core.util.FileUtils;
import com.matthewprenger.servertools.core.util.LogHelper;
import com.matthewprenger.servertools.core.util.Util;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Motd {

    private Collection<String> motd = new ArrayList<>();
    private final File motdFile;

    public Motd(File motdFile) {

        this.motdFile = motdFile;
        loadMotd();
        FMLCommonHandler.instance().bus().register(this);
    }

    public void loadMotd() {

        if (!motdFile.exists()) {
            try (Writer out = new OutputStreamWriter(new FileOutputStream(motdFile), Reference.FILE_ENCODING)) {
                for (String line : Strings.MOTD_DEFAULT) {
                    out.write(line);
                    out.write(System.lineSeparator());
                }
            } catch (IOException e) {
                LogHelper.log(Level.WARN, "Failed to create default MOTD", e);
            }

            Collections.addAll(motd, Strings.MOTD_DEFAULT);
        } else {
            try {
                motd = FileUtils.readFileToString(motdFile);
            } catch (IOException e) {
                LogHelper.log(Level.ERROR, "Failed to read MOTD from file", e);
            }
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
