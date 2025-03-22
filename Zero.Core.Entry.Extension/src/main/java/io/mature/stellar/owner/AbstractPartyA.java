package io.mature.stellar.owner;

import io.horizon.eon.em.Environment;
import io.horizon.exception.boot.AmbientConnectException;
import io.horizon.uca.boot.KPivot;
import io.macrocosm.specification.app.HAmbient;
import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.mature.stellar.ArgoStore;
import io.mature.stellar.vendor.OkB;
import io.modello.atom.app.KDS;
import io.modello.atom.app.KGlobal;
import io.modello.atom.app.KIntegration;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.feature.database.atom.Database;
import io.zerows.extension.runtime.skeleton.refine.Ke;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.zerows.extension.runtime.skeleton.refine.Ke.LOG;

/**
 * @author lang : 2023-06-13
 */
public abstract class AbstractPartyA implements OkA {

    private final Environment environment;

    private final KGlobal global;
    private final HArk ark;

    private final ConcurrentMap<String, OkB> vendors = new ConcurrentHashMap<>();

    protected AbstractPartyA(final Environment environment) {
        this.environment = environment;
        // 直接从存储中提取
        final JsonObject globalJ = ArgoStore.stellar();
        final KGlobal globalRef = Ut.deserialize(globalJ, KGlobal.class);
        this.global = globalRef;
        LOG.Ok.info(this.getClass(), "Global environment has been initialized!! = {0}", globalRef);
        // name = OkB
        globalRef.vendors().forEach(name -> {
            LOG.Ok.info(this.getClass(), "Vendor {0} has been created!", name);
            final KIntegration integration = this.global.integration(name);
            this.vendors.put(name, OkB.of(this, integration));
        });
        {
            // 检查环境是否启动完成
            final HAmbient ambient = KPivot.running();
            Fn.outBoot(Objects.isNull(ambient), AmbientConnectException.class, this.getClass());
            // 启动完成则可以直接提取应用信息
            final String appId = globalRef.appId();
            final String sigma = globalRef.sigma();
            if (Ut.isNil(appId)) {
                this.ark = Ke.ark(sigma);
            } else {
                this.ark = Ke.ark(appId);
            }
            Fn.outBoot(Objects.isNull(this.ark), AmbientConnectException.class, this.getClass());
            final HApp app = this.ark.app();
            LOG.Ok.info(this.getClass(), "HAmbient Environment has been initialized! = {0}", app.name());
        }
    }

    // --------------- 接口中的特殊API ---------------
    @Override
    public KGlobal partyA() {
        return this.global;
    }

    @Override
    public OkB partyB(final String name) {
        return this.vendors.get(name);
    }

    @Override
    public HArk configArk() {
        return this.ark;
    }

    @Override
    public Database configDatabase() {
        final KDS<Database> kds = this.ark.database();
        return kds.dynamic();
    }

    @Override
    public Environment environment() {
        return this.environment;
    }
}
