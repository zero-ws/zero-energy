package io.zerows.core.constant.configure;

import io.zerows.agreed.constant.VOption;

/**
 * @author lang : 2023-05-29
 */
interface YmlJob {
    String __KEY = "job";
    String STORE = "store";
    String CLIENT = "client";
    String INTERVAL = "interval";

    interface client extends VOption.component {

    }

    interface interval extends VOption.component {

    }

    interface store extends VOption.component {

    }
}
