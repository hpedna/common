package com.hpe.dna.common.jersey;

import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.validation.internal.ValidationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.Priorities;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.*;

/**
 * @author chun-yang.wang@hpe.com
 */
@Provider
@Priority(Priorities.USER - 10)
public class CustomValidationExceptionMapper implements ExceptionMapper<ValidationException> {
    private static final Logger logger = LoggerFactory.getLogger(CustomValidationExceptionMapper.class);
    @Context
    private Configuration config;
    @Context
    private javax.inject.Provider<Request> request;

    @Override
    public Response toResponse(ValidationException exception) {
        logger.debug("Caught ValidationException - {}", exception.getClass().getName());
        if (exception instanceof ConstraintViolationException) {

            final ConstraintViolationException cve = (ConstraintViolationException) exception;
            final Response.ResponseBuilder response = Response.status(ValidationHelper.getResponseStatus(cve));

            // Entity.
            final Object property = config.getProperty(ServerProperties.BV_SEND_ERROR_IN_RESPONSE);
            if (property != null && Boolean.valueOf(property.toString())) {
                Set<ConstraintViolation<?>> violations = cve.getConstraintViolations();
                List<Map<String, String>> errors = new ArrayList<>();
                for (ConstraintViolation<?> violation : violations) {
                    Map<String, String> error = new HashMap<>();
                    error.put("message", violation.getMessage());
                    errors.add(error);
                }
                response.entity(errors).type(MediaType.APPLICATION_JSON_TYPE);
            }

            return response.build();
        } else {
            return Response.serverError().entity(exception.getMessage()).build();
        }
    }
}
