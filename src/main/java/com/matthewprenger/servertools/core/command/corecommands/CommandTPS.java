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
import com.matthewprenger.servertools.core.util.Util;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.text.DecimalFormat;

public class CommandTPS extends ServerToolsCommand {

    private static final DecimalFormat timeFormatter = new DecimalFormat("########0.000");

    public CommandTPS(String defaultName) {
        super(defaultName);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length < 1) {

            calculateTpsSummary(sender);
        } else if (args.length == 1) {

            if (args[0].contains("all")) {
                calculateAllDimTps(sender);
            } else {
                int dimId = parseInt(sender, args[0]);
                calculateDimTps(sender, dimId);
            }
        } else {
            throw new WrongUsageException(getCommandUsage(sender));
        }
    }

    private static void calculateDimTps(ICommandSender sender, int dimId) {

        World world = MinecraftServer.getServer().worldServerForDimension(dimId);

        if (world == null) throw new PlayerNotFoundException("That Dimension Doesn't Exist!");

        double worldTickTime = mean(MinecraftServer.getServer().worldTickTimes.get(dimId)) * 1.0E-6D;
        double worldTPS = Math.min(1000.0 / worldTickTime, 20);

        sendToChat(sender, EnumChatFormatting.GREEN + "Dimension TPS Report" + EnumChatFormatting.RESET);
        sendToChat(sender, String.format("DIM %s: Mean Tick Time: %s ms", dimId, timeFormatter.format(worldTickTime)));
        sendToChat(sender, String.format("DIM %s: Mean TPS: %s", dimId, timeFormatter.format(worldTPS)));
        sendToChat(sender, String.format("   Loaded Chunks: %s", world.getChunkProvider().getLoadedChunkCount()));
        sendToChat(sender, String.format("   Loaded Entities: %s", world.loadedEntityList.size()));
        sendToChat(sender, String.format("   Loaded Tile Entites: %s", world.loadedTileEntityList.size()));
        sendToChat(sender, String.format("   Players Online: %s", world.playerEntities.size()));
    }

    private static void calculateAllDimTps(ICommandSender sender) {

        sendToChat(sender, EnumChatFormatting.GREEN + "All Dimensions TPS Report" + EnumChatFormatting.RESET);

        for (Integer dim : DimensionManager.getIDs()) {

            double worldTickTime = mean(MinecraftServer.getServer().worldTickTimes.get(dim)) * 1.0E-6D;
            double worldTPS = Math.min(1000.0 / worldTickTime, 20);

            sendToChat(sender, String.format("DIM: %s: Mean Tick Time: %s ms | Mean TPS: %s", dim, timeFormatter.format(worldTickTime), timeFormatter.format(worldTPS)));
        }
    }

    private static void calculateTpsSummary(ICommandSender sender) {

        sendToChat(sender, EnumChatFormatting.GREEN + "Overall TPS Report" + EnumChatFormatting.RESET);

        double meanTickTime = mean(MinecraftServer.getServer().tickTimeArray) * 1.0E-6D;
        double meanTPS = Math.min(1000.0 / meanTickTime, 20);

        sendToChat(sender, String.format("Server Mean Tick Time: %s ms", timeFormatter.format(meanTickTime)));
        sendToChat(sender, String.format("Server Mean TPS: %s", timeFormatter.format(meanTPS)));
    }

    private static long mean(long[] values) {

        long sum = 0l;
        for (long v : values) {
            sum += v;
        }

        return sum / values.length;
    }

    @Override
    public String getCommandUsage(ICommandSender var1) {
        return "/" + name + " [ \"all\" | dimID ]";
    }

    private static void sendToChat(ICommandSender sender, String string) {

        sender.addChatMessage(Util.getChatComponent(string, EnumChatFormatting.WHITE));
    }
}
