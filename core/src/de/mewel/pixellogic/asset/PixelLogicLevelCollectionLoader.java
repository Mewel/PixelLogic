package de.mewel.pixellogic.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;

import de.mewel.pixellogic.model.PixelLogicLevelCollection;

public class PixelLogicLevelCollectionLoader extends SynchronousAssetLoader<PixelLogicLevelCollection, PixelLogicLevelCollectionLoader.PixelLogicLevelCollectionLoaderParameters> {

    private FileHandle jsonHandle, pixmapHandle, levelmapHandle, unsolvedHandle;

    public PixelLogicLevelCollectionLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public PixelLogicLevelCollection load(AssetManager assetManager, String fileName, FileHandle file, PixelLogicLevelCollectionLoaderParameters parameter) {
        Gdx.app.log("PixelLogicLevelCollectionLoader", "load " + fileName);
        Json json = new Json();
        if (!jsonHandle.exists() || !pixmapHandle.exists()) {
            throw new GdxRuntimeException("Couldn't load '" + fileName + "'");
        }
        PixelLogicLevelCollection collection = json.fromJson(PixelLogicLevelCollection.class, jsonHandle);
        collection.setPixmap(new Pixmap(pixmapHandle));
        if (levelmapHandle.exists()) {
            collection.setLevelmap(new Pixmap(levelmapHandle));
        }
        if (unsolvedHandle.exists()) {
            collection.setUnsolvedPixmap(new Pixmap(unsolvedHandle));
        }
        return collection;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, PixelLogicLevelCollectionLoaderParameters parameter) {
        this.jsonHandle = file.child("collection.json");
        this.pixmapHandle = file.child("pixmap.png");
        this.levelmapHandle = file.child("levelmap.png");
        this.unsolvedHandle = file.child("unsolved.png");

        if (!jsonHandle.exists()) {
            throw new GdxRuntimeException("Couldn't load '" + jsonHandle.path() + "'");
        }
        if (!pixmapHandle.exists()) {
            throw new GdxRuntimeException("Couldn't load '" + pixmapHandle.path() + "'");
        }
        Array<AssetDescriptor> dependencies = new Array<>();
        dependencies.add(new AssetDescriptor<>(pixmapHandle, Pixmap.class));
        if (levelmapHandle.exists()) {
            dependencies.add(new AssetDescriptor<>(levelmapHandle, Pixmap.class));
        }
        if (unsolvedHandle.exists()) {
            dependencies.add(new AssetDescriptor<>(unsolvedHandle, Pixmap.class));
        }
        return dependencies;
    }

    static class PixelLogicLevelCollectionLoaderParameters extends AssetLoaderParameters<PixelLogicLevelCollection> {
    }

}
