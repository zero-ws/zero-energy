package io.zerows.core.web.model.uca.extract;

import io.zerows.core.uca.log.Annal;
import io.reactivex.rxjava3.core.Observable;
import io.zerows.core.fn.Fn;
import io.zerows.core.util.Ut;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Media resolver
 * 1. consumes ( default = application/json )
 * 2. produces ( default = application/json )
 */
class ToolMedia {

    private static final Annal LOGGER = Annal.get(ToolMedia.class);

    private static final Set<MediaType> DEFAULTS = new HashSet<MediaType>() {
        {
            this.add(MediaType.WILDCARD_TYPE);
        }
    };

    /**
     * Capture the consume mime types
     *
     * @param method method reference
     *
     * @return return MIME
     */
    public static Set<MediaType> consumes(final Method method) {
        return resolve(method, Consumes.class);
    }

    /**
     * Capture the produces mime types
     *
     * @param method method reference
     *
     * @return return MIME
     */
    public static Set<MediaType> produces(final Method method) {
        return resolve(method, Produces.class);
    }

    private static Set<MediaType> resolve(final Method method,
                                          final Class<? extends Annotation>
                                              mediaCls) {
        return Fn.runOr(() -> {
            final Annotation anno = method.getAnnotation(mediaCls);
            return Fn.runOr(null == anno, LOGGER,
                () -> DEFAULTS,
                () -> {
                    final String[] value = Ut.invoke(anno, "value");
                    final Set<MediaType> result = new HashSet<>();
                    // RxJava 2
                    Observable.fromArray(value)
                        .filter(Objects::nonNull)
                        .map(MediaType::valueOf)
                        .filter(Objects::nonNull)
                        .subscribe(result::add)
                        .dispose();
                    return result.isEmpty() ? DEFAULTS : result;
                });
        }, method, mediaCls);
    }
}
