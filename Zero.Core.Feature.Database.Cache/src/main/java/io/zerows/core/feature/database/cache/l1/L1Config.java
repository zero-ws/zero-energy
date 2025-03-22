package io.zerows.core.feature.database.cache.l1;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.zerows.jackson.databind.ClassDeserializer;
import io.zerows.jackson.databind.ClassSerializer;
import io.zerows.jackson.databind.JsonObjectDeserializer;
import io.zerows.jackson.databind.JsonObjectSerializer;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class L1Config implements Serializable {

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> component;

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private transient Class<?> worker;

    private transient String address;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject options;
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private transient JsonObject matrix;

    public Class<?> getComponent() {
        return this.component;
    }

    public void setComponent(final Class<?> component) {
        this.component = component;
    }

    public JsonObject getOptions() {
        return this.options;
    }

    public void setOptions(final JsonObject options) {
        this.options = options;
    }

    public JsonObject getMatrix() {
        return this.matrix;
    }

    public void setMatrix(final JsonObject matrix) {
        this.matrix = matrix;
    }

    public Class<?> getWorker() {
        return this.worker;
    }

    public void setWorker(final Class<?> worker) {
        this.worker = worker;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public L1Config copy() {
        final L1Config config = new L1Config();
        config.setComponent(this.component);
        if (Objects.nonNull(this.matrix)) {
            config.setMatrix(this.matrix.copy());
        }
        if (Objects.nonNull(this.options)) {
            config.setOptions(this.options.copy());
        }
        config.setWorker(this.worker);
        config.setAddress(this.address);
        return config;
    }
}
