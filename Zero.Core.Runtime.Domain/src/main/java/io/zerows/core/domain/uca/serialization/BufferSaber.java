package io.zerows.core.domain.uca.serialization;

import io.vertx.core.buffer.Buffer;
import io.vertx.up.fn.Fn;

/**
 * Buffer
 */
class BufferSaber extends AbstractSaber {
    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() ->
                Fn.runOr(Buffer.class == paramType, this.logger(),
                    () -> {
                        final Buffer buffer = Buffer.buffer();
                        buffer.appendString(literal);
                        // Illegal base64 character 2f
                        // buffer.appendBytes(literal.getBytes(Values.DEFAULT_CHARSET));
                        return buffer;
                    }, Buffer::buffer),
            paramType, literal);
    }
}
