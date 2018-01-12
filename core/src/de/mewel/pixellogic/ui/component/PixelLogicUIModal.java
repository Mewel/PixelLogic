package de.mewel.pixellogic.ui.component;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public abstract class PixelLogicUIModal extends Group {

    private Group parent;

    private PixelLogicUIColoredSurface backdrop;

    private Table content;

    private float fadeInDuration;

    public PixelLogicUIModal(Group parent) {
        this.parent = parent;
        this.backdrop = new PixelLogicUIColoredSurface(new Color(0f, 0f, 0f, .8f));
        this.addActor(backdrop);
        this.content = new Table();
        this.addActor(content);
        this.fadeInDuration = .2f;
        buildContent(this.content);
        updateBounds();
    }

    public void show() {
        this.setColor(0f, 0f, 0f, 0f);
        this.parent.addActor(this);
        this.addAction(Actions.fadeIn(fadeInDuration));
    }

    public void close() {
        Actions.sequence(Actions.fadeOut(this.fadeInDuration), Actions.run(new Runnable() {
            @Override
            public void run() {
                PixelLogicUIModal modal = PixelLogicUIModal.this;
                modal.parent.removeActor(modal);
            }
        }));
    }

    protected abstract void buildContent(Table content);

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha * getColor().a);
    }

    public void updateBounds() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        this.setBounds(0, 0, screenWidth, screenHeight);
        this.backdrop.setBounds(0, 0, screenWidth, screenHeight);
    }

    public void setFadeInDuration(float fadeInDuration) {
        this.fadeInDuration = fadeInDuration;
    }

    public Group getContent() {
        return content;
    }

}
