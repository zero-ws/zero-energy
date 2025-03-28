package io.zerows.core.cloud.zdk.spi;

import io.horizon.atom.datamation.KDictSource;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/*
 * Dict Processing here
 * 1) dictConfig
 *    dictComponent
 * 2) Determine `dictComponent` for fetch here.
 */
public interface Dictionary {
    /*
     * Here are default params
     * {
     *     "sigma": "The identifier of uniform",
     *     "identifier": "The bundle service identifier"
     * }
     */
    Future<ConcurrentMap<String, JsonArray>> fetchAsync(MultiMap paramMap,
                                                        List<KDictSource> sources);

    Future<JsonArray> fetchTree(String sigma, String type);

    Future<JsonArray> fetchList(String sigma, String type);
}
