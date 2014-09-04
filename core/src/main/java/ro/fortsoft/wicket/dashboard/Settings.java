/*
 * Copyright 2014 Decebal Suiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ro.fortsoft.wicket.dashboard;

import java.util.*;

/**
 * @author Decebal Suiu
 */
public class Settings extends HashMap<String, String> {

    private static final long serialVersionUID = 1L;

    public Settings() {
        super();
    }

    public Settings(Map<String, String> settings) {
        super(settings);
    }

    public String getValueAsString(String key) {
        return getValueAsString(key, null);
    }

    public String getValueAsString(String key, String defaultValue) {
        String setting = get(key);
        return setting != null ? setting : defaultValue;
    }

    public int getValueAsInt(String key) {
        return getValueAsInt(key, 0);
    }

    public int getValueAsInt(String key, int defaultValue) {
        String setting = get(key);
        return setting != null ? Integer.parseInt(setting) : defaultValue;
    }

    public long getValueAsLong(String key) {
        return getValueAsLong(key, 0);
    }

    public long getValueAsLong(String key, long defaultValue) {
        String setting = get(key);
        return setting != null ? Long.parseLong(setting) : defaultValue;

    }

    public float getValueAsFloat(String key) {
        return getValueAsFloat(key, 0);
    }

    public float getValueAsFloat(String key, float defaultValue) {
        String setting = get(key);
        return setting != null ? Float.parseFloat(setting) : defaultValue;
    }

    public boolean getValueAsBoolean(String key) {
        return getValueAsBoolean(key, false);
    }

    public boolean getValueAsBoolean(String key, boolean defaultValue) {
        String setting = get(key);
        return setting != null ? Boolean.valueOf(setting) : defaultValue;
    }

    public List<String> getValueAsList(String key) {
        return getValueAsList(key, new ArrayList<String>());
    }

    public List<String> getValueAsList(String key, List<String> defaultValue) {
        String setting = get(key);
        return setting != null ? Arrays.asList(setting.split(",")) : defaultValue;
    }

}