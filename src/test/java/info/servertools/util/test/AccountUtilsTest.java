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
package info.servertools.util.test;

import static org.junit.Assert.assertEquals;

import info.servertools.core.util.AccountUtils;
import org.junit.Test;

public class AccountUtilsTest {

    @Test
    public void test() {

        final String mattUsername = "matthewprenger";
        final String mattUUID = "8cc87067-025c-46d5-8413-66bd151084d0";

        final String resolvedUUID = AccountUtils.getUUID(mattUsername);
        assertEquals(mattUUID, resolvedUUID);

        final String resolvedUsername = AccountUtils.getUsername(mattUUID);
        assertEquals(mattUsername, resolvedUsername);
    }
}
