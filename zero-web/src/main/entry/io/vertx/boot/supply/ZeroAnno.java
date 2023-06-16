package io.vertx.boot.supply;

import com.google.inject.Injector;
import io.aeon.atom.KSwitcher;
import io.aeon.uca.web.origin.HQaSInquirer;
import io.horizon.eon.em.web.ServerType;
import io.horizon.runtime.Runner;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.macrocosm.specification.nc.HAeon;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.atom.worker.Receipt;
import io.vertx.up.atom.worker.Remind;
import io.vertx.up.boot.origin.*;
import io.vertx.up.commune.secure.Aegis;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroPack;
import io.vertx.up.util.Ut;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Transfer Class<?> set to difference mapping.
 */
class ZeroAnno {

    static final Injector DI;
    /*
     * Class Scanner
     */
    final static Set<Class<?>> ENDPOINTS = new HashSet<>();
    final static Set<Event> EVENTS = new HashSet<>();
    final static ConcurrentMap<String, Set<Event>> FILTERS = new ConcurrentHashMap<>();
    final static ConcurrentMap<String, Method> IPCS = new ConcurrentHashMap<>();
    final static Set<Mission> JOBS = new HashSet<>();
    final static ConcurrentMap<String, Method> QAS = new ConcurrentHashMap<>();
    final static ConcurrentMap<ServerType, List<Class<?>>> AGENTS = new ConcurrentHashMap<>();
    final static Set<Class<?>> QUEUES = new HashSet<>();
    final static Set<Class<?>> WORKERS = new HashSet<>();
    final static Set<Class<?>> TPS = new HashSet<>();
    /*
     * Component Scanner
     */
    final static Set<Receipt> RECEIPTS = new HashSet<>();
    final static Set<Remind> SOCKS = new HashSet<>();
    private static final Annal LOGGER = Annal.get(ZeroAnno.class);
    private final static Set<Aegis> WALLS = new TreeSet<>();
    private static final Cc<String, Set<Aegis>> CC_WALL = Cc.open();
    private static final Set<Class<?>> CLASS_SET;

    static {
        CLASS_SET = ZeroPack.getClasses();
        final Inquirer<Injector> guice = Ut.singleton(GuiceInquirer.class);
        DI = guice.scan(CLASS_SET);
        LOGGER.info("Zero Zone DI Environment.... Size= {}", String.valueOf(CLASS_SET.size()));
    }

