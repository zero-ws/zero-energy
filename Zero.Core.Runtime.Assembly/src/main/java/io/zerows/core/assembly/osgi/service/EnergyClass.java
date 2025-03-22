package io.zerows.core.assembly.osgi.service;

import io.vertx.up.util.Ut;
import io.zerows.core.metadata.uca.logging.OLog;
import org.osgi.framework.Bundle;

/**
 * @author lang : 2024-05-01
 */
public interface EnergyClass {

    void install(Bundle bundle);

    void uninstall(Bundle bundle);

    default OLog logger() {
        return Ut.Log.energy(this.getClass());
    }
}
