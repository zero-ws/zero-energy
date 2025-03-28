package io.zerows.core.feature.database.atom;

import io.horizon.annotations.Legacy;
import io.horizon.eon.em.EmDS;
import io.modello.atom.app.KDatabase;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.store.OZeroStore;
import io.zerows.core.metadata.uca.environment.MatureOn;
import io.zerows.core.metadata.uca.logging.OLog;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

/*
 * Database linker for JDBC
 * {
 *      "hostname": "localhost",
 *      "instance": "DB_XXX",
 *      "username": "lang",
 *      "password": "xxxx",
 *      "port": 3306,
 *      "category": "MYSQL5",
 *      "driverClassName": "Fix driver issue here",
 *      "jdbcUrl": "jdbc:mysql://ox.engine.cn:3306/DB_XXX?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&useSSL=false&allowPublicKeyRetrieval=true",
 * }
 * I_SERVICE -> configDatabase
 *
 * YAML 格式说明
 * jooq:
 *    provider:         // PRIMARY
 *    orbit:            // HISTORY
 * workflow:
 *    database:         // WORKFLOW
 */
@Legacy("旧版由于使用反射无法直接重命名，"
    + "所以保留了Zero内部的数据库定义，并且该定义位于 zero-argument 核心位置，"
    + "不可以直接被取消，但该类可从 KDatabase 高阶对象中继承"
)
public class Database extends KDatabase {
    private static final OLog LOGGER = Ut.Log.database(Database.class);
    private static Database DATABASE;

    /* Database Connection Testing */
    public static boolean test(final Database database) {
        try {
            DriverManager.getConnection(database.getJdbcUrl(), database.getUsername(), database.getSmartPassword());
            return true;
        } catch (final SQLException ex) {
            // Debug for database connection
            ex.printStackTrace();
            Database.LOGGER.fatal(ex);
            return false;
        }
    }

    /**
     * <pre><code>
     * jooq:
     *     provider:
     * </code></pre>
     *
     * @return {@link Database}
     */
    public static Database getCurrent() {
        if (Objects.isNull(DATABASE)) {
            final JsonObject configJ = OZeroStore.option(YmlCore.jooq.__KEY); // Database.VISITOR.read();
            final JsonObject jooq = Ut.valueJObject(configJ, YmlCore.jooq.PROVIDER);
            DATABASE = configure(MatureOn.envDatabase(jooq, EmDS.Stored.PRIMARY));
        }
        return DATABASE.copy();
    }


    /**
     * <pre><code>
     * jooq:
     *     orbit:
     * </code></pre>
     *
     * @return {@link Database}
     */
    public static Database getHistory() {
        final JsonObject configJ = OZeroStore.option(YmlCore.jooq.__KEY); // Database.VISITOR.read();
        final JsonObject jooq = Ut.valueJObject(configJ, YmlCore.jooq.ORBIT);
        return configure(MatureOn.envDatabase(jooq, EmDS.Stored.HISTORY));
    }

    /**
     * <pre><code>
     * jooq:
     *     workflow:
     * </code></pre>
     *
     * @return {@link Database}
     */
    public static Database getCamunda() {
        final JsonObject configJ = OZeroStore.option(YmlCore.jooq.__KEY);
        final JsonObject jooq = Ut.valueJObject(configJ, YmlCore.jooq.WORKFLOW);
        return configure(MatureOn.envDatabase(jooq, EmDS.Stored.WORKFLOW));
    }

    public static Database configure(final JsonObject databaseJ) {
        final JsonObject jooq = Ut.valueJObject(databaseJ);
        final Database database = new Database();
        database.fromJson(jooq);
        return database;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Database copy() {
        final JsonObject json = this.toJson().copy();
        final Database database = new Database();
        database.fromJson(json);
        return database;
    }
}
