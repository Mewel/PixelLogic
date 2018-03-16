package de.mewel.pixellogic.ui.screen;

import java.util.HashMap;
import java.util.Map;

public class PixelLogicUIScreenProperties {

    private Map<String, Object> data;

    public PixelLogicUIScreenProperties() {
        this.data = new HashMap<String, Object>();
    }

    public Object get(String key) {
        return data.get(key);
    }

    public String getString(String key) {
        return data.get(key).toString();
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

    @Override
    public String toString() {
        return data.toString();
    }

}