    /*
     * Move to main thread to do init instead of static block initialization
     * Here all the class must be prepared
     */
    static void configure() {
        /*
         * Phase 1:
         * -- Scan the whole environment to extract all classes those will be analyzed.
         * -- Guice Module Start to extract all ( DI clazz )
         * -- Class<?> Seeking ( EndPoint, Queue, HQueue )
         */
        final long start = System.currentTimeMillis();
        final long end = System.currentTimeMillis();
        LOGGER.info("Zero Timer: Scanning = {0} ms",
            String.valueOf(end - start));
        Runner.run("meditate-class",
            // @EndPoint
            () -> {
                final Inquirer<Set<Class<?>>> inquirer = Ut.singleton(EndPointInquirer.class);
                ENDPOINTS.addAll(inquirer.scan(CLASS_SET));
            },
            // @Queue
            () -> {
                final Inquirer<Set<Class<?>>> inquirer = Ut.singleton(QueueInquirer.class);
                QUEUES.addAll(inquirer.scan(CLASS_SET));
            },
            // @QaS
            () -> {
                /* QAS */
                final HAeon aeon = KSwitcher.aeon();
                if (Objects.nonNull(aeon)) {
                    /* Aeon System Enabled */
                    final Inquirer<ConcurrentMap<String, Method>> inquirer = Ut.singleton(HQaSInquirer.class);
                    QAS.putAll(inquirer.scan(CLASS_SET));
                }
            },
            // TpClients Plugins
            () -> {
                final Inquirer<Set<Class<?>>> tps = Ut.singleton(PluginInquirer.class);
                TPS.addAll(tps.scan(CLASS_SET));
            },
            // Worker Class
            () -> {
                final Inquirer<Set<Class<?>>> worker = Ut.singleton(WorkerInquirer.class);
                WORKERS.addAll(worker.scan(CLASS_SET));
            }
        );


        /*
         * Phase 2:
         * Component Scan of following:
         * -- @EndPoint -> Event
         * -- @Wall
         * -- @WebFilter
         * -- @Queue/@QaS
         * -- @Ipc
         * -- Agent Component
         * -- @WebSock
         * -- @Job
         */
        Runner.run("meditate-component",
            // @EndPoint -> Event
            () -> Fn.runAt(!ENDPOINTS.isEmpty(), LOGGER, () -> {
                final Inquirer<Set<Event>> event = Ut.singleton(EventInquirer.class);
                EVENTS.addAll(event.scan(ENDPOINTS));
                /* 1.1. Put Path Uri into Set */
                EVENTS.stream().filter(Objects::nonNull)
                    /* Only Uri Pattern will be extracted to URI_PATHS */
                    .filter(item -> 0 < item.getPath().indexOf(":"))
                    .forEach(item -> ZeroUri.resolve(item.getPath(), item.getMethod()));
                ZeroUri.report();
            }),
            // @Wall -> Authenticate, Authorize
            () -> {
                final Inquirer<Set<Aegis>> walls = Ut.singleton(WallInquirer.class);
                WALLS.addAll(walls.scan(CLASS_SET));
            },
            // @WebFilter -> JSR340
            () -> {
                final Inquirer<ConcurrentMap<String, Set<Event>>> filters = Ut.singleton(FilterInquirer.class);
                FILTERS.putAll(filters.scan(CLASS_SET));
            },
            // @Queue/@QaS -> Receipt
            () -> Fn.runAt(!QUEUES.isEmpty(), LOGGER, () -> {
                final Inquirer<Set<Receipt>> receipt = Ut.singleton(ReceiptInquirer.class);
                RECEIPTS.addAll(receipt.scan(QUEUES));
            }),
            // @Ipc -> IPC Only
            () -> {
                final Inquirer<ConcurrentMap<String, Method>> ipc = Ut.singleton(IpcInquirer.class);
                IPCS.putAll(ipc.scan(CLASS_SET));
            },
            // Agent Component
            () -> {
                final Inquirer<ConcurrentMap<ServerType, List<Class<?>>>> agent = Ut.singleton(AgentInquirer.class);
                AGENTS.putAll(agent.scan(CLASS_SET));
            },
            // @WebSock
            () -> {
                final Inquirer<Set<Remind>> socks = Ut.singleton(SockInquirer.class);
                SOCKS.addAll(socks.scan(CLASS_SET));
            },
            // @Job
            () -> {
                final Inquirer<Set<Mission>> jobs = Ut.singleton(JobInquirer.class);
                JOBS.addAll(jobs.scan(CLASS_SET));
            }
        );
        LOGGER.info("Zero Timer: Meditate = {0} ms", String.valueOf(System.currentTimeMillis() - end));
    }

    static Cc<String, Set<Aegis>> getWalls() {
        if (CC_WALL.isEmpty()) {
            // To Avoid Filling the value more than once
            WALLS.forEach(wall -> {
                final ConcurrentMap<String, Set<Aegis>> store = CC_WALL.store();
                if (!store.containsKey(wall.getPath())) {
                    store.put(wall.getPath(), new TreeSet<>());
                }
                /*
                 * 1. group by `path`, when you define more than one wall in one path, you can collect
                 * all the wall into Set.
                 * 2. The order will be re-calculated by each group
                 * 3. But you could not define `path + order` duplicated wall
                 */
                store.get(wall.getPath()).add(wall);
            });
        }
        return CC_WALL;
    }
}
