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

package com.matthewprenger.servertools.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;

public class Location {

    public final int dimID;
    public final double x, y, z;

    public Location(int dimID, double x, double y, double z) {

        this.dimID = dimID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(int dimID, int x, int y, int z) {

        this.dimID = dimID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(int dimID, ChunkCoordinates chunkCoordinates) {

        this.dimID = dimID;
        this.x = chunkCoordinates.posX;
        this.y = chunkCoordinates.posY;
        this.z = chunkCoordinates.posZ;
    }

    public Location(Entity entity) {

        this.dimID = entity.worldObj.provider.dimensionId;
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (dimID != location.dimID) return false;
        if (Double.compare(location.x, x) != 0) return false;
        if (Double.compare(location.y, y) != 0) return false;
        if (Double.compare(location.z, z) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = dimID;
        temp = Double.doubleToLongBits(x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {

        return String.format("DIM: %s, X: %s, Y: %s, Z: %s", this.dimID, this.x, this.y, this.z);
    }
}
