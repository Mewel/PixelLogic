package de.mewel.pixellogic.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.model.PixelLogicLevel;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_BLOCKED_COLOR;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_EMPTY_COLOR;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_FILLED_COLOR;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SPACE_COMBINED;

class PixelLogicGUIBoardPixel extends Actor {

    private PixelLogicLevel level;
    private int row, col;

    private Texture texture;

    private boolean solved;

    private boolean solvedAnimationStarted;

    public PixelLogicGUIBoardPixel(PixelLogicLevel level, int row, int col) {
        this.level = level;
        this.row = row;
        this.col = col;
        this.solvedAnimationStarted = this.solved = false;
        this.texture = PixelLogicGUIUtil.getWhiteTexture();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());
        batch.setColor(color);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!solved) {
            solved = level.isSolved();
            setColor(level.isEmpty(row, col) ? PIXEL_EMPTY_COLOR :
                    (level.isFilled(row, col) ? PIXEL_FILLED_COLOR : PIXEL_BLOCKED_COLOR));
        }
        if (solved && !solvedAnimationStarted) {
            boolean isFilled = level.isFilled(row, col);
            if (!isFilled) {
                this.addAction(Actions.fadeOut(0.4f));
            }
            if (isFilled) {
                Color pixelColor = level.getColor(row, col);
                if (pixelColor != null) {
                    SequenceAction sequenceAction = new SequenceAction();
                    float mult = (float) (col + row) / (float) (level.getColumns() + level.getRows());
                    sequenceAction.addAction(Actions.delay(0.2f * mult));
                    sequenceAction.addAction(Actions.color(Color.WHITE, 0.2f + (0.4f * mult)));
                    sequenceAction.addAction(Actions.delay(0.2f * mult));
                    sequenceAction.addAction(Actions.color(pixelColor, 0.2f + (0.4f * mult)));
                    this.addAction(sequenceAction);
                }
            }
            solvedAnimationStarted = true;
        }
    }

    @Override
    public void clear() {
        this.texture.dispose();
    }

}
