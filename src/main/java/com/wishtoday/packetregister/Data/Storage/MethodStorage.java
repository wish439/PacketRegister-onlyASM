package com.wishtoday.packetregister.Data.Storage;

import com.wishtoday.packetregister.Util.DescUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Getter
@Setter
@Log4j2
public final class MethodStorage extends Storage{
    private String[] args;
    private MethodStorage(String classPath, String elementName, String[] args) {
        super(classPath, elementName);
        for (int i = 0; i < args.length; i++) {
            String s = DescUtils.checkDesc(args[i]);
            if (s == null) log.warn("MethodStorage.<init> Argument {} is invalid", args[i]);
            args[i] = s;
        }
        this.args = args;
    }
    public static MethodStorageBuilder builder(){
        return new MethodStorageBuilder();
    }
    public static class MethodStorageBuilder {
        private String classPath;
        private String elementName;
        private String[] args;
        public MethodStorageBuilder classPath(String classPath) {
            this.classPath = classPath;
            return this;
        }
        public MethodStorageBuilder elementName(String elementName) {
            this.elementName = elementName;
            return this;
        }
        public MethodStorageBuilder args(String[] args) {
            this.args = args;
            return this;
        }
        public MethodStorage build(){
            return new MethodStorage(classPath, elementName, args);
        }
    }
}
