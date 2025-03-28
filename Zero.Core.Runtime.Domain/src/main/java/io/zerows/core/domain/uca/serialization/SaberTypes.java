package io.zerows.core.domain.uca.serialization;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.math.BigDecimal;
import java.util.*;

class SaberTypes {

    static final Set<Class<?>> NATIVE =
        new HashSet<Class<?>>() {
            {
                this.add(int.class);
                this.add(Integer.class);
                this.add(short.class);
                this.add(Short.class);
                this.add(double.class);
                this.add(Double.class);
                this.add(long.class);
                this.add(Long.class);
                this.add(boolean.class);
                this.add(Boolean.class);
                this.add(float.class);
                this.add(Float.class);
                this.add(JsonObject.class);
                this.add(JsonArray.class);
                this.add(String.class);
                this.add(byte[].class);
                this.add(Byte[].class);
                this.add(byte.class);
                this.add(Byte.class);
            }
        };

    static final Set<Class<?>> SUPPORTED =
        new HashSet<Class<?>>() {
            {
                this.addAll(NATIVE);
                this.add(Date.class);
                this.add(StringBuffer.class);
                this.add(StringBuilder.class);
                this.add(Calendar.class);
                this.add(Buffer.class);
                this.add(BigDecimal.class);
                this.add(Enum.class);
                this.add(Collection.class);
                this.add(Set.class);
                this.add(List.class);
            }
        };

    public static <T> boolean isNative(final T input) {
        if (null == input) {
            return false;
        }
        final Class<?> type = input.getClass();
        return NATIVE.contains(type);
    }

    public static <T> boolean isSupport(final Class<?> inputCls) {
        if (null == inputCls) {
            return false;
        }
        return SUPPORTED.contains(inputCls);
    }
}
