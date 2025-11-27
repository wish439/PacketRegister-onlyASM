package com.wishtoday.packetregister.Data.Storage;

public final class FieldStorage extends Storage{
    private FieldStorage(String classPath, String elementName) {
        super(classPath, elementName);
    }
    public static FieldStorageBuilder builder() {
        return new FieldStorageBuilder();
    }

    public static class FieldStorageBuilder{
        private String classPath;
        private String elementName;
        public FieldStorageBuilder classPath(String classPath){
            this.classPath = classPath;
            return this;
        }
        public FieldStorageBuilder elementName(String elementName){
            this.elementName = elementName;
            return this;
        }
        public FieldStorage build(){
            return new FieldStorage(classPath, elementName);
        }
    }
}
