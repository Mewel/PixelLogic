package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.PixelLogicConstants;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public abstract class PixelLogicUICheckBox extends PixelLogicUIGroup {

    private Label label;

    private PixelLogicUIColoredSurface checkBackground;

    private Sprite checkButton;

    private boolean checked;

    private PixelLogicUIButtonListener listener;

    public PixelLogicUICheckBox(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);

        // check button
        this.checkBackground = new PixelLogicUIColoredSurface(assets);
        Color bgColor = PixelLogicConstants.SECONDARY_COLOR;
        this.checkBackground.setColor(bgColor);
        this.checkBackground.setBorder(1, new Color(bgColor).mul(.5f));
        this.addActor(this.checkBackground);

        this.addListener(this.listener = new PixelLogicUIButtonListener() {
            @Override
            public void onClick() {
                PixelLogicUICheckBox.this.onClick();
            }
        });
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        update();
    }

    public void setText(String text) {
        //this.text = text;
        //updateLabel(true);
    }

    private void update() {

    }

    public void onClick() {
        PixelLogicUIUtil.playButtonSound(getAssets());
        update();
        handleClick();
    }

    public abstract void handleClick();

}
