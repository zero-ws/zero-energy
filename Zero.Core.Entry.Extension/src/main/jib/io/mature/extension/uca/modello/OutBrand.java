package io.mature.extension.uca.modello;

import io.horizon.atom.program.Kv;
import io.modello.specification.HRecord;
import io.modello.specification.uca.OComponent;
import io.vertx.core.json.JsonObject;
import io.zerows.extension.runtime.ambient.osgi.spi.component.ExAttributeComponent;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class OutBrand extends ExAttributeComponent implements OComponent {
    @Override
    public Object after(final Kv<String, Object> kv, final HRecord record, final JsonObject combineData) {
        return this.translateTo(kv.value(), combineData);
    }
}
