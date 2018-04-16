package de.mewel.pixellogic.ui.page;

import java.util.HashMap;
import java.util.Map;

public class PixelLogicUIPageProperties {

    private Map<String, Object> data;

    public PixelLogicUIPageProperties() {
        this.data = new HashMap<String, Object>();
    }

    public Object get(String key) {
        return data.get(key);
    }

    public String getString(String key) {
        Object result = data.get(key);
        return result != null ? result.toString() : null;
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

    public <T> T get(String key, Class<T> type) {
        Object o = get(key);
        if (o == null) {
            return null;
        }
        return type.isInstance(o) ? (T) o : null;
    }

}
