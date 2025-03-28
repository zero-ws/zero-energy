package io.zerows.core.web.container.uca.store;

import io.horizon.eon.VPath;
import io.horizon.eon.VString;
import io.horizon.exception.internal.EmptyIoException;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.cache.CStore;
import io.zerows.core.web.model.atom.running.RunVertx;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2024-05-03
 */
class LinearCodexA implements StubLinear {

    @Override
    public void runUndeploy(final Class<?> clazz, final RunVertx runVertx) {
        final List<String> rules = Ut.ioFiles("codex", VPath.SUFFIX.YML);
        rules.forEach(rule -> {
            try {
                // Codex 文件定义了相关规则
                final ConcurrentMap<String, JsonObject> store = CStore.CC_CODEX.store();
                // 移除
                store.remove(rule.substring(0, rule.lastIndexOf(VString.DOT)));
            } catch (final EmptyIoException ex) {
                this.logger().fatal(ex);
            }
        });
    }

    @Override
    public void runDeploy(final Class<?> clazz, final RunVertx runVertx) {
        final List<String> rules = Ut.ioFiles("codex", VPath.SUFFIX.YML);
        rules.forEach(rule -> {
            try {
                final String ruleFile = "codex/" + rule;
                final JsonObject ruleData = Ut.ioYaml(ruleFile);


                // Codex 文件定义了相关规则
                final ConcurrentMap<String, JsonObject> store = CStore.CC_CODEX.store();
                // 追加
                store.put(rule.substring(0, rule.lastIndexOf(VString.DOT)), ruleData);
            } catch (final EmptyIoException ex) {
                this.logger().fatal(ex);
            }
        });
    }
}
