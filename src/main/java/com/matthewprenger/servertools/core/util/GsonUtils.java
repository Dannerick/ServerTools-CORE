package com.matthewprenger.servertools.core.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.matthewprenger.servertools.core.lib.Reference;
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
     * @param object         the object to serialize
     * @param prettyPrinting if 'pretty priniting' should be used in the generated JSON
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
     * @param object         the object to serialize
     * @param toFile         the file to wrtie to
     * @param logger         the optional logger to log to (May be <code>null</code>)
     * @param prettyPrinting if 'pretty priniting' should be used in the generated JSON
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

    /**
     * Read an object from file using GSON
     *
     * @param file   the file to read
     * @param logger the optional logger to log to (May be <code>null</code>)
     * @param <T>    the type of the object
     * @return the object
     */
    public static <T> T fromJson(File file, Logger logger) {

        T obj = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Reference.FILE_ENCODING))) {
            obj = gson.fromJson(reader, new TypeToken<T>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            if (logger != null)
                logger.error("Failed to parse file as valid json", e);
        } catch (IOException e) {
            if (logger != null)
                logger.error("Failed to read file from disk", e);
        } catch (Throwable t) {
            if (logger != null)
                logger.error("An unknown error occured when reading the file from disk", t);
        }

        return obj;
    }
}
