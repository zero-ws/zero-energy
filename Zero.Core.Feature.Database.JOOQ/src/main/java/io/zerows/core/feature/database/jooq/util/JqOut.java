package io.zerows.core.feature.database.jooq.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.atom.mapping.Mojo;
import org.jooq.Field;
import org.jooq.Record;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/*
 * Jooq Result for ( DataRegion Needed )
 *
 * Calculate `projection` columns here
 *
 * 1. When the `projection` is null or empty, the system should get all the data from database.
 * 2. When the `projection` is not empty, projection columns are the results that contains all fields.
 */
@SuppressWarnings("all")
public class JqOut {

    public static <T> List<T> toResult(final List<T> list, final JsonArray projectionArray, final JqAnalyzer analyzer) {
        /*
         * convert projection to field
         */
        final Mojo mojo = analyzer.pojo();
        final Set<String> projections = Ut.toSet(projectionArray); // getProjections(projectionArray, mojo);
        return toResult(list, projections, analyzer.type());
    }

    public static JsonArray toJoin(
        final List<Record> records,
        final JsonArray projection,
        final ConcurrentMap<String, Set<String>> fields,
        final Mojo mojo) {
        final JsonArray joinResult = new JsonArray();
        records.forEach(record -> {
            final int size = record.size();
            final JsonObject data = new JsonObject();
            for (int idx = 0; idx < size; idx++) {
                final Field field = record.field(idx);
                final Object value = record.get(field);
                if (Objects.nonNull(value)) {
                    /*
                     * Un translated
                     */
                    final String key = field.getQualifiedName().toString().toUpperCase();
                    /*
                     * field, alias in all
                     */
                    Set<String> fieldSet = fields.get(key);
                    if (Objects.nonNull(fieldSet)) {
                        fieldSet.forEach(resultField -> putField(data, mojo, resultField, value));
                    }
                }
            }
            joinResult.add(data);
        });
        final Set<String> projections = getProjections(projection, mojo);
        return toResult(joinResult, projections);
    }

    private static void putField(final JsonObject data, final Mojo mojo,
                                 final String field, final Object value) {
        /*
         * 「主表优先策略」
         * 非延迟加载，以优先发现的数据为准，一般优先数据为主表数据，LEFT JOIN引起，忽略掉其余数据
         * 简单说就是 T1 作为核心主表数据抽取关键性字段相关信息，而忽略掉 T2 的相关信息
         * 通常情况如下：
         * - T1 包含了 key, sigma 等
         * - T2 通常包含了 key, sigma 等
         * 计算结果
         * - 如果两个属性值相同，则不需要执行第二次计算
         * - 如果两个属性值不同，则以 T1 的数据为主
         */
        final BiConsumer<String, Object> executor = (resultField, valueData) -> {
            /*
             * 旧代码的判断使用的是 field，会导致 POJO 出现时有问题
             * 新版本直接使用 resultField 作计算，这样计算结果更加标准
             * 基本规则是：已经有的值不再追加第二次，除非配置 alias，否则子表的属性不会被直接暴露
             */
            if (!data.containsKey(resultField)) {
                data.put(resultField, valueData);
            }
        };
        String resultField = field;
        if (Ut.isNotNil(resultField) && !data.containsKey(resultField)) {
            if (Objects.nonNull(mojo)) {
                final String hitField = mojo.getOut().get(resultField);
                if (Ut.isNotNil(hitField)) {
                    resultField = hitField;
                }
            }
            /*
             * key 不允许被 T2 覆盖
             * 以下情况：
             * 1. T1 = X_CATEGORY
             * 2. T2 = F_TERM
             */
            if (KName.KEY.equals(resultField) && data.containsKey(KName.KEY)) {
                return;
            }
            if (value instanceof java.sql.Timestamp) {
                /*
                 * 处理BUG
                 * java.lang.IllegalStateException: Illegal type in JsonObject: class java.sql.Timestamp
                 */
                final Date dateTime = (Date) value;
                executor.accept(resultField, dateTime.toInstant());
            } else if (value instanceof BigDecimal) {
                /*
                 * 处理BUG
                 * java.lang.IllegalStateException: Illegal type in JsonObject: class java.math.BigDecimal
                 */
                final BigDecimal decimal = (BigDecimal) value;
                executor.accept(resultField, decimal.doubleValue());
            } else {
                executor.accept(resultField, value);
            }
        }
    }

    private static <T> List<T> toResult(final List<T> list, final Set<String> projections, final Class<?> type) {
        if (!projections.isEmpty() && !list.isEmpty()) {
            /*
             * Get all fields here
             */
            final String[] fields = Arrays.stream(Ut.fields(type))
                .map(java.lang.reflect.Field::getName)
                .filter(Objects::nonNull)
                /*
                 * Calculated filtered fields by projections
                 */
                .filter(item -> !projections.contains(item))
                .collect(Collectors.toSet()).toArray(new String[]{});
            for (String filtered : fields) {
                list.forEach(entity -> Ut.field(entity, filtered, null));
            }
        }
        return list;
    }

    private static JsonArray toResult(final JsonArray data, final Set<String> projections) {
        if (!projections.isEmpty() && !Ut.isNil(data)) {
            Ut.itJArray(data).forEach(record -> {
                final Set<String> filters = new HashSet<>(record.fieldNames()).stream()
                    /*
                     * Calculated filtered fields by projections
                     */
                    .filter(item -> !projections.contains(item))
                    .collect(Collectors.toSet());
                filters.forEach(filtered -> record.remove(filtered));
            });
        }
        return data;
    }

    private static Set<String> getProjections(final JsonArray projection, final Mojo mojo) {
        final Set<String> filters = new HashSet<>();
        if (Objects.nonNull(mojo)) {
            /* Pojo file bind */
            projection.stream()
                .filter(Objects::nonNull)
                .map(field -> (String) field)
                .map(field -> mojo.getIn().get(field))
                .forEach(filters::add);
        }
        /* No Pojo file, No transform needed */
        filters.addAll(projection.getList());
        return filters;
    }
}
