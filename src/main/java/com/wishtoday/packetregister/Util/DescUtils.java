package com.wishtoday.packetregister.Util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DescUtils {
    @NotNull
    public static String checkDesc(String s) {
        if (s.isEmpty()) return s;
        if (s.contains(".")) return "L" + s.replace(".", "/") + ";";
        return s;
    }
    @Nullable
    public static String checkInternal(String s) {
        if (s.isEmpty()) return null;
        if (s.contains(".")) return s.replace(".", "/");
        return s;
    }
}
