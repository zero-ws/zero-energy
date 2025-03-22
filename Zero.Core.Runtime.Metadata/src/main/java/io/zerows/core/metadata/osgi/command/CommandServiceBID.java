package io.zerows.core.metadata.osgi.command;

import io.zerows.core.metadata.zdk.running.OCommand;
import org.osgi.framework.Bundle;

/**
 * @author lang : 2024-04-28
 */
public class CommandServiceBID implements OCommand {

    @Override
    public void execute(final Bundle caller) {
        ServiceT.bundleOr(caller);
    }
}
