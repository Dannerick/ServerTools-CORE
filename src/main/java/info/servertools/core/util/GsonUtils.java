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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import info.servertools.core.lib.Reference;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * Variety of helper methods for dealing with serializing/deserializing via {@link com.google.gson.Gson Gson}
 */
public final class GsonUtils {

    private static final Gson gson = new Gson();
    private static final Gson gson_pp = new GsonBuilder().setPrettyPrinting().create();

    private GsonUtils() {
    }

    /**
     * Serialize an Object into it's Json representation
     *
     * @param object
     *         the object to serialize
     * @param prettyPrinting
     *         if 'pretty priniting' should be used in the generated JSON
     *
     * @return the JSON
     */
    public static String toJson(Object object, boolean prettyPrinting) {

        if (prettyPrinting) {
            return gson_pp.toJson(object);
        } else {
            return gson.toJson(object);
        }
    }

    /**
     * Write an object to file using GSON
     *
     * @param object
     *         the object to serialize
     * @param toFile
     *         the file to wrtie to
     * @param logger
     *         the optional logger to log to (May be <code>null</code>)
     * @param prettyPrinting
     *         if 'pretty priniting' should be used in the generated JSON
     */
    public static void writeToFile(Object object, File toFile, Logger logger, boolean prettyPrinting) {

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toFile), Reference.FILE_ENCODING))) {
            writer.write(toJson(object, prettyPrinting));
        } catch (IOException e) {
            if (logger != null)
                logger.error("Failed to write file to disk", e);
        } catch (Throwable t) {
            if (logger != null)
                logger.error("An unknown error occured when writing the file to disk", t);
        }
    }
}
