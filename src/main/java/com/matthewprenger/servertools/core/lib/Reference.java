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

import com.google.common.collect.ImmutableList;

import java.util.List;

public class Reference {


    public static final String MOD_ID = "ServerTools";
    public static final String MOD_NAME = MOD_ID;
    public static final String DEPENDENCIES = "required-after:Forge@[10.12.1.1060,)";
    public static final String FINGERPRINT = "@FINGERPRINT@";

    public static final List<String> AUTHORS = ImmutableList.of("matthewprenger");

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
}
