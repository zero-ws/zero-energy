package io.zerows.core.cloud.cache;

import io.horizon.annotations.Memory;
import io.horizon.uca.cache.Cc;
import io.macrocosm.specification.nc.HAeon;
import io.macrocosm.specification.nc.HWrapper;
import io.vertx.core.json.JsonObject;
import io.zerows.core.cloud.atom.AeonConfig;
import io.zerows.core.metadata.cache.CStore;

/**
 * @author lang : 2023/5/2
 */
public interface CStoreCloud extends CStore {


    /*
     * 「应用配置集」
     * 用于存储 XApp + XSource 等应用程序配置集
     */
    @Memory(JsonObject.class)
    Cc<String, JsonObject> CC_META_APP = Cc.open();

    /*
     * CC_AEON:  Aeon系统启动后的核心配置缓存
     * CC_BOOT:  Aeon系统启动过后的所有使用类清单（组件接口集）
     */
    @Memory(AeonConfig.class)
    Cc<Integer, HAeon> CC_AEON = Cc.open();
    @Memory(HWrapper.class)
    Cc<Integer, HWrapper> CC_BOOT = Cc.open();
}
