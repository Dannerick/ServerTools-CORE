package com.matthewprenger.servertools.core.chat;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.matthewprenger.servertools.core.util.FileUtils;
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
            try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
                Type type = new TypeToken<Map<UUID, String>>() {
                }.getType();
                nickMap = gson.fromJson(reader, type);
            } catch (JsonSyntaxException e) {
                LogHelper.log(Level.WARN, "Failed to parse nickname savefile as valid JSON", e);
            } catch (IOException e) {
                LogHelper.log(Level.WARN, "Failed to load nickname savefile", e);
            }
        } else {
            save();
        }
    }

    public void save() {
        try {
            FileUtils.writeStringToFile(gson.toJson(nickMap), saveFile);
        } catch (IOException e) {
            LogHelper.log(Level.WARN, "Failed to save nickname map", e);
        }
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
