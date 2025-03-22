package io.zerows.core.assembly.osgi.command;

import io.zerows.core.metadata.zdk.running.OCommand;
import org.osgi.framework.Bundle;

/**
 * @author lang : 2024-05-02
 */
public class CommandClassesBID implements OCommand {
    @Override
    public void execute(final Bundle caller) {
        ServiceT.bundleOr(caller);
    }
}
