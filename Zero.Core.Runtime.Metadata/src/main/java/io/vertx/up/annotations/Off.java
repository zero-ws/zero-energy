package io.vertx.up.annotations;

import io.horizon.eon.VString;
import io.vertx.up.eon.DefaultClass;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Off {
    /*
     * Start job for the job input definition
     * - value: EventBus address
     * - outcome: income Implement class defined by `JobOutcome`
     * Message -> address
     */
    String address() default VString.EMPTY;

    
    /*
     * When there exist multi publish addresses, the address will be disabled
     * instead of send message from single point to multi points here.
     * Message -> addresses[0]
     *            addresses[1]
     *            addresses[2]
     */
    String[] addresses() default {};


    Class<?> outcome() default DefaultClass.class;
}
