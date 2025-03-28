package io.zerows.core.assembly.atom;

import io.horizon.uca.cache.Cc;

import java.lang.reflect.Proxy;

public class OProxyInstance {

    private static final Cc<Class<?>, OProxyInstance> CC_V_INSTANCE = Cc.open();
    private transient final Class<?> interfaceCls;

    private OProxyInstance(final Class<?> interfaceCls) {
        this.interfaceCls = interfaceCls;
    }

    public static OProxyInstance create(final Class<?> interfaceCls) {
        return CC_V_INSTANCE.pick(() -> new OProxyInstance(interfaceCls), interfaceCls);
        // return Fn.po?l(V_POOL, interfaceCls, () -> new OProxyInstance(interfaceCls));
    }

    @SuppressWarnings("unchecked")
    public <T> T proxy() {
        final Class<?>[] interfaces = new Class<?>[]{this.interfaceCls};
        return (T) Proxy.newProxyInstance(this.interfaceCls.getClassLoader(), interfaces, new OInvocationHandler());
    }
}
