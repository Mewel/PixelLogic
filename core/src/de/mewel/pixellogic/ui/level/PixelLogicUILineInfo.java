package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;

import static de.mewel.pixellogic.PixelLogicConstants.APP_BACKGROUND;
import static de.mewel.pixellogic.PixelLogicConstants.LINE_COLOR;
import static de.mewel.pixellogic.PixelLogicConstants.LINE_COMPLETE_COLOR;
import static de.mewel.pixellogic.PixelLogicConstants.TEXT_COLOR;

public abstract class PixelLogicUILineInfo extends PixelLogicUILevelGroup {

    protected PixelLogicLevel level;

    protected int line;

    protected List<Label> labels;

    protected Texture texture;

    protected PixelLogicUILevelResolution resolution;

    public PixelLogicUILineInfo(PixelLogicGlobal global, PixelLogicLevel level, int line) {
        super(global);
        this.level = level;
        this.line = line;
        this.texture = PixelLogicUIUtil.getWhiteTexture();
        this.labels = new ArrayList<Label>();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color baseColor = getColor();
        boolean lineComplete = isLineComplete();
        float r = lineComplete ? LINE_COMPLETE_COLOR.r : LINE_COLOR.r;
        float g = lineComplete ? LINE_COMPLETE_COLOR.g : LINE_COLOR.g;
        float b = lineComplete ? LINE_COMPLETE_COLOR.b : LINE_COLOR.b;
        batch.setColor(r, g, b, baseColor.a * parentAlpha);
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
    public void updateLevelResolution(PixelLogicUILevelResolution resolution) {
        this.resolution = resolution;
        updateLabels(resolution);
    }

    private void updateLabels(PixelLogicUILevelResolution resolution) {
        clearLabels();
        BitmapFont labelFont = getAssets().getLevelFont(resolution.getFontSize());
        Label.LabelStyle style = new Label.LabelStyle(labelFont, getLabelColor());
        addLabels((int) labelFont.getLineHeight(), style);
    }

    protected void clearLabels() {
        for (Label label : this.labels) {
            label.clear();
            this.removeActor(label);
        }
        this.labels.clear();
    }

    protected abstract void addLabels(int fontSize, Label.LabelStyle style);

    protected abstract boolean isLineComplete();

    public Color getLabelColor() {
        return isLineComplete() ? new Color(APP_BACKGROUND) : new Color(TEXT_COLOR);
    }

    public void reset() {
        if (this.resolution == null) {
            return;
        }
        updateLabels(this.resolution);
    }

}
