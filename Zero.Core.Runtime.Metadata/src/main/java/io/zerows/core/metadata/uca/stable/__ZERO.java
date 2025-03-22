package io.zerows.core.metadata.uca.stable;

interface MESSAGE {
    interface Ruler {
        /**
         *
         **/
        String RULE_FILE = "[V] Rule up.god.file = {0} has been hitted. ";
        /**
         *
         **/
        String RULE_CACHED_FILE = "[V] Rule up.god.file = {0}, read from memory directly.";

    }
}

interface Rules {
    /**
     *
     **/
    String REQUIRED = "required";
    /**
     *
     **/
    String TYPED = "typed";
    /**
     *
     **/
    String FORBIDDEN = "forbidden";
}
