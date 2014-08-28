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

import com.google.common.io.Files;
import com.matthewprenger.servertools.core.lib.Reference;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.logging.log4j.Level;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class FileUtils {

    /**
     * Write a string to file
     * <p>
     * <b>IO is done on a separate thread!</b>
     * </p>
     *
     * @param string the string to write
     * @param file   the file to write to
     * @throws IOException
     */
    public static void writeStringToFile(final String string, final File file) throws IOException {

        new Thread() {
            @Override
            public void run() {
                try {
                    Files.write(string, file, Reference.CHARSET);
                } catch (IOException e) {
                    LogHelper.log(Level.WARN, "Failed to save file to disk", e);
                }
            }

        }.start();
    }

    /**
     * Read a file into a collection of strings
     * Each line is a new collection element
     *
     * @param file the file to read
     * @return A collection of strings
     * @throws IOException
     */
    public static Collection<String> readFileToString(File file) throws IOException {

        Collection<String> lines;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        }
    }

    /**
     * Check the size of a directory
     *
     * @param directory the directory to check
     * @return the size of the directory in bytes
     */
    public static long getFolderSize(File directory) {

        long length = 0;
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile())
                        length += file.length();
                    else
                        length += getFolderSize(file);
                }
            }
        }

        return length;
    }

    /**
     * Retrieve the oldest file in a directory
     *
     * @param directory the directory to check
     * @return the oldest file
     */
    public static File getOldestFile(File directory) {

        File[] files = directory.listFiles((FileFilter) FileFileFilter.FILE);

        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);

        return files.length > 0 ? files[0] : null;
    }
}
