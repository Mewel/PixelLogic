package de.mewel.pixellogic.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.PixmapLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevelCollection;

import static de.mewel.pixellogic.PixelLogicConstants.BASE_SIZE;
import static de.mewel.pixellogic.PixelLogicConstants.BLOCK_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.KEY_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.PIXEL_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.PUZZLE_SOLVED_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.SWITCHER_SOUND;

public class PixelLogicAssets {

    private static String GAME_FONT_PREFIX = "game_font_";

    public static int GAME_FONT_SIZE = 13;

    private static int GAME_FONT_ITERATIONS = 10;

    private static String LEVEL_FONT_PREFIX = "level_font_";

    public static int LEVEL_FONT_SIZE = 5;

    private static String LEVEL_DIRECTORY = "level";

    private AssetManager manager;

    private ShapeRenderer shapeRenderer;

    public PixelLogicAssets() {
        manager = new AssetManager();
    }

    public void load() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        manager.setLoader(BitmapFont.class, ".fnt", new PixelLogicBitmapFontLoader(resolver));
        manager.setLoader(Pixmap.class, new PixmapLoader(resolver));
        manager.setLoader(Texture.class, new TextureLoader(resolver));
        manager.setLoader(Sound.class, new SoundLoader(resolver));
        manager.setLoader(Music.class, new MusicLoader(resolver));
        manager.setLoader(PixelLogicLevelCollection.class, new PixelLogicLevelCollectionLoader(resolver));

        loadLogo();
        loadLevels();
        loadFonts();
        loadIcons();
        loadAudio();
        //loadTextures();

