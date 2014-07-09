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

import com.matthewprenger.servertools.core.lib.Reference;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LogHelper {

    private LogHelper() {
    }

    private static final Logger log = LogManager.getLogger(Reference.MOD_NAME);

    public static void log(Level level, Object message) {

        log.log(level, message);
    }

    public static void log(Level level, Object message, Throwable t) {

        log.log(level, message, t);
    }

    public static void fatal(Object message) {
        log(Level.FATAL, message);
    }

    public static void error(Object message) {
        log(Level.ERROR, message);
    }

    public static void warn(Object message) {
        log(Level.WARN, message);
    }

    public static void info(Object message) {
        log(Level.INFO, message);
    }

    public static void debug(Object message) {
        log(Level.DEBUG, message);
    }

    public static void trace(Object message) {
        log(Level.TRACE, message);
    }
}
