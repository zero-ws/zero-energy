package io.zerows.core.web.io.uca.routing;

import io.horizon.specification.boot.HAxis;
import io.horizon.uca.cache.Cc;
import io.vertx.up.util.Ut;
import io.zerows.core.web.model.atom.running.RunServer;
import org.osgi.framework.Bundle;

import java.util.Objects;

/**
 * 新接口，用于替换原始的路由执行器等相关处理操作，动态管理发布过程中可执行的部分，针对不同发布执行相关处理，此处
 * {@link RunServer} 内置包含了
 *
 * @author lang : 2024-05-04
 */
public interface OAxis extends HAxis<RunServer> {

    Cc<String, OAxis> CCT_SKELETON = Cc.openThread();

    static <T extends OAxis> OAxis ofOr(final Class<T> clazz) {
        Objects.requireNonNull(clazz);
        return CCT_SKELETON.pick(() -> Ut.instance(clazz), clazz.getName());
    }

    @Override
    default void mount(final RunServer server) {
        this.mount(server, null);
    }

    void mount(RunServer server, Bundle bundle);
}
