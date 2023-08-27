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
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

import static de.mewel.pixellogic.PixelLogicConstants.BASE_SIZE;
import static de.mewel.pixellogic.PixelLogicConstants.BLOCK_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.DRAW_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.GAME_FONT;
import static de.mewel.pixellogic.PixelLogicConstants.KEY_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.LEVEL_FONT;
import static de.mewel.pixellogic.PixelLogicConstants.LEVEL_MUSIC;
import static de.mewel.pixellogic.PixelLogicConstants.MAIN_FONT;
import static de.mewel.pixellogic.PixelLogicConstants.MAIN_FONT_PT;
import static de.mewel.pixellogic.PixelLogicConstants.MENU_MUSIC;
import static de.mewel.pixellogic.PixelLogicConstants.PUZZLE_SOLVED_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.SWITCHER_SOUND;

public class PixelLogicAssets {

    private static final String GAME_FONT_PREFIX = "game_font_";

    public static final int GAME_FONT_BASE = 13;

    public static final int GAME_FONT_MIN = 13;

    private static final String LEVEL_FONT_PREFIX = "level_font_";

    public static final int LEVEL_FONT_SIZE = 5;

    private static final String MAIN_FONT_ASSET = "main_font.ttf";

    private static final String LEVEL_DIRECTORY = "level";

    private final AssetManager manager;

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
        loadLevelBackgrounds();
        loadFonts();
        loadIcons();
        loadAudio();
        loadI18n();
        //loadTextures();

        manager.finishLoading();
    }

    private void loadLogo() {
        manager.load("logo_light.png", Texture.class);
        manager.load("logo_dark.png", Texture.class);
    }

    private void loadLevels() {
        for (FileHandle collectionHandle : getLevelHandles()) {
            String collectionName = collectionHandle.path();
            manager.load(collectionName, PixelLogicLevelCollection.class);
            Gdx.app.log("assets", "level collection " + collectionName + " loaded");
        }
    }

    private void loadLevelBackgrounds() {
        manager.load("background/light.png", Texture.class);
        manager.load("background/dark.png", Texture.class);
    }

    protected void loadFonts() {
        int base = GAME_FONT_BASE * Math.round(Gdx.graphics.getDensity() * getDistanceFactor());
        int textSize = Math.max(GAME_FONT_MIN, base);
        int h2Size = Math.max(textSize + GAME_FONT_BASE, (int) (Math.ceil(textSize * 1.35f / GAME_FONT_BASE) * GAME_FONT_BASE));
        int h1Size = Math.max(textSize + GAME_FONT_BASE * 2, (int) (Math.ceil(textSize * 1.7f / GAME_FONT_BASE) * GAME_FONT_BASE));

        loadGameFont(GAME_FONT_PREFIX + "text.ttf", textSize);
        loadGameFont(GAME_FONT_PREFIX + "h1.ttf", h1Size);
        loadGameFont(GAME_FONT_PREFIX + "h2.ttf", h2Size);

        Gdx.app.log("density:", Gdx.graphics.getDensity() + "");
        Gdx.app.log("screen size inches:", PixelLogicUIUtil.getScreenSizeInches() + "");
        Gdx.app.log("base:", base + "");
        Gdx.app.log("textSize:", textSize + "");
        Gdx.app.log("h2Size:", h2Size + "");
        Gdx.app.log("h1Size:", h1Size + "");

        loadBitmapFont(LEVEL_FONT, LEVEL_FONT_PREFIX);

        loadMainFont();
    }

    private void loadGameFont(String assetName, int size) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter gameFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        gameFont.fontFileName = GAME_FONT;
        gameFont.fontParameters.color = Color.WHITE;
        gameFont.fontParameters.size = size;
        manager.load(assetName, BitmapFont.class, gameFont);
    }

    private void loadMainFont() {
        FreetypeFontLoader.FreeTypeFontLoaderParameter gameFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        gameFont.fontFileName = MAIN_FONT;
        gameFont.fontParameters.color = Color.WHITE;
        gameFont.fontParameters.size = (int) (MAIN_FONT_PT * Gdx.graphics.getDensity() * (getDistanceFactor() - 0.5f));
        manager.load(MAIN_FONT_ASSET, BitmapFont.class, gameFont);
    }

    protected float getDistanceFactor() {
        return (PixelLogicUIUtil.isDesktop() || PixelLogicUIUtil.isTablet()) ? 2f : 1.5f;
    }

    protected void loadIcons() {
        manager.load("icons.png", Texture.class);
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
        manager.load(DRAW_SOUND, Sound.class);
        manager.load(BLOCK_SOUND, Sound.class);
        manager.load(SWITCHER_SOUND, Sound.class);
        manager.load(PUZZLE_SOLVED_SOUND, Sound.class);
        manager.load(KEY_SOUND, Sound.class);

        manager.load(MENU_MUSIC, Music.class);
        manager.load(LEVEL_MUSIC, Music.class);
    }

    private void loadI18n() {
        manager.load("i18n/pixellogic", I18NBundle.class);
    }

    public BitmapFont getGameFont(int size) {
        String postfix = size == 0 ? "text" : (size == 1 ? "h2" : "h1");
        BitmapFont bitmapFont = manager.get(GAME_FONT_PREFIX + postfix + ".ttf", BitmapFont.class);
        bitmapFont.getData().markupEnabled = true;
        return bitmapFont;
    }

    public BitmapFont getLevelFont(int size) {
        int fixedSize = getFixedLevelFontSize(size);
        BitmapFont bitmapFont = manager.get(LEVEL_FONT_PREFIX + fixedSize + ".fnt", BitmapFont.class);
        bitmapFont.getData().markupEnabled = true;
        return bitmapFont;
    }

    public BitmapFont getMainFont() {
        BitmapFont bitmapFont = manager.get(MAIN_FONT_ASSET, BitmapFont.class);
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
        int fixedSize = (int) Math.ceil((float)size / (float)LEVEL_FONT_SIZE) * LEVEL_FONT_SIZE;
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
        List<PixelLogicLevelCollection> collections = new ArrayList<>();
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
        List<FileHandle> handles = new ArrayList<>();
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

    public Sprite getIcon(int index) {
        Texture icons = manager.get("icons.png", Texture.class);
        return new Sprite(icons, BASE_SIZE * index, 0, BASE_SIZE, BASE_SIZE);
    }

    public String translate(String key) {
        I18NBundle bundle = manager.get("i18n/pixellogic", I18NBundle.class);
        return bundle.get(key);
    }

    public void dispose() {
        this.manager.dispose();
        if (this.shapeRenderer != null) {
            this.shapeRenderer.dispose();
        }
    }
}
