package com.hpe.dna.common.jersey;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.google.common.collect.ImmutableMap.of;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author chun-yang.wang@hpe.com
 */
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Logger logger = getLogger(RuntimeExceptionMapper.class);

    @Override
    public Response toResponse(RuntimeException exception) {
        logger.error(exception.getClass().getName() + " was caught.", exception);
        int status = INTERNAL_SERVER_ERROR.getStatusCode();
        if (exception instanceof WebApplicationException) {
            status = ((WebApplicationException) exception).getResponse().getStatus();
        }
        ImmutableMap<String, Object> entity = of("message", exception.getMessage(), "cause", exception.getCause());
        return Response.status(status).entity(entity).build();
    }
}
