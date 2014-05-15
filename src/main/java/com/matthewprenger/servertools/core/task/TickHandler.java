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

package com.matthewprenger.servertools.core.task;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public class TickHandler {

    private final ConcurrentLinkedQueue<ITickTask> tasks = new ConcurrentLinkedQueue<>();

    public TickHandler() {

        FMLCommonHandler.instance().bus().register(this);
    }

    public void registerTask(ITickTask task) {

        tasks.offer(task);
    }

    @SubscribeEvent
    public void tickStart(TickEvent.ServerTickEvent event) {

        if (event.phase != TickEvent.Phase.START)
            return;

        for (ITickTask task : tasks) {

            if (task.isComplete()) {
                task.onComplete();
                tasks.remove(task);
            }

            task.tick();
        }
    }
}
