package com.wishtoday.packetregister.Generator.HandlerRegister;

import com.wishtoday.packetregister.Generator.ClassGenerator;

public abstract class AbstractHandlerRegister extends ClassGenerator implements HandlerRegister{

    protected AbstractHandlerRegister(String className, String packageName) {
        super(className, packageName);
    }


}
