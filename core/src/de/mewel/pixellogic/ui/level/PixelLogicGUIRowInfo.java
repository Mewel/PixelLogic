package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import java.util.Collections;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicGUIRowInfo extends PixelLogicGUILineInfo {

    public PixelLogicGUIRowInfo(PixelLogicLevel level, int row) {
        super(level, row);
    }

    @Override
    protected boolean isLineComplete() {
        return level.isRowComplete(this.line);
    }

    @Override
    protected void addLabels(int fontSize, Label.LabelStyle style) {
        List<Integer> rowLevelData = PixelLogicUtil.getNumbersOfRow(level.getLevelData(), this.line);
        Collections.reverse(rowLevelData);
        float x = getWidth() - (getWidth() / 12);
        float y = getHeight() / 2;

        for (int i = 0; i < rowLevelData.size(); i++) {
            String text = String.valueOf(rowLevelData.get(i));
            Label label = new Label(text, style);
            label.setPosition(x - (i * style.font.getSpaceWidth() * 1.5f), y, Align.right);
            this.addActor(label);
            this.labels.add(label);
        }
    }

}
