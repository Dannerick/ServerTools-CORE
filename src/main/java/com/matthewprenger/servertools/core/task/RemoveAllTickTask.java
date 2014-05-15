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

package com.matthewprenger.servertools.core.task;

import com.matthewprenger.servertools.core.ServerTools;
import com.matthewprenger.servertools.core.util.Util;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class RemoveAllTickTask implements ITickTask {

    private static final int BLOCKS_PER_TICK = 500;
    private static final int LAG_THREASHOLD = 1500;

    private boolean isComplete;
    private final EntityPlayer player;
    private Collection<TempBlock> blocksToRemove;
    private final World world;
    private int blockCounter;

    public RemoveAllTickTask(EntityPlayer player, int radius, Collection<Block> blocksToClear) {

        this.player = player;
        world = player.worldObj;

        if (world == null) {
            ServerTools.log.warn(String.format("Player: %s tried to start a removeall task, but their worldObj was null", player.getDisplayName()));
            isComplete = true;
            return;
        }

        int centerX = (int) player.posX;
        int centerY = (int) player.posY;
        int centerZ = (int) player.posZ;

        blocksToRemove = new ArrayList<>();

        for (int x = centerX - radius; x < centerX + radius; x++) {
            for (int y = centerY - radius; y < centerY + radius; y++) {
                for (int z = centerZ - radius; z < centerZ + radius; z++)
                    if (blocksToClear.contains(world.getBlock(x, y, z))) {
                        blocksToRemove.add(new TempBlock(x, y, z));
                        blockCounter++;
                    }
            }
        }

        player.addChatComponentMessage(Util.getChatComponent(String.format("Removing %s blocks", blockCounter), EnumChatFormatting.GOLD));

        if (blockCounter > LAG_THREASHOLD)
            player.addChatComponentMessage(Util.getChatComponent("Removing a lot of blocks, Incomming lag", EnumChatFormatting.RED));
    }

    @Override
    public void tick() {

        Iterator<TempBlock> iterator = blocksToRemove.iterator();

        for (int i = 0; i < BLOCKS_PER_TICK; i++) {

            if (iterator.hasNext()) {
                TempBlock block = iterator.next();
                world.setBlock(block.x, block.y, block.z, Blocks.air, 0, 2);
                iterator.remove();
            }
        }

        if (blocksToRemove.isEmpty()) {
            isComplete = true;
        }
    }

    @Override
    public void onComplete() {

        player.addChatComponentMessage(Util.getChatComponent("Finished removing blocks", EnumChatFormatting.GREEN));
    }

    @Override
    public boolean isComplete() {

        return isComplete;
    }

    /**
     * Used to reference an actaul block in world
     */
    private class TempBlock {
        final int x;
        final int y;
        final int z;

        TempBlock(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
