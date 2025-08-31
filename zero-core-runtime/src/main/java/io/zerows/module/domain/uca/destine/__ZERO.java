package io.zerows.module.domain.uca.destine;

import io.zerows.ams.annotations.Memory;
import io.zerows.core.uca.cache.Cc;


@SuppressWarnings("all")
interface POOL {
    @Memory(Hymn.class)
    Cc<String, Hymn> CCT_HYMN = Cc.openThread();

    @Memory(Conflate.class)
    Cc<String, Conflate> CCT_CONFLATE = Cc.openThread();
}