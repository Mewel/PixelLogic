package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUIAboutPage extends PixelLogicUIBasePage {

    private List<TextNode> nodes;

    public PixelLogicUIAboutPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.about, global.getAssets().translate("about"), PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void build() {
        String intro = getAssets().translate("about.intro");
        String credits = getAssets().translate("about.credits");
        String privacy = getAssets().translate("about.privacy");
        String privacyText = getAssets().translate("about.privacyText");
        StringBuilder creditsText = new StringBuilder(getAssets().translate("about.programming"));
        creditsText.append("\n[MAIN_COLOR]mewel");
        creditsText.append("\n\n").append(getAssets().translate("about.pixelArtists"));

        List<String> pixelArtists = new ArrayList<String>();
        pixelArtists.add("Johan Vinet");
        pixelArtists.add("Luis Zuno");
        pixelArtists.add("Alex's Assets");
        pixelArtists.add("Refuzzle");
        pixelArtists.add("Bakudas");
        pixelArtists.add("Davit Masia");
        pixelArtists.add("Kyrise");
        pixelArtists.add("mewel");

        for (String artist : pixelArtists) {
            creditsText.append(artist).append("\n");
        }

        creditsText.append("\n").append(getAssets().translate("about.music"));
        creditsText.append("Tobias Weber - Between Worlds").append("\n");
        creditsText.append("Tobias Weber - The Parting Glas").append("\n");

        creditsText.append("\n").append(getAssets().translate("about.thirdParty"));
        List<String> thirdParty = new ArrayList<String>();
        thirdParty.add("libgdx");
        thirdParty.add("google quicksand regular font");
        thirdParty.add("visitor2 font");

        for (String text : thirdParty) {
            creditsText.append(text).append("\n");
        }

        creditsText.append("\n").append(getAssets().translate("about.jose"));

        getPageRoot().space(0);
        this.nodes = new ArrayList<TextNode>();
        this.nodes.add(new TextNode(intro));
        this.nodes.add(new HeaderNode(credits));
        this.nodes.add(new TextNode(creditsText.toString()));
        this.nodes.add(new HeaderNode(privacy));
        this.nodes.add(new TextNode(privacyText));

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

        protected Label label;

        protected Container<Label> labelContainer;

        public TextNode(final String text) {
            super(new HorizontalGroup());
            getActor().setFillParent(true);
            this.label = this.getLabel(text);
            this.label.setWrap(true);
            this.labelContainer = new Container<Label>(label);
            getActor().addActor(this.labelContainer);
            getActor().pack();
        }

        public void resize() {
            this.width(getComponentWidth());
            float padding = getPadding() / 2;
            this.pad(padding, 0, padding, padding * 2);
            this.labelContainer.width(getComponentWidth());
            this.labelContainer.getActor().setStyle(getLabelStyle());
        }

        protected Label getLabel(String text) {
            Label.LabelStyle style = getLabelStyle();
            return new Label(text, style);
        }

        protected Label.LabelStyle getLabelStyle() {
            BitmapFont font = PixelLogicUIUtil.getMainFont(getAssets());
            return new Label.LabelStyle(font, Color.WHITE);
        }

    }

    private class HeaderNode extends TextNode {

        public HeaderNode(String text) {
            super(text);
            this.label.setAlignment(Align.center);
            this.labelContainer.padTop(12f + Gdx.graphics.getDensity());
        }

        protected Label.LabelStyle getLabelStyle() {
            BitmapFont font = PixelLogicUIUtil.getAppFont(getAssets(), 1);
            return new Label.LabelStyle(font, Color.WHITE);
        }

    }

}
