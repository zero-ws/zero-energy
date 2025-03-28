package io.zerows.core.feature.web.websocket.store;

import io.horizon.eon.VString;
import io.vertx.up.annotations.Address;
import io.vertx.up.eon.DefaultClass;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.feature.web.websocket.annotations.Subscribe;
import io.zerows.core.feature.web.websocket.atom.Remind;
import io.zerows.core.feature.web.websocket.eon.em.RemindType;
import io.zerows.core.web.model.uca.extract.Extractor;
import io.zerows.core.web.model.uca.extract.ToolMethod;
import io.zerows.core.web.model.uca.extract.ToolVerifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SockExtractor implements Extractor<Set<Remind>> {

    @Override
    public Set<Remind> extract(final Class<?> clazz) {
        return Fn.runOr(new HashSet<>(), () -> {
            // 1. Class verify
            ToolVerifier.noArg(clazz, this.getClass());
            ToolVerifier.modifier(clazz, this.getClass());
            // 2. Scan method to find @WebSocket
            final Set<Remind> websockets = new HashSet<>();
            final Method[] methods = clazz.getDeclaredMethods();
            Arrays.stream(methods)
                .filter(ToolMethod::isValid)
                .filter(method -> method.isAnnotationPresent(Subscribe.class))
                .map(this::extract)
                .forEach(websockets::add);
            return websockets;
        }, clazz);
    }

    private Remind extract(final Method method) {
        final Class<?> clazz = method.getDeclaringClass();
        // 1. Scan whole Endpoints
        final Annotation annotation = method.getDeclaredAnnotation(Subscribe.class);
        String address = Ut.invoke(annotation, KName.VALUE);
        /*
         * If the address is not start with "/", the system convert the address value
         * from direct address to the normalized path.
         *
         * For example:
         *
         * job/notify       -> /job/notify
         */
        if (!address.startsWith(VString.SLASH)) {
            address = VString.SLASH + address;
        }
        final RemindType type = Ut.invoke(annotation, KName.TYPE);
        // 2. Build Remind
        final Remind remind = new Remind();
        remind.setMethod(method);
        remind.setSubscribe(address);
        remind.setType(type);

        // Fix: Instance class for proxy
        //        final Object proxy = PLUGIN.createInstance(clazz);
        remind.setProxy(clazz);
        remind.setName(Ut.invoke(annotation, KName.NAME));
        remind.setSecure(Ut.invoke(annotation, "secure"));
        // Input Part: input / inputAddress
        final Annotation annoAddr = method.getDeclaredAnnotation(Address.class);
        final String inputAddress = Ut.invoke(annoAddr, KName.VALUE);
        if (Ut.isNotNil(inputAddress)) {
            remind.setAddress(inputAddress);
            final Class<?> inputCls = Ut.invoke(annotation, "input");
            if (DefaultClass.class != inputCls) {
                remind.setInput(inputCls);
            }
        }
        final String sockAddr = address;
        Fn.runAt(() -> this.logger().info(INFO.SOCK_HIT, clazz, sockAddr, inputAddress));
        return remind;
    }
}
