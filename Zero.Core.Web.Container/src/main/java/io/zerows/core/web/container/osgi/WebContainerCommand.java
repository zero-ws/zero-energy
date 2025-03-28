package io.zerows.core.web.container.osgi;

import io.zerows.core.metadata.zdk.AbstractCommand;
import io.zerows.core.web.container.osgi.command.CommandRunningVertx;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.osgi.framework.BundleContext;

import java.util.Map;

/**
 * @author lang : 2024-05-03
 */
public class WebContainerCommand extends AbstractCommand {
    public static String[] COMMANDS = new String[]{
        "zrunning"
    };

    public WebContainerCommand(final BundleContext context) {
        super(context);
    }


    @Descriptor("View running instance of current environment.")
    public String zrunning(@Parameter(names = {"instance", "-i"}, absentValue = "") final String instance) {
        return this.execCommon(instance, Map.of(
            // running instance vertx
            "vertx", new CommandRunningVertx()
        ));
    }
}
