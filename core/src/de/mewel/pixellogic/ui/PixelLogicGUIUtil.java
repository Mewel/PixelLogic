package de.mewel.pixellogic.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicGUIUtil {

    public static Texture getWhiteTexture() {
        return getTexture(Color.WHITE);
    }

    public static Texture getTexture(Color color) {
        Pixmap linePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        linePixmap.setColor(color);
        linePixmap.fill();
        return new Texture(linePixmap);
    }

    public static int getInfoSizeFactor(PixelLogicLevel level) {
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level.getLevelData());
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level.getLevelData());
        int maxNumbers = 1;
        for (List<Integer> row : rowData) {
            if (row.size() > maxNumbers) {
                maxNumbers = row.size();
            }
        }
        for (List<Integer> column : colData) {
            if (column.size() > maxNumbers) {
                maxNumbers = column.size();
            }
        }
        return (int) Math.ceil(maxNumbers / 2d);
    }

}
