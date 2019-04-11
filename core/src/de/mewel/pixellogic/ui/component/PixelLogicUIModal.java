package de.mewel.pixellogic.ui.component;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;

public abstract class PixelLogicUIModal extends PixelLogicUIGroup {

    private Group parent;

    private PixelLogicUIColoredSurface backdrop;

    private Table content;

    private float fadeInDuration;

    public PixelLogicUIModal(PixelLogicGlobal global, Group parent) {
        super(global);
        this.parent = parent;
        this.backdrop = new PixelLogicUIColoredSurface(getGlobal());
        this.backdrop.setColor(new Color(0f, 0f, 0f, .6f));
        this.addActor(backdrop);
        this.content = new Table();
        this.addActor(content);
        this.fadeInDuration = .2f;
    }

    public void show() {
        this.setColor(0f, 0f, 0f, 0f);
        this.parent.addActor(this);
        this.addAction(Actions.fadeIn(fadeInDuration));
    }

    public void show(Actor before) {
        this.setColor(0f, 0f, 0f, 0f);
        this.parent.addActorBefore(before, this);
        this.addAction(Actions.fadeIn(fadeInDuration));
    }

    public void close() {
        this.addAction(Actions.sequence(Actions.fadeOut(this.fadeInDuration), Actions.run(new Runnable() {
            @Override
            public void run() {
                PixelLogicUIModal modal = PixelLogicUIModal.this;
                modal.parent.removeActor(modal);
                afterClose();
            }
        })));
    }

    protected void afterClose() {
    }

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

    public Table getContent() {
        return content;
    }

    public Group getModalParent() {
        return parent;
    }

}
