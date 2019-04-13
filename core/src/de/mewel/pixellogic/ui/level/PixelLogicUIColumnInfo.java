package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.Collections;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicUIColumnInfo extends PixelLogicUILineInfo {

    protected boolean lineComplete;

    public PixelLogicUIColumnInfo(PixelLogicGlobal global, PixelLogicLevel level, int column) {
        super(global, level, column);
        this.lineComplete = level.isColumnComplete(column);
    }

    @Override
    public void onBoardChange(PixelLogicBoardChangedEvent event) {
        super.onBoardChange(event);
        if (event.getCol() == line) {
            this.lineComplete = level.isColumnComplete(event.getCol());
            for (Label label : labels) {
                label.getStyle().fontColor = getLabelColor();
            }
        }
    }

    @Override
    protected boolean isLineComplete() {
        return lineComplete;
    }

    @Override
    protected void addLabels(int fontSize, Label.LabelStyle style) {
        int yOffset = fontSize + (fontSize / 3);
        float x = getWidth() / 2 + 1;
        float y = yOffset / 5;
        List<Integer> colLevelData = PixelLogicUtil.getNumbersOfCol(level.getLevelData(), this.line);
        Collections.reverse(colLevelData);
        for (int i = 0; i < colLevelData.size(); i++) {
            String text = String.valueOf(colLevelData.get(i));
            Label label = new Label(text, style);
            label.setPosition(x - label.getPrefWidth() / 2, y + ((colLevelData.size() - 1 - i) * yOffset));
            this.addActor(label);
            this.labels.add(label);
        }
    }

}
