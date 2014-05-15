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

package com.matthewprenger.servertools.core.asm;

import com.google.common.eventbus.EventBus;
import com.matthewprenger.servertools.core.lib.Reference;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

public class STPluginModContainer extends DummyModContainer {

    public STPluginModContainer() {
        super(new ModMetadata());
        ModMetadata meta = super.getMetadata();
        meta.modId = "STPlugin";
        meta.name = "ServerTools Plugin";
        meta.description = "Patches Vanilla Minecraft for ServerTools";
        meta.authorList = Reference.AUTHORS;
        meta.parent = Reference.MOD_ID;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {

        bus.register(this);
        return true;
    }
}
