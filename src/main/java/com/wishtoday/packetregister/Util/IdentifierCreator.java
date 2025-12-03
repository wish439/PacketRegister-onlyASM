package com.wishtoday.packetregister.Util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import net.minecraft.util.Identifier;

@Log4j2
public class IdentifierCreator {
    @Setter @Getter
    private static String nameSpace = "";
    //PacketDefault -> ptdf
    //⬆    ⬆⬆ ⬆
    private static final String DEFAULT_NAMESPACE = "ptdf";
    public static Identifier create(String path) {
        if (nameSpace.isEmpty()) {
            log.warn("IdentifierCreator's nameSpace is empty, will use default {}", DEFAULT_NAMESPACE);
            return Identifier.of(DEFAULT_NAMESPACE, path);
        }
        return Identifier.of(nameSpace, path);
    }
}
