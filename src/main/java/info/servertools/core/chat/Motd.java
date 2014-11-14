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
package info.servertools.core.chat;

import com.google.common.io.Files;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import info.servertools.core.CoreConfig;
import info.servertools.core.ServerTools;
import info.servertools.core.lib.Reference;
import info.servertools.core.lib.Strings;
import info.servertools.core.util.ChatUtils;
import net.minecraft.entity.player.EntityPlayer;

import java.io.*;

public class Motd {

    private String motd;
    private final File motdFile;

    public Motd(File motdFile) {
        this.motdFile = motdFile;
        loadMotd();
        FMLCommonHandler.instance().bus().register(this);
    }

    public void loadMotd() {
        synchronized (motdFile) {
            try {
                if (!motdFile.exists()) {
                    Files.write(Strings.MOTD_DEFAULT, motdFile, Reference.CHARSET);
                    this.motd = Strings.MOTD_DEFAULT;
                } else {
                    motd = Files.toString(motdFile, Reference.CHARSET);
                }
            } catch (IOException e) {
                ServerTools.LOG.warn("Failed to read MOTD from disk", e);
            }
        }
    }

    public void serveMotd(EntityPlayer player) {
        for (String line : motd.split(Reference.LINE_SEPARATOR)) {
            line = line.replace("$PLAYER$", player.getDisplayName());
            player.addChatComponentMessage(ChatUtils.getChatComponent(line));
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (CoreConfig.SEND_MOTD_ON_LOGIN) {
            serveMotd(event.player);
        }
    }
}
