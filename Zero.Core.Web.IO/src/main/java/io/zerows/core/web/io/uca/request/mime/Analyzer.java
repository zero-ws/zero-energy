package io.zerows.core.web.io.uca.request.mime;

import io.horizon.exception.WebException;
import io.zerows.core.web.io.uca.request.mime.parse.Income;
import io.zerows.core.web.model.atom.Event;
import io.zerows.core.web.model.commune.Envelop;

/**
 * Mime resolution for web request
 * 1. Server Driven
 * 1.1. Content-Type
 * 1.2. Content-Length
 * 1.3. Content-Encoding
 * 2. Client Driven
 * 2.1. Accept
 * 2.2. Accept-Charset
 * 2.3. Accept-Encoding
 * 2.4. Accept-Language
 * 3. Vary
 * New resource model usage for this analyzer.
 */
public interface Analyzer extends Income<Object[]> {
    /**
     * response mime analyzing
     *
     * @param envelop Input Request of uniform model
     * @param event   Event definition
     *
     * @return Normalized Request
     * @throws WebException Common exception
     */
    Envelop out(Envelop envelop, Event event)
        throws WebException;
}
