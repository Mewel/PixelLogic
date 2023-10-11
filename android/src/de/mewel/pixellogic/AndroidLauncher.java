package de.mewel.pixellogic;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;

public class AndroidLauncher extends AndroidApplication implements PixelLogicListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useWakelock = true;
        PixelLogicGame game = new PixelLogicGame();
        initialize(game, config);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            game.ready().thenRun(() -> game.getEventManager().listen(this));
        }
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent levelStatusEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.PLAYABLE.equals(levelStatusEvent.getStatus())) {
                PixelLogicUILevel levelUI = levelStatusEvent.getSource().getLevelUI();
                int left = 0;
                int right = (int) levelUI.getStage().getWidth();
                int top = (int) levelUI.getY();
                int bottom = (int) levelUI.getY() + (int) levelUI.getHeight();
                setGestureExclusionRect(new Rect(left, top, right, bottom));
            } else {
                setGestureExclusionRect(null);
            }
        }
    }

    private void setGestureExclusionRect(Rect rect) {
        List<Rect> rects = new ArrayList<>();
        if (rect != null) {
            rects.add(rect);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            getWindow().getDecorView().setSystemGestureExclusionRects(rects);
        }
    }

}
