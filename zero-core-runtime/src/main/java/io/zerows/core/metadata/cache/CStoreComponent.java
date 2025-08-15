package io.zerows.core.metadata.cache;

import io.horizon.annotations.Memory;
import io.horizon.specification.typed.TCombiner;
import io.horizon.specification.typed.TEvent;
import io.horizon.uca.cache.Cc;

/**
 * @author lang : 2024-04-17
 */
interface CStoreComponent {


    /*
     * 「界面级别处理」
     */
    @SuppressWarnings("all")
    @Memory(TCombiner.class)
    Cc<String, TCombiner> CC_COMBINER = Cc.openThread();


    /*
     * 「线程级」
     * CCT_EVENT: Aeon中的所有Event集合
     */
    @SuppressWarnings("all")
    @Memory(TEvent.class)
    Cc<String, TEvent> CCT_EVENT = Cc.openThread();
}
