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
package info.servertools.core.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.UUID;

/**
 * Helper class with methods used for server functions and checks
 */
public final class ServerUtils {

    private ServerUtils() {
    }

    /**
     * Get all players logged into the server
     *
     * @return a list of players
     */
    @SuppressWarnings("unchecked")
    public static List<EntityPlayerMP> getAllPlayers() {
        return MinecraftServer.getServer().getConfigurationManager().playerEntityList;
    }

    /**
     * Get the player for a given {@link java.util.UUID UUID}
     *
     * @param uuid
     *         the UUID
     *
     * @return the EntityPlayer, or <code>null</code> if the player does not exist
     */
    public static EntityPlayerMP getPlayerForUUID(UUID uuid) {

        Util.checkNotNull(uuid);

        for (EntityPlayerMP player : getAllPlayers()) {
            if (uuid.equals(player.getGameProfile().getId())) {
                return player;
            }
        }

        return null;
    }

    /**
     * Get the player for a given username
     *
     * @param username
     *         the username
     *
     * @return the EntityPlayer, or <code>null</code> if the player does not exist
     */
    public static EntityPlayerMP getPlayerForUsername(String username) {

        Util.checkNotNull(username);

        for (EntityPlayerMP player : getAllPlayers()) {
            if (username.equals(player.getGameProfile().getName())) {
                return player;
            }
        }

        return null;
    }

    /**
     * Teleport a player to a given location
     *
     * @param entityPlayer
     *         the player
     * @param location
     *         the location
     */
    public static void teleportPlayer(EntityPlayerMP entityPlayer, Location location) {

        Util.checkNotNull(entityPlayer, location);

        if (entityPlayer.worldObj.provider.dimensionId != location.dimID)
            entityPlayer.travelToDimension(location.dimID);

        entityPlayer.setPositionAndUpdate(location.x, location.y, location.z);
    }

    /**
     * Checks to see if a given player has server OP status
     *
     * @param profile
     *         the player's {@link com.mojang.authlib.GameProfile GameProfile}
     *
     * @return if the player is OP
     */
    public static boolean isOP(GameProfile profile) {
        return MinecraftServer.getServer().getConfigurationManager().getOppedPlayers().hasEntry(profile);
    }
}
