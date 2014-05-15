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
     * Check to see if a module's {@link java.lang.Package#implVersion Implementation-Version} is equal to ServerTool's Version
     *
     * The game will not load if they are unequal
     *
     * @param clazz a class to use
     */
    public static void checkModuleVersion(Class clazz) {

        if (BUILD.equals(String.valueOf('@') + "VERSION_BUILD@")) {
            ServerTools.log.warn("We are in a development environment");
            return;
        }

        String moduleVersion = clazz.getPackage().getImplementationVersion();

        if (!VERSION.equals(moduleVersion)) {

            ServerTools.log.fatal("Module: {} version does not match the ServerTools core version, please download matching ServerTools module versions", clazz.getName());
            throw new RuntimeException("Mismatched Module Versions");
        }
    }
}
