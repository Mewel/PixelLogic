package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUIAboutPage extends PixelLogicUIBasePage {

    private List<TextNode> nodes;

    public PixelLogicUIAboutPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.about, "About", PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void build() {
        String intro = "[TEXT_COLOR]Thank you for playing [MAIN_COLOR]pixel logic[]! I hope you had" +
                " as much fun playing as I enjoyed developing the game. If you find any bugs, have" +
                " anything to criticize or just want to make a comment, please feel free to write me:" +
                " [MAIN_COLOR]pixellogic@gmail.com[].";
        String thanks = "[TEXT_COLOR]I developed this game in my free time, and without help, I couldn't have" +
                " done it. Big thanks to all the artists who created those beautiful and free to use" +
                " pixel art I used as puzzles. Here's a list of those awesome people:";
        List<String> pixelArtists = new ArrayList<String>();
        pixelArtists.add("Johan Vinet");
        pixelArtists.add("Luis Zuno");
        pixelArtists.add("Alex's Assets");
        pixelArtists.add("Refuzzle");
        pixelArtists.add("Bakudas");
        pixelArtists.add("Davit Masia");
        pixelArtists.add("Kyrise");

        String jose = "[MAIN_COLOR]Developed for, dedicated to, and inspired by the wonderful Jose. Without you," +
                " this game wouldn't exist.";

        getPageRoot().space(0);
        this.nodes = new ArrayList<TextNode>();
        this.nodes.add(new TextNode(intro, 1));
        this.nodes.add(new TextNode(thanks, 1));
        StringBuilder artists = new StringBuilder("[TEXT_COLOR]");
        for (String artist : pixelArtists) {
            artists.append(artist).append("\n");
        }
        this.nodes.add(new TextNode(artists.toString(), 1));
        this.nodes.add(new TextNode(jose, 1));

        for (TextNode node : nodes) {
            getPageRoot().addActor(node);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        for (TextNode node : nodes) {
            node.resize();
        }
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
        fadeIn(null);
    }

    private class TextNode extends Container<HorizontalGroup> {

        private int size;

        private Container<Label> labelContainer;

        public TextNode(final String text, int size) {
            super(new HorizontalGroup());
            this.size = size;
            getActor().setFillParent(true);
            Label label = this.getLabel(text, size);
            label.setWrap(true);
            this.labelContainer = new Container<Label>(label);
            getActor().addActor(this.labelContainer);

            getActor().pack();
        }

        public void resize() {
            this.width(getComponentWidth());
            float padding = getPadding() / 2;
            this.pad(padding, 0, padding, padding * 2);
            this.labelContainer.width(getComponentWidth());
            this.labelContainer.getActor().setStyle(getLabelStyle(size));
        }

        public Label getLabel(String text, int size) {
            Label.LabelStyle style = getLabelStyle(size);
            return new Label(text, style);
        }

        private Label.LabelStyle getLabelStyle(int size) {
            BitmapFont font = PixelLogicUIUtil.getAppFont(getAssets(), size);
            return new Label.LabelStyle(font, Color.WHITE);
        }

    }

}
