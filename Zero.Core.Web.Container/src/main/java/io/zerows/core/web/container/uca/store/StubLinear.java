package io.zerows.core.web.container.uca.store;

import io.horizon.eon.VString;
import io.horizon.exception.web._501NotSupportException;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.eon.KMeta;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.store.OCacheClass;
import io.zerows.core.metadata.uca.logging.OLog;
import io.zerows.core.web.container.store.under.StoreVertx;
import io.zerows.core.web.container.verticle.ZeroHttpAgent;
import io.zerows.core.web.container.verticle.ZeroHttpWorker;
import io.zerows.core.web.model.atom.running.RunVertx;
import org.osgi.framework.Bundle;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Verticle 实例管理器
 * 键值计算和其他不同，此处构造 StubLinear 必须包含 Verticle 的类名，如
 * <pre><code>
 *     1. {@link ZeroHttpAgent}
 *     2. {@link ZeroHttpWorker}
 *     3. 扩展模式 ZeroHttpRegistry 或其他
 * </code></pre>
 *
 * @author lang : 2024-05-03
 */
public interface StubLinear {

    Cc<String, StubLinear> CC_SKELETON = Cc.open();

    ConcurrentMap<KMeta.Typed, Function<Bundle, StubLinear>> RUNNER = new ConcurrentHashMap<>() {
        {
            this.put(KMeta.Typed.AGENT, LinearAgent::new);
            this.put(KMeta.Typed.WORKER, LinearWorker::new);
            this.put(KMeta.Typed.IPC, LinearRpc::new);
            this.put(KMeta.Typed.CODEX, LinearCodex::new);
            this.put(KMeta.Typed.INFUSION, LinearInfusion::new);
        }
    };

    static StubLinear of(final KMeta.Typed type) {
        return of(null, type);
    }

    static StubLinear of(final Bundle bundle, final KMeta.Typed type) {
        final String cacheKey = Ut.Bnd.keyCache(bundle, StubLinear.class);
        final String cacheLinear = type.name() + VString.SLASH + cacheKey;
        return StubLinear.CC_SKELETON.pick(() -> {
            Ut.Log.vertx(StubLinear.class).info("StubLinear will be initialized by type/key = {}/{}", type, cacheKey);
            final Function<Bundle, StubLinear> constructorFn = RUNNER.get(type);
            Objects.requireNonNull(constructorFn);
            return constructorFn.apply(bundle);
        }, cacheLinear);
    }

    // --------------------- 启动执行 ---------------------
    static void standalone(final Vertx vertx, final KMeta.Typed type) {
        final RunVertx runVertx = StoreVertx.of(null).valueGet(vertx.hashCode());
        final Set<Class<?>> scanClass = OCacheClass.entireValue(type);
        final StubLinear linear = StubLinear.of(type);
        linear.initialize(scanClass, runVertx);
    }

    // --------------------- 行为专用 ---------------------

    /**
     * 此处 initialize 方法暂时不可以拿掉，主要原因在于 {@link Infusion} 中的 Infix 架构必须依赖一个 initialize 方法来做
     * 组件初始化，这个组件初始化不可以出错，所以这种情况下，必须让 Infusion 可用
     *
     * @param classSet 可用注解类
     * @param runVertx 运行实例
     */
    default void initialize(final Set<Class<?>> classSet, final RunVertx runVertx) {
        classSet.forEach(clazz -> this.runDeploy(clazz, runVertx));
    }


    default void runDeploy(final Class<?> clazz, final RunVertx runVertx) {
        throw Ut.Bnd.failWeb(_501NotSupportException.class, this.getClass());
    }

    default void runUndeploy(final Class<?> clazz, final RunVertx runVertx) {
        throw Ut.Bnd.failWeb(_501NotSupportException.class, this.getClass());
    }

    default OLog logger() {
        return Ut.Log.vertx(this.getClass());
    }
}
