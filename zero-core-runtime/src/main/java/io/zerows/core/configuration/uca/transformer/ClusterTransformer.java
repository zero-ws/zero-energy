package io.zerows.core.configuration.uca.transformer;

import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.zerows.core.configuration.atom.option.ClusterOptions;
import io.zerows.core.configuration.zdk.Transformer;

/**
 * @author lang : 2024-04-20
 */
public class ClusterTransformer implements Transformer<ClusterOptions> {
    @Override
    public ClusterOptions transform(final JsonObject config) {
        return Fn.runOr(null == config, this.logger(),
            ClusterOptions::new,
            () -> new ClusterOptions(config));
    }
}
