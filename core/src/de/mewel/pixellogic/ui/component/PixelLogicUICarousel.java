package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

public class PixelLogicUICarousel extends Group {

    private List<PixelLogicUICarouselItem> items;

    private int index;

    public PixelLogicUICarousel() {
        this.items = new ArrayList<PixelLogicUICarouselItem>();
        this.index = 0;
    }

    public void add(PixelLogicUICarouselItem item) {
        this.items.add(item);
    }


}
