package com.wishtoday.packetregister.Data;

import com.wishtoday.packetregister.Data.Storage.FieldStorage;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public class RegisterInfo {
    private FieldStorage ID;
    private FieldStorage CODEC;
    private boolean needCreat;

    public RegisterInfo() {
        this.needCreat = false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RegisterInfo that)) return false;
        return Objects.equals(ID, that.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }
}
