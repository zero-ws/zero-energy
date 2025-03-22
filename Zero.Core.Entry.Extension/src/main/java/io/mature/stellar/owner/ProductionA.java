package io.mature.stellar.owner;

import io.horizon.eon.em.Environment;

/**
 * @author lang : 2023-06-13
 */
public class ProductionA extends AbstractPartyA {
    public ProductionA() {
        super(Environment.Production);
    }
}
