package de.mewel.pixellogic.ui.page;

import java.util.HashMap;
import java.util.Map;

public class PixelLogicUIPageProperties {

    private Map<String, Object> data;

    public PixelLogicUIPageProperties() {
        this.data = new HashMap<String, Object>();
    }

    public String getString(String key) {
        Object result = data.get(key);
        return result != null ? result.toString() : null;
    }

    public String getString(String key, String defaultValue) {
        Object result = data.get(key);
        return result != null ? result.toString() : defaultValue;
    }

    public Long getLong(String key) {
        return (Long) data.get(key);
    }

    public Integer getInt(String key) {
        return (Integer) data.get(key);
    }

    public void put(String key, Object value) {
        this.data.put(key, value);
    }

    public Map<String, Object> getData() {
        return data;
    }

    public boolean has(String key) {
        return this.data.containsKey(key);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object o = data.get(key);
        if (o == null) {
            return null;
        }
        return (T) o;
    }

}
