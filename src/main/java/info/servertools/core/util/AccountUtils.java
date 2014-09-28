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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import info.servertools.core.ServerTools;
import org.apache.logging.log4j.Level;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Callable;

/**
 * Helper class for interacting with Mojang accounts
 */
public final class AccountUtils {

    private static final Gson gson = new Gson();

    private static final Cache<String, String> usernameToUUID = CacheBuilder.newBuilder().build();
    private static final Cache<String, String> UUIDToUsername = CacheBuilder.newBuilder().build();

    private AccountUtils() {
    }

    /**
     * Get a player's username given their UUID
     * The username is cached for future use
     *
     * @param uuid
     *         the player's UUID
     *
     * @return the player's username
     */
    public static String getUsername(final String uuid) {

        String username = "";

        try {
            username = UUIDToUsername.get(uuid, new Callable<String>() {
                @Override
                public String call() throws Exception {

                    URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + URLEncoder.encode(uuid.replace("-", ""), "UTF-8"));
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                        Account account = gson.fromJson(reader, Account.class);
                        return account.name;
                    }
                }
            });
        } catch (Exception e) {
            ServerTools.LOG.log(Level.WARN, "Failed to fetch username from UUID", e);
        }

        return username;
    }

    /**
     * Get a player's UUID from their username
     * The UUID is cached for future use
     *
     * @param username
     *         the player's username
     *
     * @return the player's UUID
     */
    public static String getUUID(final String username) {

        String uuid = "";

        try {
            uuid = usernameToUUID.get(username, new Callable<String>() {
                @Override
                public String call() throws Exception {


                    URL url = new URL("https://api.mojang.com/profiles/minecraft");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");

                    connection.setUseCaches(false);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
                        writer.write(GsonUtils.toJson(username, false).getBytes());
                    }

                    Account[] account;

                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        account = gson.fromJson(reader, Account[].class);
                    }

                    return (account != null && account.length > 0) ? account[0].id : "";
                }
            });
        } catch (Exception e) {

            ServerTools.LOG.log(Level.WARN, "Failed to fetch UUID  from username", e);
        }

        uuid = uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"); // Add the dashes back into the UUID

        return uuid;
    }

    public static class Account {

        public String id;
        public String name;

        @Override
        public String toString() {
            return "Account{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
        }
    }
}
