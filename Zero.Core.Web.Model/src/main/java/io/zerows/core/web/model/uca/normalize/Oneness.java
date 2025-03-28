package io.zerows.core.web.model.uca.normalize;

import io.horizon.uca.cache.Cc;
import io.zerows.core.metadata.atom.configuration.modeling.MDConnect;

import java.util.Set;

/**
 * 唯一标识计算符，每个线程一个此组件，替换原始的计算符，Office.Excel 中常用，针对 unique 标识规则的调整
 * <pre><code>
 *     1. 计算 Unique Key（多字段值）
 *     2. 计算 Primary Key（单字段值）
 * </code></pre>
 *
 * @author lang : 2024-05-10
 */
public interface Oneness<T> {

    @SuppressWarnings("all")
    Cc<String, Oneness> CC_ONE = Cc.openThread();

    @SuppressWarnings("unchecked")
    static Oneness<MDConnect> ofConnect() {
        return (Oneness<MDConnect>) CC_ONE.pick(OnenessConnect::new, OnenessConnect.class.getName());
    }

    String keyPrimary(T connect);

    Set<String> keyUnique(T connect);
}
