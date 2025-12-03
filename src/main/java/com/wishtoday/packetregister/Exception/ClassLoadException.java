package com.wishtoday.packetregister.Exception;

public class ClassLoadException extends RuntimeException {
    public ClassLoadException(String message) {
        super(message);
    }
    public ClassLoadException(Throwable cause) {
        super(cause);
    }

    public ClassLoadException(String s, Throwable e) {
        super(s, e);
    }
}
