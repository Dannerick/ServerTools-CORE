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

package com.matthewprenger.servertools.core.lib;

public class Strings {

    /* MOTD */
    public static final String[] MOTD_DEFAULT = new String[]{
            "Hello, $PLAYER$!",
            "This is the default MOTD. In order to change it,",
            "edit the motd.txt in the servertools directory"
    };
    public static final String MOTD_RELOAD = "Reloaded the MOTD from file";

    /* Voice / Silence */
    public static final String VOICE_LOAD_ERROR = "Error loading the voice file";
    public static final String VOICE_SAVE_ERROR = "Error saving the voice file";
    public static final String SILENCE_LOAD_ERROR = "Error loading the silence file";
    public static final String SILENCE_SAVE_ERROR = "Error saving the silence file";

    /* General Command Errors */
    public static final String COMMAND_ERROR_ONLYPLAYER = "Only players can use that command";
    public static final String COMMAND_ERROR_ENTITY_NOEXIST = "That entity doesn't exist";

    /* Voice Commands */
    public static final String COMMAND_VOICE_ADD = "Gave voice to %s";
    public static final String COMMAND_VOICE_REMOVE = "Removed voice from %s";
    public static final String COMMAND_VOICE_REMOVE_NOUSER = "That user is not voiced";
    public static final String COMMAND_VOICE_RELOAD = "Reloaded the voice list from file";

    /* Silence Commands */
    public static final String COMMAND_SILENCE_ADD = "Silenced %s";
    public static final String COMMAND_SILENCE_REMOVE = "Removed silence from %s";
    public static final String COMMAND_SILENCE_REMOVE_NOUSER = "That user is not silenced";
    public static final String COMMAND_SILENCE_RELOAD = "Reloaded the silence list from file";

    public static final String ERROR_SILENCED = "You are silenced on this server";
}
