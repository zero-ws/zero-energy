package io.zerows.core.web.model.store;

import io.horizon.uca.cache.Cc;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.zdk.running.OCache;
import io.zerows.core.web.model.atom.action.OJointAction;
import io.zerows.core.web.model.atom.action.OJointMap;
import org.osgi.framework.Bundle;

/**
 * @author lang : 2024-04-21
 */
public interface OCacheJoint extends OCache<OJointAction> {
    Cc<String, OCacheJoint> CC_SKELETON = Cc.open();

    static OCacheJoint of(final Bundle bundle) {
        final String cacheKey = Ut.Bnd.keyCache(bundle, OCacheJointAmbiguity.class);
        return CC_SKELETON.pick(() -> new OCacheJointAmbiguity(bundle), cacheKey);
    }

    static OCacheJoint of() {
        return of(null);
    }

    static OJointMap entireJoint() {
        final OJointMap jointMap = new OJointMap();
        CC_SKELETON.store().values().forEach(each -> {
            final OJointMap eachMap = each.joint();
            jointMap.add(eachMap);
        });
        return jointMap;
    }

    OJointMap joint();
}
