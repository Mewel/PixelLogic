package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.PIXEL_BLOCKED_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.PIXEL_EMPTY_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.PIXEL_FILLED_COLOR;

class PixelLogicUIBoardPixel extends Actor {

    private PixelLogicLevel level;
    private int row, col;

    private Texture texture;

    private PixelLogicLevelStatus levelStatus;

    private boolean solved;

    private boolean solvedAnimationStarted;

    public PixelLogicUIBoardPixel(PixelLogicLevel level, int row, int col) {
        this.level = level;
        this.row = row;
        this.col = col;
        this.solvedAnimationStarted = this.solved = false;
        this.levelStatus = null;
        this.texture = PixelLogicUIUtil.getWhiteTexture();
    }

    public void updateLevelStatus(PixelLogicLevelStatus status) {
        this.levelStatus = status;
        if(PixelLogicLevelStatus.solved.equals(this.levelStatus)) {
            this.solved = true;
        }
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
            setColor(level.isEmpty(row, col) ? PIXEL_EMPTY_COLOR :
                    (level.isFilled(row, col) ? PIXEL_FILLED_COLOR : PIXEL_BLOCKED_COLOR));
        }
        if (solved && !solvedAnimationStarted) {
            boolean isFilled = level.isFilled(row, col);
            Color pixelColor = level.getColor(row, col);
            if (!isFilled) {
                if (pixelColor == null) {
                    this.addAction(Actions.fadeOut(0.4f));
                } else {
                    SequenceAction sequenceAction = new SequenceAction();
                    sequenceAction.addAction(Actions.fadeOut(0.4f));
                    sequenceAction.addAction(Actions.delay(0.2f));
                    sequenceAction.addAction(Actions.color(pixelColor, 0.6f));
                    this.addAction(sequenceAction);
                }
            }
            if (isFilled) {
                if (pixelColor != null) {
                    SequenceAction sequenceAction = new SequenceAction();
                    sequenceAction.addAction(Actions.delay(0.3f));
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
        super.clear();
        this.texture.dispose();
    }

}
