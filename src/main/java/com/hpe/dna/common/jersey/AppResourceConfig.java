package com.hpe.dna.common.jersey;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import javax.ws.rs.ApplicationPath;

/**
 * @author chun-yang.wang@hpe.com
 */
@ApplicationPath("api/v1")
public class AppResourceConfig extends ResourceConfig {
	
    public AppResourceConfig() {
        packages("com.hpe.dna");
        register(DateParamConverterProvider.class);
        register(JacksonFeature.class);
        register(CorsResponseFilter.class);
        register(RolesAllowedDynamicFeature.class);
        register(MultiPartFeature.class);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
    }
}
