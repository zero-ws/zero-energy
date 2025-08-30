package io.zerows.module.configuration.uca.transformer;

import io.vertx.core.json.JsonObject;
import io.zerows.core.fn.Fn;
import io.zerows.module.configuration.atom.option.RpcOptions;
import io.zerows.module.configuration.zdk.Transformer;

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
