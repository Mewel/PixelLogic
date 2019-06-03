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
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

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
        PixelLogicUIStyle style = getGlobal().getStyle();
        Color lineCompleteColor = style.getLineCompleteColor();
        Color lineColor = style.getLineColor();

        float r = lineComplete ? lineCompleteColor.r : lineColor.r;
        float g = lineComplete ? lineCompleteColor.g : lineColor.g;
        float b = lineComplete ? lineCompleteColor.b : lineColor.b;
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

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        super.styleChanged(style);
        this.updateLabels(this.resolution);
    }

    protected abstract void addLabels(int fontSize, Label.LabelStyle style);

    protected abstract boolean isLineComplete();

    public Color getLabelColor() {
        PixelLogicUIStyle style = getGlobal().getStyle();
        return isLineComplete() ? new Color(style.getBackgroundColor()) : new Color(style.getTextColor());
    }

    public void reset() {
        if (this.resolution == null) {
            return;
        }
        updateLabels(this.resolution);
    }

}
