package com.hpe.dna.common.jersey;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * @author chun-yang.wang@hpe.com
 */
@ApplicationPath("api/v1")
public class AppResourceConfig extends ResourceConfig {
	
    public AppResourceConfig() {
        packages("com.hpe.dna");
        register(JacksonFeature.class);
        register(CorsResponseFilter.class);
    }
}
