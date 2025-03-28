package io.zerows.core.web.io.uca.response.wrap;

import io.horizon.uca.log.Annal;
import io.macrocosm.specification.config.HSetting;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.store.OZeroStore;
import io.zerows.core.web.model.commune.Envelop;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractWings implements Wings {

    protected boolean isFreedom() {
        final HSetting setting = OZeroStore.setting();
        final JsonObject launcher = setting.launcher().options();
        return launcher.getBoolean(YmlCore.FREEDOM, Boolean.FALSE);
    }

    protected String toFreedom(final Envelop envelop) {
        final JsonObject input = envelop.outJson();
        if (Ut.isNil(input)) {
            return null;
        } else {
            if (input.containsKey("data")) {
                final Object value = input.getValue("data");
                return Objects.isNull(value) ? null : value.toString();
            } else {
                return input.encode();
            }
        }
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
