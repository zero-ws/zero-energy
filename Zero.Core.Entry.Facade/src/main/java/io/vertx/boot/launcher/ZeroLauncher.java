package io.vertx.boot.launcher;

import io.horizon.specification.boot.HLauncher;
import io.macrocosm.specification.boot.HOff;
import io.macrocosm.specification.boot.HOn;
import io.macrocosm.specification.config.HConfig;
import io.vertx.core.Vertx;
import io.vertx.up.util.Ut;
import io.zerows.core.configuration.atom.NodeNetwork;
import io.zerows.core.configuration.store.OCacheNode;
import io.zerows.core.web.container.osgi.service.EnergyVertx;
import io.zerows.core.web.container.osgi.service.EnergyVertxService;
import io.zerows.core.web.container.store.under.StoreVertx;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * 标准容器 Service / Application 启动器
 *
 * @author lang : 2023-05-30
 */
public class ZeroLauncher implements HLauncher<Vertx> {

    private static final EnergyVertx SERVICE = new EnergyVertxService();
    private static final ConcurrentMap<String, Vertx> STORED_DATA = new ConcurrentHashMap<>();

    @Override
    public ConcurrentMap<String, Vertx> store() {
        if (STORED_DATA.isEmpty()) {
            final StoreVertx storeVertx = StoreVertx.of();
            final Set<String> names = storeVertx.keys();
            names.forEach(name -> STORED_DATA.put(name, storeVertx.vertx(name)));
        }
        return STORED_DATA;
    }

    @Override
    public <T extends HConfig> void start(final HOn<T> on, final Consumer<Vertx> server) {
        final NodeNetwork network = OCacheNode.of().network();

        SERVICE.startAsync(null, network).onComplete(cached -> {
            if (cached.failed()) {
                Ut.Log.boot(this.getClass()).fatal(cached.cause());
                return;
            }


            // 存储引用专用
            final StoreVertx storeVertx = cached.result();
            final Set<String> names = storeVertx.keys();

            names.forEach(name -> {
                final Vertx vertx = storeVertx.vertx(name);
                if (Objects.nonNull(vertx)) {
                    server.accept(vertx);
                }
            });
        });
    }

    @Override
    public <T extends HConfig> void stop(final HOff<T> off, final Consumer<Vertx> server) {
        // 等待实现
    }
}
