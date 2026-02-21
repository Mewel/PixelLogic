package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public abstract class PixelLogicUIButtonListener extends InputListener {

    private Vector2 start;

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        this.start = new Vector2(x, y);
        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        int maxDragUntilCancel = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 20;
        if (start.dst(new Vector2(x, y)) < maxDragUntilCancel) {
            onClick();
        }
    }

    public abstract void onClick();

}
