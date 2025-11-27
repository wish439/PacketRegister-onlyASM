package com.wishtoday.packetregister.Data.Storage;

import com.wishtoday.packetregister.Util.DescUtils;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
@Log4j2
public sealed abstract class Storage permits FieldStorage, MethodStorage {
    private String elementName;
    private String classPath;

    protected Storage(String classPath, String elementName) {
        String s = DescUtils.checkInternal(classPath);
        if (s == null) {
            log.warn("Storage.<init> {} is empty", classPath);
        } else this.classPath = s;
        //this.classPath = classPath;
        this.elementName = elementName;
    }

    public boolean hasEmpty() {
        return classPath.isEmpty()
                || elementName.isEmpty();
    }
}
