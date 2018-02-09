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

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.ui.PixelLogicGUIUtil;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.LINE_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.LINE_COMPLETE_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.TEXT_COLOR;

public class PixelLogicGUIColumnInfo extends Group {

    private PixelLogicLevel level;
    private int column;

    private PixelLogicGUILevelResolution resolution;

    private List<Label> labels;

    private Texture texture;

    public PixelLogicGUIColumnInfo(PixelLogicLevel level, int column) {
        this.level = level;
        this.column = column;
        this.texture = PixelLogicGUIUtil.getWhiteTexture();
        this.labels = new ArrayList<Label>();
        this.updateLabels();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color baseColor = getColor();
        // block
        /*Color blockColor = level.isColumnComplete(this.column) ? LINE_COMPLETE_COLOR : LINE_COLOR;
        blockColor.a = baseColor.a * parentAlpha;
        batch.setColor(blockColor);
        batch.draw(texture, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());*/
        // text
        /*for(TextData data : textDataList) {
            resolution.getGameFont().draw(batch, data.layout, data.x, data.y);
        }*/

        batch.setColor(baseColor);
        super.draw(batch, parentAlpha);
    }

    public List<Label> getLabels() {
        return labels;
    }

    @Override
    public void clear() {
        this.texture.dispose();
        for (Actor actor : this.getChildren()) {
            actor.clear();
        }
        super.clear();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        updateLabels();
    }

    protected void updateLabels() {
        PixelLogicGUILevelResolutionManager resolutionManager = PixelLogicGUILevelResolutionManager.instance();
        this.resolution = resolutionManager.get(level);
        int scale = PixelLogicGUIUtil.getInfoSizeFactor(level);
        List<Integer> colLevelData = PixelLogicUtil.getNumbersOfCol(level.getLevelData(), column);
        int yOffset = ((int) resolution.getGameFont().getLineHeight() / 2) + 2;
        float x = 0;
        float y = getY() + (resolution.getGamePixelSize() * scale) - yOffset;
        if(this.labels.isEmpty()) {
            for (int i = 0; i < colLevelData.size(); i++) {
                String text = String.valueOf(colLevelData.get(i));
                BitmapFont labelFont = resolutionManager.getFont(resolution.getGamePixelSize(), TEXT_COLOR);
                Label.LabelStyle style = new Label.LabelStyle(labelFont, TEXT_COLOR);
                Label label = new Label(text, style);
                label.setPosition(x, y - ((colLevelData.size() - 1 - i) * yOffset));
                this.addActor(label);
                this.labels.add(label);

                Gdx.app.log("ci", "add label");

                /*Gdx.app.log("ci", "add label " + text);
                Gdx.app.log("ci", "x " + x);
                Gdx.app.log("ci", "y " + (y - ((colLevelData.size() - 1 - i) * yOffset)));*/


                /*layout.setText(resolution.getGameFont(), String.valueOf(colLevelData.get(i)),
                        textColor, resolution.getGamePixelSize(), Align.center, false);
                this.textDataList.add(new TextData(layout, x, y - ((colLevelData.size() - 1 - i) * yOffset)));*/
            }
        } else {
            for(int i = 0; i < labels.size(); i++) {
                Label label = labels.get(i);
                label.setPosition(x, y - ((colLevelData.size() - 1 - i) * yOffset));
            }
        }
    }

}
