package de.mewel.pixellogic.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.PixmapLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevelCollection;

public class PixelLogicAssets {

    private static String GAME_FONT_PREFIX = "game_font_";

    public static int GAME_FONT_SIZE = 13;

    private static int GAME_FONT_ITERATIONS = 7;

    private static String LEVEL_FONT_PREFIX = "level_font_";

    private static int FONT_ITERATION_START = 2;

    private static int FONT_ITERATION_END = 20;

    public static int LEVEL_FONT_SIZE = 5;

    private static String LEVEL_DIRECTORY = "level";

    private AssetManager manager;

    public PixelLogicAssets() {
        manager = new AssetManager();
    }

    public void load() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        manager.setLoader(BitmapFont.class, ".fnt", new PixelLogicBitmapFontLoader(resolver));
        //manager.setLoader(TextureAtlas.class, new TextureAtlasLoader(resolver));
        manager.setLoader(Pixmap.class, new PixmapLoader(resolver));

        manager.setLoader(PixelLogicLevelCollection.class, new PixelLogicLevelCollectionLoader(resolver));

        loadLevels();
        loadFonts();
        //loadTextures();

        manager.finishLoading();
    }

    protected void loadLevels() {
        for (FileHandle collectionHandle : getLevelHandles()) {
            String collectionName = collectionHandle.path();
            manager.load(collectionName, PixelLogicLevelCollection.class);
            Gdx.app.log("assets", "level collection " + collectionName + " loaded");
        }
    }

    protected void loadTextures() {
        // manager.load("target/level.atlas", TextureAtlas.class);
    }

    protected void loadFonts() {
        loadTTFFont("fonts/visitor2.ttf", GAME_FONT_PREFIX);
        loadBitmapFont("fonts/numbers.fnt", LEVEL_FONT_PREFIX);
    }

    protected void loadTTFFont(String path, String prefix) {
        for (int i = GAME_FONT_SIZE; i <= (GAME_FONT_SIZE * GAME_FONT_ITERATIONS); i += GAME_FONT_SIZE) {
            String assetName = prefix + i + ".ttf";
            FreetypeFontLoader.FreeTypeFontLoaderParameter gameFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
            gameFont.fontFileName = path;
            gameFont.fontParameters.color = Color.WHITE;
            gameFont.fontParameters.flip = true;
            gameFont.fontParameters.size = i;
            manager.load(assetName, BitmapFont.class, gameFont);
            Gdx.app.log("assets", "font " + assetName + " loaded");
        }
    }

    protected void loadBitmapFont(String path, String prefix) {
        for (int i = FONT_ITERATION_START; i <= FONT_ITERATION_END; i++) {
            String assetName = prefix + (i * LEVEL_FONT_SIZE) + ".fnt";
            PixelLogicBitmapFontLoader.PixelLogicBitmapFontLoaderParameters params = new PixelLogicBitmapFontLoader.PixelLogicBitmapFontLoaderParameters();
            params.fileName = path;
            params.flip = true;
            params.scale = i;
            manager.load(assetName, BitmapFont.class, params);
            Gdx.app.log("assets", "font " + assetName + " loaded");
        }
    }

    public BitmapFont getGameFont(int size) {
        int fixedSize = (int) Math.ceil(size / GAME_FONT_SIZE) * GAME_FONT_SIZE;
        fixedSize = Math.min(fixedSize, GAME_FONT_SIZE * GAME_FONT_ITERATIONS);
        fixedSize = Math.max(fixedSize, GAME_FONT_SIZE);
        return manager.get(GAME_FONT_PREFIX + fixedSize + ".ttf", BitmapFont.class);
    }

    public BitmapFont getLevelFont(int size) {
        int fixedSize = (int) Math.ceil(size / LEVEL_FONT_SIZE) * LEVEL_FONT_SIZE;
        fixedSize = Math.max(fixedSize, FONT_ITERATION_START * LEVEL_FONT_SIZE);
        fixedSize = Math.min(fixedSize, FONT_ITERATION_END * LEVEL_FONT_SIZE);
        return manager.get(LEVEL_FONT_PREFIX + fixedSize + ".fnt", BitmapFont.class);
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

    private List<FileHandle> getLevelHandles() {
        List<FileHandle> handles = new ArrayList<FileHandle>();
        FileHandle levelFolder = Gdx.files.internal(LEVEL_DIRECTORY);
        FileHandle[] collectionsHandle = levelFolder.list();
        for (FileHandle collectionHandle : collectionsHandle) {
            if (!collectionHandle.isDirectory() ||
                    !collectionHandle.child("collection.json").exists() ||
                    !collectionHandle.child("pixmap.png").exists()) {
                continue;
            }
            handles.add(collectionHandle);
        }
        return handles;
    }

    public void dispose() {
        this.manager.dispose();
    }
}
