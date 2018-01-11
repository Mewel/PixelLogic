package de.mewel.pixellogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.model.PixelLogicLevelCollection;

public class PixelLogicCollectionManager {

    private Map<String, PixelLogicLevelCollection> collections;

    private PixelLogicCollectionManager() {
        collections = new HashMap<String, PixelLogicLevelCollection>();
    }

    public static PixelLogicCollectionManager instance() {
        return InstanceHolder.INSTANCE;
    }

    public void setup() {
        FileHandle levelFolder = Gdx.files.internal("level");
        FileHandle[] collectionsHandle = levelFolder.list();
        for (FileHandle collectionHandle : collectionsHandle) {
            FileHandle jsonHandle = collectionHandle.child("collection.json");
            if (!jsonHandle.exists()) {
                throw new RuntimeException("collection.json in " + collectionHandle.name() + " does not exists!");
            }
            Json json = new Json();
            PixelLogicLevelCollection collection = json.fromJson(PixelLogicLevelCollection.class, jsonHandle);
            collection.setId(collectionHandle.name());
            this.collections.put(collection.getId(), collection);
        }

    }

    public Collection<PixelLogicLevelCollection> getCollections() {
        return this.collections.values();
    }

    public PixelLogicLevelCollection getCollection(String id) {
        return this.collections.get(id);
    }

    private static class InstanceHolder {

        static final PixelLogicCollectionManager INSTANCE = new PixelLogicCollectionManager();

    }

}
