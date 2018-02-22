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

    public Map<String, String> getData() {
        return data;
    }

}
