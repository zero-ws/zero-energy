package io.zerows.core.metadata.osgi.command;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.store.OCacheFailure;
import io.zerows.core.metadata.zdk.running.OCommand;
import org.osgi.framework.Bundle;

/**
 * @author lang : 2024-04-22
 */
public class CommandFailureBundle implements OCommand {

    @Override
    public void execute(final Bundle caller) {
        // 提取异常表
        final OCacheFailure cache = OCacheFailure.of(caller);
        final JsonObject stored = Ut.valueJObject(cache.value(), KName.ERROR);
        // 拉取异常报表
        System.out.println("There are " + stored.size() + " errors stored in environment.");
    }
}
