package io.zerows.core.configuration.uca.setup;

import io.horizon.eon.em.web.ServerType;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import io.zerows.core.configuration.atom.NodeVertx;
import io.zerows.core.configuration.atom.option.RpcOptions;
import io.zerows.core.configuration.uca.transformer.RpcTransformer;
import io.zerows.core.configuration.zdk.Processor;
import io.zerows.core.configuration.zdk.Transformer;

/**
 * @author lang : 2024-04-20
 */
class ProcessorOfIpc implements Processor<NodeVertx, JsonArray> {
    private final transient Transformer<RpcOptions> rpcTransformer;

    ProcessorOfIpc() {
        this.rpcTransformer = new RpcTransformer();
    }

    @Override
    public void makeup(final NodeVertx target, final JsonArray setting) {
        this.logger().debug(INFO.V_BEFORE, KName.SERVER, ServerType.IPC, setting);

        Ut.itJArray(setting, (item, index) -> {
            final JsonObject configureJ = Ut.valueJObject(item, KName.CONFIG);

            final RpcOptions options = this.rpcTransformer.transform(configureJ);
            final String serverName = Ut.valueString(item, KName.NAME);
            target.optionServer(serverName, options);
        });
        if (Ut.isNotNil(setting)) {
            this.logger().info(INFO.V_AFTER, KName.SERVER, ServerType.IPC, setting);
        }
    }
}
