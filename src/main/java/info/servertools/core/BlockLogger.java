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
package info.servertools.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import org.apache.logging.log4j.Level;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BlockLogger {

    private File logDirectory;

    private static final String FILE_HEADER = "TimeStamp,PlayerName,DimID,BlockX,BlockY,BlockZ,BlockName";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-YYYY");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("kk-mm-ss");

    public BlockLogger(File logDirectory) {

        if (logDirectory.exists() && !logDirectory.isDirectory())
            throw new IllegalArgumentException("File with same name as block logging directory detected");

        //noinspection ResultOfMethodCallIgnored
        logDirectory.mkdirs();

        this.logDirectory = logDirectory;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {

        File logFile = new File(logDirectory, DATE_FORMAT.format(Calendar.getInstance().getTime()) + ".csv");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {

            if (!logFile.exists())
                writeHeader(logFile);

            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s", TIME_FORMAT.format(Calendar.getInstance().getTime()), event.getPlayer().getCommandSenderName(),
                    event.world.provider.dimensionId, event.x, event.y, event.z, event.block.getUnlocalizedName()));
            writer.newLine();
        } catch (IOException e) {
            ServerTools.LOG.log(Level.WARN, "Failed to write block break event to file", e);
        }
    }

    private static void writeHeader(File file) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(FILE_HEADER);
            writer.newLine();
        } catch (IOException e) {
            ServerTools.LOG.log(Level.ERROR, "Failed to write BlockLogger header", e);
        }
    }
}
