package com.wishtoday.packetregister.ClassLoader;

public class SimpleClassLoader extends ClassLoader{

    public Class<?> loadClass(String name
            , byte[] classBytes) {
        return defineClass(name, classBytes, 0, classBytes.length);
    }
}
