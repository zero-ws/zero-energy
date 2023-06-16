package io.vertx.boot.supply;

import com.google.inject.Injector;
import io.horizon.eon.em.web.ServerType;
import io.horizon.fn.Actuator;
import io.horizon.uca.cache.Cc;
import io.macrocosm.specification.config.HConfig;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.*;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.commune.secure.Aegis;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * 容器统一归口，处理容器层的统一调用处理，用来控制容器底层的所有基础信息
 *
 * @author lang : 2023-06-11
 */
public class Electy {
    // ---------------------- 基础元数据类

    /**
     * 读取整个环境中的所有类信息，这些类可以作为 {@link io.vertx.up.boot.anima.Factor} 中的
     * 被扫描的基础 Agent 元数据处理，并提供过滤功能
     *
     * @param category      要读取的服务器类型
     * @param defaultAgents 默认可用的 Agents 数组
     * @param internals     内部已经存在的 Agents 和哈希
     *
     * @return 返回最终的 Agents 可用类型
     */
    public static ConcurrentMap<ServerType, Class<?>> clazzAgent(
        final ServerType category,
        final Class<?>[] defaultAgents,
        final ConcurrentMap<ServerType, Class<?>> internals
    ) {
        return ZeroAgent.agentCommon(category, defaultAgents, internals);
    }

    public static ConcurrentMap<ServerType, List<Class<?>>> clazzAgent() {
        return ZeroAnno.AGENTS;
    }

    public static Set<Class<?>> clazzQueue() {
        return ZeroAnno.QUEUES;
    }

    /**
     * 提取带有 {@link io.vertx.up.annotations.EndPoint} 注解的类元数据
     *
     * @return {@link Set} 集合
     */
    public static Set<Class<?>> clazzEndPoint() {
        return ZeroAnno.ENDPOINTS;
    }

    /**
     * 提取带有 {@link io.vertx.up.annotations.Queue} 注解的类元数据
     *
     * @return {@link Set} 集合
     */
    public static Set<Class<?>> clazzWorker() {
        return ZeroAnno.WORKERS;
    }

    /**
     * 提取带有 {@link Infusion} 注解的类元数据
     *
     * @return {@link Set} 集合
     */
    public static Set<Class<?>> clazzInfix() {
        return ZeroAnno.TPS;
    }

    // ---------------------- 方法集合
    public static ConcurrentMap<String, Method> methodIpc() {
        return ZeroAnno.IPCS;
    }

    /**
     * 提取覆盖原始 Worker 专用的带 {@link io.aeon.annotations.QaS} 注解的方法
     *
     * @param address 传入的地址
     *
     * @return {@link Method} 实例
     */
    public static Method getQaS(final String address) {
        return ZeroAnno.QAS.getOrDefault(address, null);
    }

    // ---------------------- 组件类型

    /**
     * 读取环境中的依赖注入专用组件
     *
     * @return {@link Injector} 实例
     */
    public static Injector ucaDI() {
        return ZeroAnno.DI;
    }

    /**
     * 读取环境中的接口定义构造类 {@link Event}
     *
     * @return {@link Set}
     */
    public static Set<Event> ucaEvent() {
        return ZeroAnno.EVENTS;
    }

    /**
     * 读取 JSR 340 专用组件
     *
     * @return {@link Set}
     */
    public static ConcurrentMap<String, Set<Event>> ucaFilter() {
        return ZeroAnno.FILTERS;
    }

    /**
     * 读取环境中所有任务定义
     *
     * @return {@link Set}
     */
    public static Set<Mission> ucaJob() {
        return ZeroAnno.JOBS;
    }

    /**
     * Worker 专用的组件定义，关联 {@link io.vertx.up.annotations.Address}
     *
     * @return {@link Set}
     */
    public static Set<Receipt> ucaReceipt() {
        return ZeroAnno.RECEIPTS;
    }

    /**
     * WebSocket 专用配置
     *
     * @return {@link Set}
     */
    public static Set<Remind> ucaWebSocket() {
        return ZeroAnno.SOCKS;
    }

    /**
     * 安全墙组件专用
     *
     * @return {@link Set}
     */
    public static Cc<String, Set<Aegis>> ucaWall() {
        return ZeroAnno.getWalls();
    }

