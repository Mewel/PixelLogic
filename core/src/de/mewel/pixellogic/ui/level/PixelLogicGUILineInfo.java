package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicGUIUtil;

import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.FONT_SCALE;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.LINE_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.LINE_COMPLETE_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.TEXT_COLOR;

public abstract class PixelLogicGUILineInfo extends Group {

    protected PixelLogicLevel level;

    protected int line;

    protected List<Label> labels;

    protected Texture texture;

    public PixelLogicGUILineInfo(PixelLogicLevel level, int line) {
        this.level = level;
        this.line = line;
        this.texture = PixelLogicGUIUtil.getWhiteTexture();
        this.labels = new ArrayList<Label>();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color baseColor = getColor();
        Color blockColor = isLineComplete() ? LINE_COMPLETE_COLOR : LINE_COLOR;
        blockColor.a = baseColor.a * parentAlpha;
        batch.setColor(blockColor);
        batch.draw(texture, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());
        batch.setColor(baseColor);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void clear() {
        this.texture.dispose();
        clearLabels();
        super.clear();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        updateLabels();
    }

    protected void clearLabels() {
        for (Label label : this.labels) {
            label.clear();
            this.removeActor(label);
        }
        this.labels.clear();
    }

    protected void updateLabels() {
        clearLabels();
        PixelLogicGUILevelResolutionManager resolutionManager = PixelLogicGUILevelResolutionManager.instance();
        PixelLogicGUILevelResolution resolution = resolutionManager.get(level);
        int fontSize = resolution.getGamePixelSize() * FONT_SCALE;
        BitmapFont labelFont = resolutionManager.buildFont(fontSize, Color.BLACK);
        labelFont.getData().setScale(1f / FONT_SCALE);
        Label.LabelStyle style = new Label.LabelStyle(labelFont, TEXT_COLOR);
        Gdx.app.log("line info", "" + style.font.getCapHeight());
        addLabels(fontSize, style);
    }

    protected abstract void addLabels(int fontSize, Label.LabelStyle style);

    protected abstract boolean isLineComplete();

}
