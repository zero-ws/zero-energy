package io.zerows.core.metadata.cache;

import io.horizon.annotations.Memory;
import io.horizon.uca.cache.Cc;

/**
 * @author lang : 2024-04-17
 */
interface CStoreObject {
    /*
     * 「Zero标准」
     * CC_SPI:    内置调用HService形成的通道接口（ServiceLoader规范）
     *            HService优先级
     *            - /META-INF/services/aeon/        Aeon Enabled
     *            - /META-INF/services/             Zero Extension Enabled
     */
    @Memory(Object.class)
    Cc<Class<?>, Object> CC_SPI = Cc.open();
}
