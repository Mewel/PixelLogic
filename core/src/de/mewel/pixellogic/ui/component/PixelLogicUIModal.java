package de.mewel.pixellogic.ui.component;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;

public abstract class PixelLogicUIModal extends PixelLogicUIGroup {

    private Group parent;

    private PixelLogicUIColoredSurface backdrop;

    private Table content;

    private float fadeInDuration;

    public PixelLogicUIModal(PixelLogicAssets assets, PixelLogicEventManager eventManager, Group parent) {
        super(assets, eventManager);
        this.parent = parent;
        this.backdrop = new PixelLogicUIColoredSurface(new Color(0f, 0f, 0f, .8f));
        this.addActor(backdrop);
        this.content = new Table();
        this.addActor(content);
        this.fadeInDuration = .2f;
        buildContent(this.content);
    }

    public void show() {
        this.setColor(0f, 0f, 0f, 0f);
        this.parent.addActor(this);
        this.addAction(Actions.fadeIn(fadeInDuration));
    }

    public void close() {
        this.addAction(Actions.sequence(Actions.fadeOut(this.fadeInDuration), Actions.run(new Runnable() {
            @Override
            public void run() {
                PixelLogicUIModal modal = PixelLogicUIModal.this;
                modal.parent.removeActor(modal);
            }
        })));
    }

    protected abstract void buildContent(Table content);

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha * getColor().a);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        this.setBounds(0, 0, screenWidth, screenHeight);
        this.backdrop.setBounds(0, 0, screenWidth, screenHeight);
        this.content.setBounds(0, 0, screenWidth, screenHeight);
    }

    public void setFadeInDuration(float fadeInDuration) {
        this.fadeInDuration = fadeInDuration;
    }

    public Group getContent() {
        return content;
    }

}
