package io.zerows.core.configuration.uca.transformer;

import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.zerows.core.configuration.atom.option.RpcOptions;
import io.zerows.core.configuration.zdk.Transformer;

/**
 * @author lang : 2024-04-20
 */
public class RpcTransformer implements Transformer<RpcOptions> {

    @Override
    public RpcOptions transform(final JsonObject input) {
        return Fn.runOr(null == input, this.logger(),
            RpcOptions::new,
            () -> new RpcOptions(input));
    }
}
