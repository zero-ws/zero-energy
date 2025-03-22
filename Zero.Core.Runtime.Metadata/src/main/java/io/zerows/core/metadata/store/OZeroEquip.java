package io.zerows.core.metadata.store;

import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.eon.spec.VBoot;
import io.horizon.spi.boot.HEquip;
import io.macrocosm.atom.boot.KSetting;
import io.macrocosm.specification.config.HConfig;
import io.macrocosm.specification.config.HSetting;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KPlugin;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.zdk.AbstractAmbiguity;
import org.osgi.framework.Bundle;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lang : 2023-05-30
 */
public class OZeroEquip extends AbstractAmbiguity implements HEquip {

    private OZeroEquip() {
        super(null);
    }

    private OZeroEquip(final Bundle bundle) {
        super(bundle);
    }

    public static OZeroEquip of(final Bundle bundle) {
        return Objects.isNull(bundle) ? new OZeroEquip() : new OZeroEquip(bundle);
    }

    /**
     * @return {@link io.macrocosm.atom.boot.KSetting}
     */
    @Override
    public HSetting initialize() {
        /*
         * 基础装配 vertx.yml 读取
         * - bundle/vertx.yml
         * - vertx.yml
         *
         * zero:
         *    lime: key1,key2,key3
         * scanned:
         *
         */
        final JsonObject configuration = Ut.Bnd.ioCombineJ(this.nameZero(null), this.caller()); // ZeroIo.read(null, true);
        final HSetting setting = KSetting.of();
        final JsonObject configZero = Ut.valueJObject(configuration, KName.Internal.ZERO);
        setting.container(HConfig.of(configZero));
        final String extension = Ut.valueString(configZero, YmlCore.LIME);

        final JsonObject configBoot = Ut.valueJObject(configuration, VBoot.__KEY);
        setting.launcher(HConfig.of(configBoot));

        /*
         * zero:
         *    lime: key1,key2,key3
         */
        final JsonObject configExtension = new JsonObject();
        final Set<String> keys = Ut.toSet(extension, VString.COMMA);
        final Set<String> internal = Arrays.stream(KPlugin.FILE_KEY)
            .collect(Collectors.toSet());
        keys.stream().filter(field -> !internal.contains(field)).forEach(field -> {
            // lime file
            // - vertx-key1.yml
            // - vertx-key2.yml
            // - vertx-key3.yml
            final JsonObject fileData = Ut.Bnd.ioConfigureJ(this.nameZero(field), this.caller()); // ZeroIo.read(field, false);
            configExtension.mergeIn(fileData, true);
        });
        /*
         * 文件合并处理，所有 key 值
         */
        Ut.<JsonObject>itJObject(configExtension).forEach(entry -> {
            final String key = entry.getKey();
            setting.infix(key, HConfig.of(entry.getValue()));
        });
        /*
         * <Internal>
         * server = {}
         * inject = {}
         * error = {}
         * resolver = {}
         */
        Arrays.stream(KPlugin.FILE_KEY).forEach(field -> {
            final JsonObject fileData = Ut.Bnd.ioCombineJ(this.nameZero(field), this.caller()); // ZeroIo.read(field, true);
            setting.infix(field, HConfig.of(fileData));
        });
        return setting;
    }

    private String nameZero(final String key) {
        return Objects.isNull(key) ?
            "vertx" + VString.DOT + VPath.SUFFIX.YML :
            "vertx" + VString.DASH + key + VString.DOT + VPath.SUFFIX.YML;
    }
}
