package io.zerows.core.metadata.eon.em;

/**
 * @author lang : 2024-04-20
 */
public final class EmDeploy {
    private EmDeploy() {
    }

    public enum Mode {
        CONFIG, // Configuration
        CODE,   // Programming
    }

    public enum Component {
        AGENT,          // Agent
        WORKER,         // Worker
        SCHEDULER,      // Scheduler
    }
}
