package io.zerows.core.feature.web.mbse.atom.specification;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;
import io.zerows.jackson.databind.ClassDeserializer;
import io.zerows.jackson.databind.ClassSerializer;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KQr implements Serializable {
    private JsonObject condition = new JsonObject();

    private JsonObject mapping = new JsonObject();

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> classDao;

    private String identifier;

    public KQr identifier(final String identifier) {
        this.identifier = identifier;
        return this;
    }

    public String identifier() {
        return this.identifier;
    }

    public JsonObject getCondition() {
        return Ut.isNil(this.condition) ? new JsonObject() : this.condition.copy();
    }

    public void setCondition(final JsonObject condition) {
        this.condition = condition;
    }

    public JsonObject getMapping() {
        return this.mapping;
    }

    public void setMapping(final JsonObject mapping) {
        this.mapping = mapping;
    }

    public Class<?> getClassDao() {
        return this.classDao;
    }

    public void setClassDao(final Class<?> classDao) {
        this.classDao = classDao;
    }

    public boolean valid() {
        return Objects.nonNull(this.classDao);
    }
}
