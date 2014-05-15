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

import com.matthewprenger.servertools.core.ServerTools;
import com.matthewprenger.servertools.core.command.corecommands.*;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class CommandManager {

    private static final String ENABLE_COMMAND_CONFIG_CATEGORY = "enableCommand";
    private static final String COMMAND_NAME_CONFIG_CATEGORY = "commandName";

    private static final Configuration commandConfig = new Configuration(new File(ServerTools.serverToolsDir, "command.cfg"));

    static {
        commandConfig.load();

        commandConfig.addCustomCategoryComment(ENABLE_COMMAND_CONFIG_CATEGORY, "Allows you to disable any command registered with ServerTools");
        commandConfig.addCustomCategoryComment(COMMAND_NAME_CONFIG_CATEGORY, "Allows you to rename any command registered with ServerTools");

        if (commandConfig.hasChanged()) commandConfig.save();
    }

    private static final Set<ServerToolsCommand> commandsToLoad = new HashSet<>();

    private static boolean commandsLoaded = false;

    /**
     * Registers a command with ServerTools
     *
     * @param command A command that extends ServerToolsCommand
     */
    public static void registerSTCommand(ServerToolsCommand command) {

        if (commandsLoaded) {
            throw new IllegalStateException("Tried to register ServerTools Command after FMLServerStarting Event");
        }

        boolean enableCommand = commandConfig.get("enableCommand", command.getClass().getName(), true).getBoolean(true);
        command.name = commandConfig.get("commandName", command.getClass().getName(), command.defaultName).getString();

        if (enableCommand) {
            commandsToLoad.add(command);
        }

        if (commandConfig.hasChanged()) {
            commandConfig.save();
        }

    }

    public static void registerCommands(CommandHandler commandHandler) {

        for (ServerToolsCommand command : commandsToLoad) {
            ServerTools.log.debug(String.format("Command: %s , has name: %s", command.getClass(), command.name));
            ServerTools.log.info("Registering Command: " + command.name);
            commandHandler.registerCommand(command);
        }

        commandsLoaded = true;
    }

    public static void onServerStopped() {

        commandsLoaded = false;
        commandsToLoad.clear();
    }

    public static void initCoreCommands() {

        registerSTCommand(new CommandMotd("motd"));
        registerSTCommand(new CommandVoice("voice"));
        registerSTCommand(new CommandSilence("silence"));
        registerSTCommand(new CommandDisarm("disarm"));
        registerSTCommand(new CommandEntityCount("entitycount"));
        registerSTCommand(new CommandHeal("heal"));
        registerSTCommand(new CommandInventory("inventory"));
        registerSTCommand(new CommandKillPlayer("killplayer"));
        registerSTCommand(new CommandKillAll("killall"));
        registerSTCommand(new CommandReloadMotd("reloadmotd"));
        registerSTCommand(new CommandSpawnMob("spawnmob"));
        registerSTCommand(new CommandWhereIs("whereis"));
        registerSTCommand(new CommandTPS("tps"));
        registerSTCommand(new CommandRemoveAll("removeall"));
        registerSTCommand(new CommandMemory("memory"));
        registerSTCommand(new CommandPing("ping"));
    }

    public static boolean areCommandsLoaded() {

        return commandsLoaded;
    }
}
