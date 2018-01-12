package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.event.PixelLogicLevelChangeAdapter;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicLevelChangeListener;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicGUIBoard extends Group implements PixelLogicLevelChangeListener {

    private PixelLogicLevel level;

    private PixelLogicGUIBoardPixel[][] pixels;

    private PixelLogicGUIBoardGrid grid;

    private PixelLogicLevelChangeAdapter changeAdapter;

    public PixelLogicGUIBoard() {
        this.changeAdapter = new PixelLogicLevelChangeAdapter();
        this.changeAdapter.bind(this);
    }

    @Override
    public void onLevelLoad(PixelLogicLevelChangeEvent event) {
        this.level = event.getLevel();
        this.pixels = new PixelLogicGUIBoardPixel[level.getRows()][level.getColumns()];
        this.grid = new PixelLogicGUIBoardGrid(this.level);
        this.addActor(this.grid);
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                PixelLogicGUIBoardPixel pixel = new PixelLogicGUIBoardPixel(level, row, col);
                this.pixels[row][col] = pixel;
                this.addActor(pixel);
            }
        }
        this.addAction(Actions.fadeIn(2f));
    }

    @Override
    public void onLevelSolved(PixelLogicLevelChangeEvent event) {
        this.grid.addAction(Actions.fadeOut(.2f));
    }

    @Override
    public void onLevelDestroyed(PixelLogicLevelChangeEvent event) {

    }

    @Override
    public void onLevelChange(PixelLogicLevelChangeEvent event) {
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                this.pixels[row][col].updateLevelStatus(event.getStatus());
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(this.level);
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                float x = resolution.getGamePixelSizeCombined() * col;
                float y = resolution.getGamePixelSizeCombined() * row;
                PixelLogicGUIBoardPixel pixel = this.pixels[row][col];
                pixel.setBounds(x, y, resolution.getGamePixelSize(), resolution.getGamePixelSize());
            }
        }
    }

    @Override
    public void clear() {
        for (Actor actor : this.getChildren()) {
            actor.clear();
        }
        this.changeAdapter.unbind();
        super.clear();
    }

}
