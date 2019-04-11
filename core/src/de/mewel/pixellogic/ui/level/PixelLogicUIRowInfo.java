package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import java.util.Collections;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicUIRowInfo extends PixelLogicUILineInfo {

    public PixelLogicUIRowInfo(PixelLogicGlobal global, PixelLogicLevel level, int row) {
        super(global, level, row);
    }

    @Override
    protected boolean isLineComplete() {
        return level.isRowComplete(this.line);
    }

    @Override
    public void onBoardChange(PixelLogicBoardChangedEvent event) {
        if (event.getRow() == line) {
            for (Label label : labels) {
                label.getStyle().fontColor = getLabelColor();
            }
        }
    }

    @Override
    protected void addLabels(int fontSize, Label.LabelStyle style) {
        List<Integer> rowLevelData = PixelLogicUtil.getNumbersOfRow(level.getLevelData(), this.line);
        Collections.reverse(rowLevelData);
        float x = getWidth() - (getWidth() / 18);
        float y = getHeight() / 2;

        float xCharOffset = 0;
        for (int i = 0; i < rowLevelData.size(); i++) {
            String text = String.valueOf(rowLevelData.get(i));
            Label label = new Label(text, style);
            label.setPosition(x - (xCharOffset * style.font.getSpaceWidth() * 1.5f), y, Align.right);
            this.addActor(label);
            this.labels.add(label);
            xCharOffset += text.length() == 1 ? 1 : 1.5f;
        }
    }

}
