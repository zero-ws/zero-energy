package io.zerows.core.feature.web.websocket.store;

import io.vertx.up.fn.Fn;
import io.zerows.core.feature.web.websocket.annotations.Subscribe;
import io.zerows.core.feature.web.websocket.atom.Remind;
import io.zerows.core.feature.web.websocket.eon.MessageOfSock;
import io.zerows.core.metadata.zdk.uca.Inquirer;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SockInquirer implements Inquirer<Set<Remind>> {

    @Override
    public Set<Remind> scan(final Set<Class<?>> clazzes) {
        final Set<Class<?>> endpoints = clazzes.stream()
            .filter(this::isSocked)
            .collect(Collectors.toSet());
        this.logger().info(MessageOfSock.WEBSOCKET, endpoints.size());
        final List<SockThread> threadReference = new ArrayList<>();
        /* 2.1.Build Api metadata **/
        for (final Class<?> endpoint : endpoints) {
            final SockThread thread =
                new SockThread(endpoint);
            threadReference.add(thread);
            thread.start();
        }
        /* 3.2. Join **/
        Fn.jvmAt(() -> {
            for (final SockThread item : threadReference) {
                item.join();
            }
        }, this.logger());
        /* 3.3. Finally **/
        final Set<Remind> events = new HashSet<>();
        Fn.jvmAt(() -> {
            for (final SockThread item : threadReference) {
                events.addAll(item.getEvents());
            }
        }, this.logger());
        return events;
    }

    private boolean isSocked(final Class<?> clazz) {
        final Method[] methods = clazz.getDeclaredMethods();
        final long counter = Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(Subscribe.class))
            .count();
        return 0 < counter;
    }
}
