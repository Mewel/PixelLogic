package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUIAboutPage extends PixelLogicUIBasePage {

    private List<Actor> nodes;

    public PixelLogicUIAboutPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.about, global.getAssets().translate("about"), PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void build() {
        String intro = getAssets().translate("about.intro");
        TextNode emailLink = new TextNode("[MAIN_COLOR]pixellogic.de@gmail.com");
        emailLink.getLabel().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("mailto:pixellogic.de@gmail.com");
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        String gitHub = "[TEXT_COLOR]GitHub";
        String gitHubText = getAssets().translate("about.githubText");
        TextNode gitHubLink = new TextNode("[MAIN_COLOR]https://github.com/Mewel/pixellogic");
        gitHubLink.getLabel().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.net.openURI("https://github.com/Mewel/pixellogic");
                return super.touchDown(event, x, y, pointer, button);
            }
        });

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
        this.nodes = new ArrayList<Actor>();
        this.nodes.add(new TextNode(intro));
        this.nodes.add(emailLink);
        this.nodes.add(new HeaderNode(gitHub));
        this.nodes.add(new TextNode(gitHubText));
        this.nodes.add(gitHubLink);
        this.nodes.add(new HeaderNode(credits));
        this.nodes.add(new TextNode(creditsText.toString()));
        this.nodes.add(new HeaderNode(privacy));
        this.nodes.add(new TextNode(privacyText));

        for (Actor node : nodes) {
            getPageRoot().addActor(node);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        int componentWidth = getComponentWidth();
        for (Actor node : nodes) {
            node.setWidth(componentWidth);
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

        @Override
        protected void sizeChanged() {
            super.sizeChanged();
            this.labelContainer.width(getComponentWidth());
            this.labelContainer.getActor().setStyle(getLabelStyle());
        }

        public Label getLabel() {
            return label;
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
            this.labelContainer.padTop(24f * Gdx.graphics.getDensity());
            this.labelContainer.padBottom(8f * Gdx.graphics.getDensity());
        }

        protected Label.LabelStyle getLabelStyle() {
            BitmapFont font = PixelLogicUIUtil.getAppFont(getAssets(), 1);
            return new Label.LabelStyle(font, Color.WHITE);
        }

    }

}
