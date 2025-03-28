package io.zerows.core.metadata.zdk.running;

import io.horizon.eon.VString;
import io.horizon.exception.web._501NotSupportException;
import io.horizon.uca.cache.Cc;
import io.macrocosm.specification.config.HSetting;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.store.OZeroStore;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import java.util.Objects;

/**
 * 配置仓库
 * <pre><code>
 *     1. 单机版
 *        X.of().configure() 执行
 *     2. OSGI 版
 *        X.of(bundle).configure() 执行
 * </code></pre>
 * 统一处理配置启动专用流程
 * <pre><code>
 *     一般内置调用 {@link OCache} 的子接口，结构如：
 *     ORepository
 *       | - OCache
 *            | - OCacheXXX
 *            | - ....
 * </code></pre>
 * 此接口只是为了统一流程，configure() 内置方法可以
 *
 * @author lang : 2024-05-01
 */
public interface ORepository {

    Cc<String, ORepository> CC_SKELETON = Cc.open();

    static <T extends ORepository> ORepository ofOr(final Class<T> clazz) {
        Objects.requireNonNull(clazz);
        final Bundle bundle = FrameworkUtil.getBundle(clazz);
        return ofOr(clazz, bundle);
    }

    static <T extends ORepository> ORepository ofOr(final Class<T> clazz, final Bundle bundle) {
        Objects.requireNonNull(clazz);
        final String cacheKey = clazz.getName() + VString.SLASH + Ut.Bnd.keyCache(bundle, clazz);
        return CC_SKELETON.pick(() -> Ut.instance(clazz, bundle), cacheKey);
    }

    // ------------------- 初始化流程
    void whenStart(HSetting setting);

    /** 单机版专用的快速方法，可直接在外层调用 */
    default void whenStart() {
        final HSetting setting = OZeroStore.setting();
        this.whenStart(setting);
    }

    default void whenUpdate(final HSetting setting) {
        throw Ut.Bnd.failWeb(_501NotSupportException.class, this.getClass());
    }

    default void whenRemove() {
        throw Ut.Bnd.failWeb(_501NotSupportException.class, this.getClass());
    }

    Bundle caller();
}