        manager.finishLoading();
    }

    private void loadLogo() {
        manager.load("logo.png", Texture.class);
    }

    private void loadLevels() {
        for (FileHandle collectionHandle : getLevelHandles()) {
            String collectionName = collectionHandle.path();
            manager.load(collectionName, PixelLogicLevelCollection.class);
            Gdx.app.log("assets", "level collection " + collectionName + " loaded");
        }
    }

    protected void loadFonts() {
        loadTTFFont("fonts/visitor2.ttf", GAME_FONT_PREFIX);
        loadBitmapFont("fonts/numbers.fnt", LEVEL_FONT_PREFIX);
    }

    protected void loadIcons() {
        manager.load("icons.png", Texture.class);
    }

    protected void loadTTFFont(String path, String prefix) {
        for (int i = GAME_FONT_SIZE; i <= (GAME_FONT_SIZE * GAME_FONT_ITERATIONS); i += GAME_FONT_SIZE) {
            String assetName = prefix + i + ".ttf";
            FreetypeFontLoader.FreeTypeFontLoaderParameter gameFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
            gameFont.fontFileName = path;
            gameFont.fontParameters.color = Color.WHITE;
            gameFont.fontParameters.size = i;
            manager.load(assetName, BitmapFont.class, gameFont);
            Gdx.app.log("assets", "font " + assetName + " loaded");
        }
    }

    protected void loadBitmapFont(String path, String prefix) {
        for (int i = getLevelFontIterationStart(); i <= getLevelFontIterationEnd(); i++) {
            String assetName = prefix + (i * LEVEL_FONT_SIZE) + ".fnt";
            PixelLogicBitmapFontLoader.PixelLogicBitmapFontLoaderParameters params = new PixelLogicBitmapFontLoader.PixelLogicBitmapFontLoaderParameters();
            params.fileName = path;
            params.scale = i;
            manager.load(assetName, BitmapFont.class, params);
            Gdx.app.log("assets", "font " + assetName + " loaded");
        }
    }

    private void loadAudio() {
        manager.load(BUTTON_SOUND, Sound.class);
        manager.load(PIXEL_SOUND, Sound.class);
        manager.load(BLOCK_SOUND, Sound.class);
        manager.load(SWITCHER_SOUND, Sound.class);
        manager.load(PUZZLE_SOLVED_SOUND, Sound.class);
        manager.load(KEY_SOUND, Sound.class);
    }

    public BitmapFont getGameFont(int base, int size) {
        int baseSize = (int) Math.ceil(base / GAME_FONT_SIZE) * GAME_FONT_SIZE;
        int fixedSize = baseSize + size * GAME_FONT_SIZE;
        fixedSize = Math.min(fixedSize, GAME_FONT_SIZE * GAME_FONT_ITERATIONS);
        fixedSize = Math.max(fixedSize, GAME_FONT_SIZE);
        BitmapFont bitmapFont = manager.get(GAME_FONT_PREFIX + fixedSize + ".ttf", BitmapFont.class);
        bitmapFont.getData().markupEnabled = true;
        return bitmapFont;
    }

    public BitmapFont getLevelFont(int size) {
        int fixedSize = getFixedLevelFontSize(size);
        BitmapFont bitmapFont = manager.get(LEVEL_FONT_PREFIX + fixedSize + ".fnt", BitmapFont.class);
        bitmapFont.getData().markupEnabled = true;
        return bitmapFont;
    }

    public static int getLevelFontIterationStart() {
        return Math.max(2, (int) (2 * Gdx.app.getGraphics().getDensity()));
    }

    public static int getLevelFontIterationEnd() {
        return getLevelFontIterationStart() * 10;
    }

    public static int getFixedLevelFontSize(int size) {
        int fixedSize = (int) Math.ceil(size / LEVEL_FONT_SIZE) * LEVEL_FONT_SIZE;
        fixedSize = Math.max(fixedSize, getLevelFontIterationStart() * LEVEL_FONT_SIZE);
        fixedSize = Math.min(fixedSize, getLevelFontIterationEnd() * LEVEL_FONT_SIZE);
        return fixedSize;
    }

    public PixelLogicLevelCollection getLevelCollection(String name) {
        try {
            String resolvedName = name.startsWith(LEVEL_DIRECTORY) ? name : LEVEL_DIRECTORY + "/" + name;
            return manager.get(resolvedName, PixelLogicLevelCollection.class);
        } catch (Exception exc) {
            Gdx.app.log("assets", "Unable to get level collection " + name);
            return null;
        }
    }

    public List<PixelLogicLevelCollection> listLevels() {
        List<PixelLogicLevelCollection> collections = new ArrayList<PixelLogicLevelCollection>();
        for (FileHandle collectionHandle : getLevelHandles()) {
            PixelLogicLevelCollection levelCollection = getLevelCollection(collectionHandle.path());
            if (levelCollection != null) {
                collections.add(levelCollection);
            }
        }
        return collections;
    }

    public AssetManager get() {
        return manager;
    }

    public ShapeRenderer getShapeRenderer() {
        if (shapeRenderer == null) {
            shapeRenderer = new ShapeRenderer();
        }
        return shapeRenderer;
    }

    private List<FileHandle> getLevelHandles() {
        FileHandle levelFolder = Gdx.files.internal(LEVEL_DIRECTORY);
        FileHandle[] collectionsHandle = levelFolder.list();
        return getLevelHandles(collectionsHandle);
    }

    private List<FileHandle> getLevelHandles(FileHandle[] collectionsHandle) {
        List<FileHandle> handles = new ArrayList<FileHandle>();
        for (FileHandle collectionHandle : collectionsHandle) {
            if (!collectionHandle.isDirectory()) {
                continue;
            }
            if (collectionHandle.child("collection.json").exists() && collectionHandle.child("pixmap.png").exists()) {
                handles.add(collectionHandle);
                continue;
            }
            handles.addAll(getLevelHandles(collectionHandle.list()));
        }
        return handles;
    }

    public Texture getLogo() {
        return manager.get("logo.png", Texture.class);
    }

    public Sprite getIcon(int index) {
        Texture icons = manager.get("icons.png", Texture.class);
        return new Sprite(icons, BASE_SIZE * index, 0, BASE_SIZE, BASE_SIZE);
    }

    public void dispose() {
        if (this.manager != null) {
            this.manager.dispose();
        }
        if (this.shapeRenderer != null) {
            this.shapeRenderer.dispose();
        }
    }

}
