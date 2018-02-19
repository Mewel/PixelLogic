package de.mewel.pixellogic.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
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

    private static int FONT_ITERATIONS = 20;

    private static int LEVEL_FONT_SIZE = 5;

    private static String LEVEL_FONT_PREFIX = "level_font_";

    private static String LEVEL_DIRECTORY = "level";

    private AssetManager manager;

    public PixelLogicAssets() {
        manager = new AssetManager();
    }

    public void load() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        //manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        manager.setLoader(BitmapFont.class, ".fnt", new BitmapFontLoader(resolver));
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
        loadFont("fonts/numbers.fnt", LEVEL_FONT_PREFIX);
        //loadFont("fonts/ObelusCompact.ttf", LEVEL_FONT_PREFIX);
    }
/*
    protected void loadFont(String path, String prefix) {
        for (int i = LEVEL_FONT_SIZE; i <= (LEVEL_FONT_SIZE * FONT_ITERATIONS); i += LEVEL_FONT_SIZE) {
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
*/
    protected void loadFont(String path, String prefix) {
        for (int i = 1; i <= FONT_ITERATIONS; i++) {
            String assetName = prefix + i + ".fnt";
            BitmapFontLoader.BitmapFontParameter parameter = new BitmapFontLoader.BitmapFontParameter();
            parameter.flip = true;
            parameter.bitmapFontData = new BitmapFont.BitmapFontData();
            parameter.bitmapFontData.scale(i);
            manager.load(path, BitmapFont.class, parameter);
        }
    }

    public BitmapFont getLevelFont(int size) {
        int fixedSize = (int) Math.ceil(size / LEVEL_FONT_SIZE) * LEVEL_FONT_SIZE;
        fixedSize = Math.min(fixedSize, FONT_ITERATIONS * LEVEL_FONT_SIZE);
        return manager.get(LEVEL_FONT_PREFIX + fixedSize + ".ttf", BitmapFont.class);
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
