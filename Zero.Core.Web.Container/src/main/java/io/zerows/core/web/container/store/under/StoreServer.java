package io.zerows.core.web.container.store.under;

import io.horizon.uca.cache.Cc;
import io.vertx.core.http.HttpServer;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.zdk.running.OCache;
import io.zerows.core.web.model.atom.running.RunServer;
import org.osgi.framework.Bundle;

/**
 * 此处开始开启缓存的第二种形态，这种形态为运行态，存储的并非配置，而是运行态的实例数据，最终和高阶对应的容器对应，最终形成完成的运行管理。
 *
 * @author lang : 2024-05-03
 */
public interface StoreServer extends OCache<RunServer> {

    // 工厂部分
    Cc<String, StoreServer> CC_SKELETON = Cc.open();

    static StoreServer of() {
        return of(null);
    }

    static StoreServer of(final Bundle bundle) {
        final String cacheKey = Ut.Bnd.keyCache(bundle, StoreServerAmbiguity.class);
        return CC_SKELETON.pick(() -> new StoreServerAmbiguity(bundle), cacheKey);
    }

    /*
     * 由于内置对象是一个 Server，Vertx 中的 Handler，所以此处会创建多个 HttpServer 引用，只是这些 Handler 共享了一套
     * HttpServerOptions 的配置信息
     */
    HttpServer server(String serverKey);

    HttpServer server();

    /**
     * 扩展的移除方法，直接通过 key 值移除相关数据
     *
     * @param name key 值
     *
     * @return 移除后的 StoreServer 实例
     */
    StoreServer remove(String name);
}
