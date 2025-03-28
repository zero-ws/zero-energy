package io.zerows.core.feature.database.jooq;

import io.horizon.eon.em.EmDS;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.feature.database.atom.Database;
import io.zerows.core.feature.database.cp.zdk.DataPool;
import io.zerows.core.feature.database.jooq.exception.BootJooqConfigurationException;
import io.zerows.core.metadata.uca.environment.MatureOn;
import io.zerows.core.metadata.uca.logging.OLog;
import org.jooq.Configuration;
import org.jooq.Table;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JooqPin {

    private static final OLog LOGGER = Ut.Log.database(JooqPin.class);

    public static String initTable(final Class<?> clazz) {
        final JooqDsl dsl = JooqInfix.getDao(clazz);
        final Table<?> table = Ut.field(dsl.dao(), KName.TABLE);
        return table.getName();
    }

    public static Class<?> initPojo(final Class<?> clazz) {
        final JooqDsl dsl = JooqInfix.getDao(clazz);
        return Ut.field(dsl.dao(), KName.TYPE);
    }

    static ConcurrentMap<String, Configuration> initConfiguration(final JsonObject config) {
        /*
         * config should be configured in vertx-jooq.yml
         * jooq:
         *    # Standard configuration
         *    provider:
         *    # History configuration
         *    orbit:
         */
        final ConcurrentMap<String, Configuration> configurationMap =
            new ConcurrentHashMap<>();

        Fn.outBoot(Ut.isNil(config) || !config.containsKey(YmlCore.jooq.PROVIDER),
            LOGGER, BootJooqConfigurationException.class, JooqPin.class);

        if (Ut.isNotNil(config)) {
            /*
             * provider / orbit
             * provider - 标准数据库
             * orbit - 历史数据库
             */
            config.fieldNames().stream()
                // 过滤：key值对应的配置存在，并且是合法的 Database Json 格式
                .filter(key -> Objects.nonNull(config.getValue(key)))
                .filter(key -> config.getValue(key) instanceof JsonObject)
                .forEach(key -> {
                    final JsonObject options = config.getJsonObject(key);
                    // Database Environment Connected
                    final JsonObject databaseJ;
                    if (YmlCore.jooq.ORBIT.equals(key)) {
                        databaseJ = MatureOn.envDatabase(options, EmDS.Stored.HISTORY);
                    } else {
                        databaseJ = MatureOn.envDatabase(options, EmDS.Stored.PRIMARY);
                    }
                    final DataPool pool = DataPool.create(Database.configure(databaseJ));
                    final Configuration configuration = pool.configuration();
                    configurationMap.put(key, configuration);
                    final JsonObject populated = databaseJ.copy();
                    populated.remove(KName.PASSWORD);
                    LOGGER.info("Jooq options: \n{0}", populated.encodePrettily());
                });
        }
        return configurationMap;
    }
}
