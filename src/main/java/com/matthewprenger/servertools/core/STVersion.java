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

package com.matthewprenger.servertools.core;

import com.google.common.base.Strings;

public final class STVersion {

    private STVersion() {
    }

    /**
     * This is incremented when major changes occur
     */
    public static final String MAJOR = "@VERSION_MAJOR@";

    /**
     * This is incremented when minor changes occur
     */
    public static final String MINOR = "@VERSION_MINOR@";

    /**
     * This is incremented when a small revision occurs
     */
    public static final String REV = "@VERSION_REV@";

    /**
     * This is incremented each time the mod is built
     */
    public static final String BUILD = "@VERSION_BUILD@";

    /**
     * The full version string represented in MAJOR.MINOR.REV.BUILD format
     */
    public static final String VERSION = MAJOR + "." + MINOR + "." + REV + "." + BUILD;

    /**
     * The Minecraft version that we are built for
     */
    public static final String MCVERSIONN = "@MCVERSION@";

    // TODO Add version check capability

    /**
     * Check if the minimum version requirement for CORE is met
     * The game will halt if it is not
     *
     * @param minCoreVersion the minimum CORE version
     */
    public static void checkVersion(String minCoreVersion) {

        if (minCoreVersion.contains("@") || Strings.isNullOrEmpty(STVersion.class.getPackage().getSpecificationVersion())) {
            ServerTools.log.warn("Development environment detected");
            return;
        }

        boolean compatable;
        try {
            compatable = STVersion.class.getPackage().isCompatibleWith(minCoreVersion);
        } catch (NumberFormatException e) {
            compatable = false;
        }

        if (!compatable) {
            throw new RuntimeException("Minimum ServerTools-CORE version required is: " + minCoreVersion);
        }
    }
}
