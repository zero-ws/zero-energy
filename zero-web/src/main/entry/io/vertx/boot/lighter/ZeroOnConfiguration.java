package io.vertx.boot.lighter;

import io.macrocosm.specification.app.HPre;
import io.modello.atom.app.KConfig;

/**
 * 标准容器中的 {@link HPre} 处理器
 * 针对框架中的插件执行双重处理：
 * <pre><code>
 *     Vertx容器已执行完实例化
 *     - 容器启动之前的内置插件部分
 *     Vertx Extension / Ambient 容器已执行完实例化
 *     - 容器扩展部分加载完成之后的插件部分
 * </code></pre>
 *
 * @author lang : 2023-05-31
 */
public class ZeroOnConfiguration extends KConfig {
}
