package com.wishtoday.packetregister.ClassLoader;

public class SimpleClassLoader extends ClassLoader{

    public SimpleClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class<?> loadClass(String name
            , byte[] classBytes) {
//        if (!name.startsWith("com.wishtoday")) return super.loadClass()
        Class<?> aClass = defineClass(name, classBytes, 0, classBytes.length);
        return aClass;
    }
}
