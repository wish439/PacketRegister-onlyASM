package com.wishtoday.packetregister.Data.Storages;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

public class BooleanStorage {
    private Object2BooleanOpenHashMap<String> booleanMap;
    BooleanStorage() {
        this.booleanMap = new Object2BooleanOpenHashMap<>();
    }
    public boolean visit(String key) {
        return this.booleanMap.getBoolean(key);
    }

    public void visit(String key, boolean value) {
        this.booleanMap.put(key, value);
    }
}
