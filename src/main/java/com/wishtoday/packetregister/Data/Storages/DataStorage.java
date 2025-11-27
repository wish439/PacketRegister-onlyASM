package com.wishtoday.packetregister.Data.Storages;

public interface DataStorage {
    class DataStorageFactory {
        public static <V> DefaultStorage<V> createDataStorage(Class<V> clazz) {
            return new DefaultStorage<>();
        }
        public static BooleanStorage createBooleanStorage() {
            return new BooleanStorage();
        }
        public static IntStorage createIntStorage() {
            return new IntStorage();
        }
    }
}
