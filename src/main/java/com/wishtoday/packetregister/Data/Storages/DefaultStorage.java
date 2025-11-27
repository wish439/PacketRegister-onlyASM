package com.wishtoday.packetregister.Data.Storages;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

@Log4j2
public class DefaultStorage<V> implements DataStorage {
    private Object2ObjectOpenHashMap<String, V> defaultMap;
    DefaultStorage() {
        defaultMap = new Object2ObjectOpenHashMap<>();
    }
    public void visit(String key, V value) {
        defaultMap.put(key, value);
    }
    @Nullable
    public V visit(String key) {
        if (defaultMap.containsKey(key)) {
            return defaultMap.get(key);
        }
        return null;
    }


}
