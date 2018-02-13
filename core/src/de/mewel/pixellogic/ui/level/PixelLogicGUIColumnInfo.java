package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

import static de.mewel.pixellogic.ui.PixelLogicGUIUtil.getFontScaleFactor;

public class PixelLogicGUIColumnInfo extends PixelLogicGUILineInfo {

    public PixelLogicGUIColumnInfo(PixelLogicLevel level, int column) {
        super(level, column);
    }

    @Override
    protected boolean isLineComplete() {
        return level.isColumnComplete(this.line);
    }

    @Override
    protected void addLabels(int fontSize, Label.LabelStyle style) {
        int yOffset = (fontSize / getFontScaleFactor()) / 2 - (fontSize / (12 * getFontScaleFactor()));
        float x = getWidth() / 2 + 1;
        float y = getHeight() - yOffset;

        List<Integer> colLevelData = PixelLogicUtil.getNumbersOfCol(level.getLevelData(), this.line);
        for (int i = 0; i < colLevelData.size(); i++) {
            String text = String.valueOf(colLevelData.get(i));
            Label label = new Label(text, style);
            label.setPosition(x - label.getPrefWidth() / 2, y - ((colLevelData.size() - 1 - i) * yOffset));
            this.addActor(label);
            this.labels.add(label);
        }
    }

}
