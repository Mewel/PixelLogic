package de.mewel.pixellogic.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class PixelLogicBitmapFontLoader extends AsynchronousAssetLoader<BitmapFont, PixelLogicBitmapFontLoader.PixelLogicBitmapFontLoaderParameters> {

    private BitmapFont.BitmapFontData data;

    public PixelLogicBitmapFontLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, PixelLogicBitmapFontLoaderParameters parameter) {
    }

    @Override
    public BitmapFont loadSync(AssetManager manager, String fileName, FileHandle file, PixelLogicBitmapFontLoaderParameters parameter) {
        int n = data.getImagePaths().length;
        Array<TextureRegion> regs = new Array<>(n);
        for (int i = 0; i < n; i++) {
            regs.add(new TextureRegion(manager.get(data.getImagePath(i), Texture.class)));
        }
        return new BitmapFont(data, regs, true);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, PixelLogicBitmapFontLoaderParameters parameter) {
        Array<AssetDescriptor> deps = new Array<>();
        FileHandle fileHandle = parameter.fileName != null ? Gdx.files.internal(parameter.fileName) : file;
        data = new BitmapFont.BitmapFontData(fileHandle, parameter.flip);
        data.setScale(parameter.scale);
        for (int i = 0; i < data.getImagePaths().length; i++) {
            String path = data.getImagePath(i);
            FileHandle resolved = resolve(path);
            TextureLoader.TextureParameter textureParams = new TextureLoader.TextureParameter();
            textureParams.genMipMaps = parameter.genMipMaps;
            textureParams.minFilter = parameter.minFilter;
            textureParams.magFilter = parameter.magFilter;
            AssetDescriptor<Texture> descriptor = new AssetDescriptor<>(resolved, Texture.class, textureParams);
            deps.add(descriptor);
        }
        return deps;
    }

    public static class PixelLogicBitmapFontLoaderParameters extends AssetLoaderParameters<BitmapFont> {

        /**
         * Flips the font vertically if {@code true}. Defaults to {@code false}.
         **/
        public final boolean flip = false;

        /**
         * Generates mipmaps for the font if {@code true}. Defaults to {@code false}.
         **/
        public final boolean genMipMaps = false;

        /**
         * The {@link Texture.TextureFilter} to use when scaling down the {@link BitmapFont}. Defaults to {@link Texture.TextureFilter#Nearest}.
         */
        public final Texture.TextureFilter minFilter = Texture.TextureFilter.Nearest;

        /**
         * The {@link Texture.TextureFilter} to use when scaling up the {@link BitmapFont}. Defaults to {@link Texture.TextureFilter#Nearest}.
         */
        public final Texture.TextureFilter magFilter = Texture.TextureFilter.Nearest;

        public String fileName = null;

        public int scale = 1;

    }

}
