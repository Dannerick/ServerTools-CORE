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

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matthewprenger.servertools.core.CoreConfig;
import com.matthewprenger.servertools.core.ServerTools;
import com.matthewprenger.servertools.core.lib.Strings;
import com.matthewprenger.servertools.core.util.FileUtils;
import com.matthewprenger.servertools.core.util.Util;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.command.server.CommandBroadcast;
import net.minecraft.command.server.CommandEmote;
import net.minecraft.command.server.CommandMessage;
import net.minecraft.command.server.CommandMessageRaw;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static net.minecraft.util.EnumChatFormatting.*;

public class VoiceHandler {

    private final File voiceFile;
    private final File silenceFile;
    private final Gson gson;
    private Set<String> voicedUsers;
    private Set<String> silencedUsers;

    public VoiceHandler() {

        voicedUsers = new HashSet<>();
        silencedUsers = new HashSet<>();

        voiceFile = new File(ServerTools.serverToolsDir, "voice.json");
        silenceFile = new File(ServerTools.serverToolsDir, "silence.json");

        gson = new GsonBuilder().setPrettyPrinting().create();

        loadSilenceList();
        loadVoiceList();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private static void refreshPlayerDisplayName(String username) {

        EntityPlayer player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(username);

        if (player != null)
            player.refreshDisplayName();
    }

    public boolean isUserVoiced(String username) {

        return voicedUsers.contains(username.toLowerCase());
    }

    public void giveVoice(String username) {

        voicedUsers.add(username.toLowerCase());
        saveVoiceList();
        refreshPlayerDisplayName(username);
    }

    public boolean removeVoice(String username) {

        if (voicedUsers.contains(username.toLowerCase())) {
            voicedUsers.remove(username.toLowerCase());
            saveVoiceList();
            refreshPlayerDisplayName(username);
            return true;
        }
        return false;
    }

    public boolean isUserSilenced(String username) {

        return silencedUsers.contains(username.toLowerCase());
    }

    public void silence(String username) {

        silencedUsers.add(username.toLowerCase());
        saveSilenceList();
    }

    public boolean removeSilence(String username) {

        if (silencedUsers.contains(username.toLowerCase())) {
            silencedUsers.remove(username.toLowerCase());
            saveSilenceList();
            return true;
        }
        return false;
    }

    public Set<String> getVoicedUsers() {

        return ImmutableSet.copyOf(voicedUsers);
    }

    public Set<String> getSilencedUsers() {

        return ImmutableSet.copyOf(silencedUsers);
    }

    public void saveVoiceList() {

        String gsonRepresentation = gson.toJson(voicedUsers);

        try {
            FileUtils.writeStringToFile(gsonRepresentation, voiceFile);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            ServerTools.log.warn(Strings.VOICE_SAVE_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadVoiceList() {

        if (!voiceFile.exists())
            return;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(voiceFile));
            voicedUsers = gson.fromJson(reader, HashSet.class);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            ServerTools.log.warn(Strings.VOICE_LOAD_ERROR);
        }
    }

    public void saveSilenceList() {

        String gsonRepresentation = gson.toJson(silencedUsers);

        try {
            FileUtils.writeStringToFile(gsonRepresentation, silenceFile);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            ServerTools.log.warn(Strings.SILENCE_SAVE_ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadSilenceList() {

        if (!silenceFile.exists())
            return;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(silenceFile));
            silencedUsers = gson.fromJson(reader, HashSet.class);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            ServerTools.log.warn(Strings.SILENCE_LOAD_ERROR);
        }
    }

    @SubscribeEvent
    public void nameFormat(PlayerEvent.NameFormat event) {

        if (!CoreConfig.COLOR_OP_CHAT_MESSAGE)
            return;

        if (MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(event.username)) {
            StringBuilder sb = new StringBuilder();
            sb.append(RED).append('[').append("OP").append("] ").append(RESET);
            event.displayname = sb.toString() + event.displayname;
        }

        if (isUserVoiced(event.username)) {
            StringBuilder sb = new StringBuilder();
            sb.append(BLUE).append('[').append('+').append("] ").append(RESET);
            event.displayname = sb.toString() + event.displayname;
        }

    }

    @SubscribeEvent
    public void serverChat(ServerChatEvent event) {

        if (isUserSilenced(event.username)) {
            event.setCanceled(true);
            event.player.addChatComponentMessage(Util.getChatComponent(Strings.ERROR_SILENCED, EnumChatFormatting.RED));
        }

    }

    @SubscribeEvent
    public void command(CommandEvent event) {

        if (isUserSilenced(event.sender.getCommandSenderName())) {
            if (event.command instanceof CommandBroadcast || event.command instanceof CommandMessage || event.command instanceof CommandEmote || event.command instanceof CommandMessageRaw) {

                event.setCanceled(true);
                event.sender.addChatMessage(Util.getChatComponent(Strings.ERROR_SILENCED, EnumChatFormatting.RED));
            }
        }
    }
}
