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

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public final class ChatUtils {

    public static ChatComponentText getChatComponent(String message) {
        return getChatComponent(message, null);
    }

    public static ChatComponentText getChatComponent(String message, EnumChatFormatting formatting) {
        ChatComponentText text = new ChatComponentText(message);
        if (formatting != null)
            text.getChatStyle().setColor(formatting);
        return text;
    }

    private ChatUtils() {}
}
