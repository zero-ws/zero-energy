package io.zerows.core.web.container.store;

import io.horizon.eon.em.EmApp;
import io.horizon.exception.BootingException;
import io.horizon.uca.log.Annal;
import io.macrocosm.atom.boot.KBoot;
import io.macrocosm.specification.config.HBoot;
import io.macrocosm.specification.config.HSetting;
import io.macrocosm.specification.config.HStation;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Up;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.fn.Fn;
import io.zerows.core.cloud.util.Ho;
import io.zerows.core.metadata.store.OZeroStore;
import io.zerows.core.web.container.eon.em.FeatureMark;
import io.zerows.core.web.container.exception.BootUpClassArgsException;
import io.zerows.core.web.container.exception.BootUpClassInvalidException;
import io.zerows.core.web.model.util.Anno;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2023-05-30
 */
public class BootStore implements HStation {
    /**
     * 针对 Annotation 部分的创建
     */
    private static final ConcurrentMap<String, Annotation> STORE_ANNO =
        new ConcurrentHashMap<>();
    private static volatile BootStore STORE;
    private final ConcurrentMap<FeatureMark, Boolean> features =
        new ConcurrentHashMap<>();

    private final HBoot boot;


    private BootStore() {
        final HSetting setting = OZeroStore.setting();
        final JsonObject launcherJ = setting.launcher().options();
        this.boot = KBoot.of(launcherJ);
    }

    public static BootStore singleton() {
        if (Objects.isNull(STORE)) {
            synchronized (BootStore.class) {
                if (Objects.isNull(STORE)) {
                    STORE = new BootStore();
                }
            }
        }
        {
            // session
            STORE.feature(FeatureMark.SESSION, OZeroStore.is(YmlCore.inject.SESSION));
            // init
            STORE.feature(FeatureMark.INIT, OZeroStore.is(YmlCore.init.__KEY));
            // etcd
            final boolean etcd = OZeroStore.is(YmlCore.etcd.__KEY);
            STORE.feature(FeatureMark.ETCD, etcd);
            // gateway
            Fn.outBug(() -> {
                // 暂时不考虑 API 类型
                STORE.boot().app(etcd ? EmApp.Type.SERVICE : EmApp.Type.APPLICATION);
            }, Annal.get(BootStore.class));
        }
        return STORE;
    }

    public static BootStore singleton(final Class<?> bootingCls, final String... arguments) {
        // 启动检查
        ensure(bootingCls);

        return singleton().bind(bootingCls, arguments);
    }

    /**
     * 「启动规范说明」
     * 1. Zero容器要求输入的clazz必须不能为空，用于后期挂载数据专用
     * 2. 最好在启动类中使用 {@link Up}，否则会导致启动规范的警告，但是不影响启动
     */
    private static void ensure(final Class<?> clazz) {
        // Step 1
        Fn.out(Objects.isNull(clazz), BootUpClassArgsException.class, BootStore.class);
        // Step 2
        STORE_ANNO.putAll(Anno.get(clazz));
        if (!STORE_ANNO.containsKey(Up.class.getName())) {
            final BootingException warning = new BootUpClassInvalidException(BootStore.class, clazz.getName());
            Ho.LOG.Env.info(BootStore.class, warning.getMessage());
        }
    }

    // ------------------- Reference --------------------
    @Override
    public BootStore bind(final Class<?> mainClass, final String[] arguments) {
        this.boot.bind(mainClass, arguments);
        return this;
    }

    @Override
    public HBoot boot() {
        return this.boot;
    }

    // ------------------- Feature --------------------

    public BootStore feature(final FeatureMark mark, final Boolean enabled) {
        this.features.put(mark, enabled);
        return this;
    }

    public boolean isEtcd() {
        return this.features
            .getOrDefault(FeatureMark.ETCD, Boolean.FALSE);
    }

    public boolean isInit() {
        return this.features
            .getOrDefault(FeatureMark.INIT, Boolean.FALSE);
    }

    public boolean isSession() {
        return this.features
            .getOrDefault(FeatureMark.SESSION, Boolean.FALSE);
    }
}
