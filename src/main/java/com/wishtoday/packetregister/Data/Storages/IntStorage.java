package com.wishtoday.packetregister.Data.Storages;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class IntStorage implements DataStorage{
    private Object2IntOpenHashMap<String> booleanMap;
    public void visit(String key, int value) {
        this.booleanMap.put(key, value);
    }
    public int get(String key) {
        return this.booleanMap.getInt(key);
    }
}
