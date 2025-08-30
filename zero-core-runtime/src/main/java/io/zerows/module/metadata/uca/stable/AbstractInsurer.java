package io.zerows.module.metadata.uca.stable;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.zerows.agreed.constant.VValue;
import io.zerows.core.exception.ProgramException;
import io.zerows.core.fn.Fn;
import io.zerows.core.util.Ut;
import io.zerows.module.metadata.exception.DaemonJElementException;
import io.zerows.module.metadata.uca.logging.OLog;

/**
 * @author lang
 */
public abstract class AbstractInsurer implements Insurer {

    @Override
    public void flumen(final JsonArray array,
                       final JsonObject elementRule)
        throws ProgramException {
        Fn.bugAt(() -> {
            // 1. Verify the element must be json object for each
            final int size = array.size();
            for (int idx = VValue.IDX; idx < size; idx++) {
                final Object value = array.getValue(idx);
                // 2. Call check method to confirm JsonObject
                Fn.outBug(!Ut.isJObject(value), this.logger(),
                    DaemonJElementException.class,
                    this.getClass(), idx, value);

                final JsonObject item = (JsonObject) value;
                // 3. Apply the rule to each object.
                this.flumen(item, elementRule);
            }
        }, array, elementRule);
    }

    protected OLog logger() {
        return Ut.Log.uca(this.getClass());
    }
}
