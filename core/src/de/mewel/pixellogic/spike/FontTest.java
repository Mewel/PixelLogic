package de.mewel.pixellogic.spike;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FontTest extends Game {

    private static Color BG_COLOR = Color.BLACK;

    @Override
    public void create() {
        this.setScreen(new TestScreen());
    }

    private static class TestScreen implements Screen {

        private Stage stage;

        public TestScreen() {
            this.stage = new Stage();
            int pos = 10;

            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/visitor2.ttf"));

            for(int i = 13; i <= 64; i += 13) {
                FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
                params.size = i;
                params.color = Color.WHITE;
                BitmapFont font = generator.generateFont(params);
                Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
                Label actor = new Label(i + ": test 1234567890", style);
                actor.setPosition(20, pos);
                pos += i;
                stage.addActor(actor);
            }

            //PixelLogicAssets assets = new PixelLogicAssets();
            //assets.load();


/*            TextureAtlas textureAtlas = assets.get().get("target/level.atlas", TextureAtlas.class);
            Sprite sprite = textureAtlas.createSprite("characters/example");
            this.stage.addActor(new Image(sprite));
*/

/*
            for(int i = 16; i <= 128; i += 16) {
                BitmapFont levelFont = assets.getLevelFont(i);
                Label.LabelStyle style = new Label.LabelStyle(levelFont, Color.RED);
                Label actor = new Label(i + ": test 1234567890", style);
                actor.setPosition(20, pos);
                pos += i;
                stage.addActor(actor);
            }
*/

/*
            BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/numbers.fnt"), true);
            font.getData().scale(5f);

            Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
            Label actor = new Label("21 36 11 999", style);
            actor.setPosition(0, 0);
            stage.addActor(actor);
 */
        }

        @Override
        public void show() {

        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(BG_COLOR.r, BG_COLOR.g, BG_COLOR.b, BG_COLOR.a);
            Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

            this.stage.act(delta);
            this.stage.draw();
        }

        @Override
        public void resize(int width, int height) {
            OrthographicCamera camera = new OrthographicCamera();
            camera.setToOrtho(true);
            stage.setViewport(new ExtendViewport(width, height, camera));
            this.stage.getViewport().update(width, height);
        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void hide() {

        }

        @Override
        public void dispose() {

        }

    }

}
