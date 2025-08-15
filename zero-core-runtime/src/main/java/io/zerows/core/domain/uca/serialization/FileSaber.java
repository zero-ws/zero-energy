package io.zerows.core.domain.uca.serialization;

import io.vertx.up.fn.Fn;
import io.zerows.core.domain.exception._400FilePathMissingException;

import java.io.File;

class FileSaber extends AbstractSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String filename) {
        return Fn.runOr(() -> {
            final File file = new File(filename);
            // Throw 400 Error
            Fn.outWeb(!file.exists() || !file.canRead(), this.logger(),
                _400FilePathMissingException.class, this.getClass(), filename);
            return file;
        }, paramType, filename);
    }
}
