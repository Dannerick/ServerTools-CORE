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
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import java.util.Collections;
import java.util.List;

public class CommandInventory extends ServerToolsCommand {

    public CommandInventory(String defaultName) {
        super(defaultName);
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List getCommandAliases() {

        return Collections.singletonList("inv");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {

        return par2ArrayOfStr.length >= 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames()) : null;
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2) {

        return par2 == 0;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {

        return "/" + name + " {username}";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] astring) {

        if (!(sender instanceof EntityPlayerMP))
            throw new WrongUsageException(Strings.COMMAND_ERROR_ONLYPLAYER);

        EntityPlayerMP player;

        if (astring.length == 0)
            player = (EntityPlayerMP) sender;
        else
            player = getPlayer(sender, astring[0]);

        player.displayGUIChest(new InvPlayerWrapper((EntityPlayerMP) sender, player));
    }

    public static class InvPlayerWrapper implements IInventory {
        private final EntityPlayerMP viewer;
        private final EntityPlayer player;

        public InvPlayerWrapper(EntityPlayerMP viewer, EntityPlayer player) {
            this.viewer = viewer;
            this.player = player;
        }

        @Override
        public int getSizeInventory() {
            if (player == null || player.isDead) {
                viewer.closeScreen();
            }

            return 45;
        }

        @Override
        public ItemStack getStackInSlot(int var1) {
            if (player == null || player.isDead) {
                viewer.closeScreen();
                return null;
            }

            if (var1 >= 0 && var1 < 27) {
                return player.inventory.mainInventory[var1 + 9];
            } else if (var1 >= 27 && var1 < 36) {
                return player.inventory.mainInventory[var1 - 27];
            } else if (var1 >= 36 && var1 < 40) {
                return player.inventory.armorInventory[39 - var1];
            } else return null;
        }

        @Override
        public ItemStack decrStackSize(int i, int j) {
            if (player == null || player.isDead) {
                viewer.closeScreen();
                return null;
            }

            ItemStack stack = getStackInSlot(i);
            if (stack != null) {
                if (stack.stackSize <= j) {
                    setInventorySlotContents(i, null);
                    markDirty();
                    return stack;
                }
                ItemStack stack1 = stack.splitStack(j);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(i, null);
                }
                markDirty();
                return stack1;
            } else
                return null;
        }

        @Override
        public ItemStack getStackInSlotOnClosing(int var1) {
            if (player == null || player.isDead) {
                viewer.closeScreen();
                return null;
            }

            ItemStack stack = getStackInSlot(var1);
            if (stack != null) {
                setInventorySlotContents(var1, null);
                return stack;
            } else return null;
        }

        @Override
        public void setInventorySlotContents(int var1, ItemStack var2) {
            if (player == null || player.isDead) {
                viewer.entityDropItem(var2, 0.5F);
                viewer.closeScreen();
                return;
            }

            if (var1 >= 0 && var1 < 27) {
                player.inventory.mainInventory[var1 + 9] = var2;
            } else if (var1 >= 27 && var1 < 36) {
                player.inventory.mainInventory[var1 - 27] = var2;
            } else if (var1 >= 36 && var1 < 40) {
                player.inventory.armorInventory[39 - var1] = var2;
            } else {
                viewer.entityDropItem(var2, 0.5F);
            }
        }

        @Override
        public String getInventoryName() {

            return player.getDisplayName();
        }

        @Override
        public boolean hasCustomInventoryName() {

            return false;
        }

        @Override
        public int getInventoryStackLimit() {
            if (player == null || player.isDead) {
                viewer.closeScreen();
                return 64;
            }

            return player.inventory.getInventoryStackLimit();
        }

        @Override
        public void markDirty() {
        }

        @Override
        public boolean isUseableByPlayer(EntityPlayer var1) {
            if (player == null || player.isDead) {
                viewer.closeScreen();
                return false;
            }

            return true;
        }

        @Override
        public void openInventory() {
        }

        @Override
        public void closeInventory() {
        }

        @Override
        public boolean isItemValidForSlot(int i, ItemStack itemstack) {
            return true;
        }
    }
}
