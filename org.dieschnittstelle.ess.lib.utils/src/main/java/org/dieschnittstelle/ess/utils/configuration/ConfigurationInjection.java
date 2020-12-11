package org.dieschnittstelle.ess.utils.configuration;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.utils.interceptors.LoggedInterceptor;
import org.eclipse.microprofile.config.Config;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/*
 * this class makes the microprofile configuration available to the rest of the application, see https://rieckpil.de/whatis-eclipse-microprofile-config/
 */
public class ConfigurationInjection {

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ConfigurationInjection.class);

    @Inject
    private Config config;

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        logger.info("got configuration: " + config);
    }

}
