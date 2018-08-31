package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.BLOCK_COLOR;

public class PixelLogicUIMoreLevelsPage extends PixelLogicUIBasePage {

    private List<LevelModeUI> modes;

    public PixelLogicUIMoreLevelsPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.moreLevels, "More Levels", PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void build() {
        this.modes = new ArrayList<LevelModeUI>();
        this.modes.add(new LevelModeUI("100 Characters", "77 extra levels made using ", this));
        this.modes.add(new LevelModeUI("Time trial", "Play infinite auto " +
                "generated levels against the clock and try to beat your highscore.", this));
        // this.buildModes();
    }

    private class LevelModeUI extends Container<VerticalGroup> {

        private String description;

        private PixelLogicUIMoreLevelsPage page;

        private PixelLogicUIButton button;

        public LevelModeUI(final String buttonText, final String description, PixelLogicUIMoreLevelsPage page) {
            super(new VerticalGroup());

            this.page = page;
            this.description = description;

            getActor().setFillParent(true);
            getActor().center();
            getActor().bottom();
            getActor().space(getPadding());

            this.button = new PixelLogicUIButton(page.getAssets(), page.getEventManager(), buttonText) {
                @Override
                public void onClick() {
                    if (block()) {
                        return;
                    }
                    // startTimeTrial(options);
                }
            };
            this.button.setSize(getButtonWidth(), getButtonHeight());
            getActor().addActor(this.button);

            getActor().pack();

            Texture whiteTexture = PixelLogicUIUtil.getTexture(BLOCK_COLOR);
            Sprite s = new Sprite(whiteTexture);
            this.setBackground(new SpriteDrawable(s));
        }

        public void resize() {
            this.width(getComponentWidth());
            float padding = getPadding() / 2;
            this.pad(padding, 0, padding, padding * 2);
            this.button.setSize(getButtonWidth(), getButtonHeight());
        }

        public void activate() {
            this.button.unblock();
        }
    }

}
