package de.mewel.pixellogic.ui.screen;

import java.util.HashMap;
import java.util.Map;

public class PixelLogicUIScreenData {

    private Map<String, String> data;

    public PixelLogicUIScreenData() {
        this.data = new HashMap<String, String>();
    }

    public String get(String key) {
        return data.get(key);
    }

    public Long getLong(String key) {
        return Long.valueOf(data.get(key));
    }

    public void put(String key, String value) {
        this.data.put(key, value);
    }

    public void put(String key, Long value) {
        this.data.put(key, value.toString());
    }

    public Map<String, String> getData() {
        return data;
    }

}
