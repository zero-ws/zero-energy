package io.vertx.up.util;

import io.vertx.config.ConfigStoreOptions;

/**
 * @author lang : 2023-06-19
 */
class _Load extends _Jackson {
    /*
     * ConfigStoreOptions reading
     * 1) loadJson
     * 2) loadYaml
     * 3) loadProp
     */
    public static ConfigStoreOptions loadJson(final String filename) {
        return Store.getJson(filename);
    }

    public static ConfigStoreOptions loadYaml(final String filename) {
        return Store.getYaml(filename);
    }

    public static ConfigStoreOptions loadProp(final String filename) {
        return Store.getProp(filename);
    }
}