    // ---------------------- Uri处理

    /**
     * 检查当前的URI和模式是否匹配，对路径参数而言会处理反向检查以及匹配当前请求实际路径和定义路径是一致的
     * <pre><code>
     *     如：/api/user/:key
     *        /api/user/123
     *     上述路径应该是匹配的
     * </code></pre>
     *
     * @param requestUri 请求路径
     * @param method     Http方法
     *
     * @return 是否匹配
     */
    public static boolean uriMatch(final String requestUri, final HttpMethod method) {
        return ZeroUri.isMatch(method, requestUri);
    }

    /**
     * 将路径参数的路径从请求参数执行还原
     *
     * @param uri    请求路径
     * @param method Http方法
     *
     * @return 还原后的路径
     */
    public static String uriRecovery(final String uri, final HttpMethod method) {
        return ZeroUri.recovery(uri, method);
    }

    public static void uriResolve(final String uri, final HttpMethod method) {
        ZeroUri.resolve(uri, method);
    }

    // ---------------------- Option提取

    /**
     * 读取当前的VertxOptions所有实例配置 {@link VertxOptions}
     *
     * @return {@link ConcurrentMap}
     */
    public static ConcurrentMap<String, VertxOptions> optionVertx() {
        return ZeroOption.getVertxOptions();
    }

    /**
     * HttpServer服务器配置 {@link HttpServerOptions}
     *
     * @return {@link ConcurrentMap}
     */
    public static ConcurrentMap<Integer, HttpServerOptions> optionHttp() {
        return ZeroOption.getServerOptions();
    }

    public static ConcurrentMap<Integer, HttpServerOptions> optionGateway() {
        return ZeroOption.getGatewayOptions();
    }

    public static ConcurrentMap<Integer, HttpServerOptions> optionRx() {
        return ZeroOption.getRxOptions();
    }

    /**
     * WebSocket 服务配置 {@link SockOptions}
     *
     * @return {@link ConcurrentMap}
     */
    public static ConcurrentMap<Integer, SockOptions> optionSock() {
        return ZeroOption.getSockOptions();
    }

    /**
     * 读取当前的ClusterOptions所有实例配置 {@link ClusterOptions}
     *
     * @return {@link ConcurrentMap}
     */
    public static ClusterOptions optionCluster() {
        return ZeroOption.getClusterOption();
    }

    public static ConcurrentMap<Integer, RpcOptions> optionRpc() {
        return ZeroOption.getRpcOptions();
    }

    /**
     * 返回和EventBus相关的通讯专用配置
     *
     * @return {@link DeliveryOptions}
     */
    public static DeliveryOptions optionDelivery() {
        return ZeroOption.getDeliveryOption();
    }

    public static ConcurrentMap<Integer, String> serverName() {
        return ZeroOption.getServerNames();
    }

    public static ConcurrentMap<Integer, AtomicInteger> serverLog() {
        return ZeroOption.ATOMIC_LOG;
    }

    // ---------------------- 注册专用
    public static void initialize() {
        ZeroAnno.configure();
    }

    public static Future<Set<HArk>> registryStart(final Vertx vertx, final HConfig config) {
        return ZeroEnroll.registryStart(vertx, config);
    }

    public static Future<Boolean> registryAmbient(final Vertx vertx, final HConfig config, final Set<HArk> arkSet) {
        return ZeroEnroll.registryAmbient(vertx, config, arkSet);
    }

    public static Future<Boolean> registryArk(final Vertx vertx, final HConfig config, final Set<HArk> arkSet) {
        return ZeroEnroll.registryArk(vertx, config, arkSet);
    }

    public static Handler<AsyncResult<Boolean>> registryEnd(final Actuator actuator) {
        return ZeroEnroll.registryEnd(actuator);
    }

    public static BiConsumer<Vertx, HConfig> whenInstruction(final BiConsumer<Vertx, HConfig> endFn) {
        return ZeroEntry.whenInstruction(endFn);
    }

    public static BiConsumer<Vertx, HConfig> whenContainer(final BiConsumer<Vertx, HConfig> endFn) {
        return ZeroEntry.whenContainer(endFn);
    }
}
