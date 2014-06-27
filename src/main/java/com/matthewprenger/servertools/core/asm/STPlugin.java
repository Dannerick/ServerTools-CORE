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

import cpw.mods.fml.relauncher.IFMLCallHook;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@IFMLLoadingPlugin.Name("STPlugin")
@IFMLLoadingPlugin.TransformerExclusions({"com.matthewprenger.servertools.core.asm"})
@IFMLLoadingPlugin.MCVersion("1.7.10")
public class STPlugin implements IFMLLoadingPlugin, IFMLCallHook {

    public static final Logger log = LogManager.getLogger(STPlugin.class.getSimpleName());

    @Override
    public Void call() throws Exception {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{STClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {

        return STPluginModContainer.class.getName();
    }

    @Override
    public String getSetupClass() {

        return getClass().getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
