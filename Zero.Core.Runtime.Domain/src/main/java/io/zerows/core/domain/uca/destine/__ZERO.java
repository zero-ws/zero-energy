package io.zerows.core.domain.uca.destine;

import io.horizon.annotations.Memory;
import io.horizon.uca.cache.Cc;


@SuppressWarnings("all")
interface POOL {
    @Memory(Hymn.class)
    Cc<String, Hymn> CCT_HYMN = Cc.openThread();

    @Memory(Conflate.class)
    Cc<String, Conflate> CCT_CONFLATE = Cc.openThread();
}