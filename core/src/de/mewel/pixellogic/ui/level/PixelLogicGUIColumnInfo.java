package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.ui.PixelLogicGUIUtil;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.LINE_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.LINE_COMPLETE_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.TEXT_COLOR;

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
        int yOffset = fontSize / 2 - (fontSize / 12);
        float x = getWidth() / 2 + 1;
        float y = getHeight() - yOffset;

        List<Integer> colLevelData = PixelLogicUtil.getNumbersOfCol(level.getLevelData(), this.line);
        for (int i = 0; i < colLevelData.size(); i++) {
            String text = String.valueOf(colLevelData.get(i));
            Label label = new Label(text, style);
            label.setPosition(x - label.getWidth() / 2, y - ((colLevelData.size() - 1 - i) * yOffset));
            this.addActor(label);
            this.labels.add(label);
        }
    }

}
