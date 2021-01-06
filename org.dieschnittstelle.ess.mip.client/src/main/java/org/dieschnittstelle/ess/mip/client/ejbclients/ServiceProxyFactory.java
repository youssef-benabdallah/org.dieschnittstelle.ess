package org.dieschnittstelle.ess.mip.client.ejbclients;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.*;
import java.util.Properties;

/*
 * Created by master on 17.02.17.
 *
 * creates ejb proxies given a uri and a remote interface
 * alternatively creates a rest service proxy for the interface
 *
 * does some checks on the uris and interfaces
 * realised as singleton
 */
public class ServiceProxyFactory {

    /*
     * this filter logs the raw response body data
     */
    public static class LoggingFilter implements ClientResponseFilter {

        @Override
        public void filter(ClientRequestContext clientRequestContext, ClientResponseContext clientResponseContext) throws IOException {
            if (clientResponseContext.hasEntity()) {
                // we need to copy the original input stream because, once read, it will not be available to further processing anymore
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                clientResponseContext.getEntityStream().transferTo(baos);
                // this copy of the stream will be used for logging
                InputStream dataForLogging = new ByteArrayInputStream(baos.toByteArray());
                // this copy will be used for further processing, i.e. it will be re-set on the response context
                InputStream dataForFurtherProcessing = new ByteArrayInputStream(baos.toByteArray());
                clientResponseContext.setEntityStream(dataForFurtherProcessing);

                if (clientResponseContext.getStatus() != HttpStatus.SC_OK && clientResponseContext.getStatus() != HttpStatus.SC_CREATED && clientResponseContext.getStatus() != HttpStatus.SC_ACCEPTED) {
                    logger.error("got response body for status " + clientResponseContext.getStatus() +  ": " + IOUtils.toString(dataForLogging));
                }
                else {
                    logger.info("got response body for status " + clientResponseContext.getStatus() +  ": " + IOUtils.toString(dataForLogging));
                }
            }
        }
    }

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ServiceProxyFactory.class);

    // some custom runtime exception
    private static class ServiceProxyException extends RuntimeException {

        public ServiceProxyException(String msg) {
            super(msg);
        }

        public ServiceProxyException(String msg, Throwable cause) {
            super(msg, cause);
        }

    }

    private static Properties essClientProperties = new Properties();

    public static final String PROPERTY_USE_WEB_API_AS_DEFAULT = "ess.mip.client.useWebAPIAsDefault";
    public static final String PROPERTY_WEB_API_BASE_URL = "ess.mip.client.webAPIBaseUrl";

    static {
        try {
            essClientProperties.load(ServiceProxyFactory.class.getClassLoader().getResourceAsStream("ess-mip-client.properties"));
            logger.info("initialise(): loaded properties: " + essClientProperties);
        }
        catch (Exception e) {
            throw new ServiceProxyException("<static initialiser> got exception trying to read client properties: " + e,e);
        }
    }

    // the instance
    private static ServiceProxyFactory instance;

    // public method for initialising the factory
    public static void initialise(String webAPIBaseUrl,boolean useWebAPI) {
        logger.info("initialise(): useWebAPIAsDefault: " + useWebAPI);
        logger.info("initialise(): webAPIBaseUrl: " + webAPIBaseUrl);
        if (instance != null) {
            logger.warn("initialise() was called on ServiceProxyFactory, but there already exists an instance. Will not overwrite it.");
            return;
        }
        instance = new ServiceProxyFactory(webAPIBaseUrl,useWebAPI);
    }

    public static void initialise(boolean useWebAPI) {
        initialise(essClientProperties.getProperty(PROPERTY_WEB_API_BASE_URL),useWebAPI);
    }

    // alternative initialisation which will result in properties (base url and web api default usage) being read from a configuration file
    public static void initialise() {
        initialise(Boolean.valueOf(essClientProperties.getProperty(PROPERTY_USE_WEB_API_AS_DEFAULT,"false")));
    }


    // this gives us the instance
    public static ServiceProxyFactory getInstance() {
        if (instance == null) {
            throw new ServiceProxyException("getInstance() was invoked, but no instance has been created yet. Need to call initialise() before");
        }
        return instance;
    }

    // when instantiating the factory, we specify whether ejb proxies or rest service proxies shall be created
    private boolean useWebAPIAsDefault;

    // this is the client-side representation of the web api, which gives access to the different services offered via this api
    private ResteasyWebTarget webAPI;

    private ServiceProxyFactory(String webAPIBaseUrl, boolean useWebAPIAsDefault) {
        this.useWebAPIAsDefault = useWebAPIAsDefault;

        // we check whether polymorphism is handled for products and touchpoints
        if (useWebAPIAsDefault && !AbstractTouchpoint.class.isAnnotationPresent(JsonTypeInfo.class)) {
            throw new ServiceProxyException("access to web api cannot be supported as polymorphism is not handled sufficiently. Check annotations on AbstractTouchpoint! Remember to also restart the server-side application once changes have been made.");
        } else if (useWebAPIAsDefault && !AbstractProduct.class.isAnnotationPresent(JsonTypeInfo.class)) {
            logger.warn("NOTE THAT AbstractProduct might need to be prepared for polymorphism in order for WebAPI access to work overall correctly. Remember to also restart the server-side application once changes have been made.");
        } else {
            logger.info("consistency check of datamodel classes succeeded. Both ejb and web api access should work.");
        }

        if (useWebAPIAsDefault) {
            System.out.println("\n%%%%%%%%%%%% ServiceProxyFactory: services will be accessed via REST API %%%%%%%%%%%\n\n");
        }
        else {
            System.out.println("\n%%%%%%%%%%%% ServiceProxyFactory: services will be accessed via EJB proxies %%%%%%%%%%%\n\n");
        }

        try {
            // this is the webAPI instantiation - here, we hard-code the baseUrl for the webAPI, could be passed as an argument, though
            ResteasyClient client = new ResteasyClientBuilder().register(new LoggingFilter()).build();
            this.webAPI = client.target(webAPIBaseUrl);
        } catch (Exception e) {
            throw new ServiceProxyException("got exception trying to instantiate proxy factory: " + e, e);
        }

    }

    // use the default setting for whether ejb or rest service proxies shall be created
    public <T> T getProxy(Class<T> serviceInterface) {
        return getProxy(serviceInterface, "", this.useWebAPIAsDefault);
    }

    public <T> T getProxy(Class<T> serviceInterface, String ejbUri) {
        return getProxy(serviceInterface, ejbUri, this.useWebAPIAsDefault);
    }

    // allow to specify what kind of proxy shall be created
    public <T> T getProxy(Class<T> serviceInterface, String ejbUri, boolean useWebAPI) {
        T proxy;

        try {
            proxy = this.webAPI.proxy(serviceInterface);
        } catch (Exception e) {
            throw new ServiceProxyException("got exception trying to create a " + (useWebAPI ? " web service " : " EJB ") + " proxy for interface " + serviceInterface + ": " + e, e);
        }

        logger.info("getProxy(): returning proxy for " + serviceInterface + ": " + proxy);

        return proxy;
    }

    // we make transparent whether we use the webAPI as default or not
    public boolean usesWebAPIAsDefault() {
        return this.useWebAPIAsDefault;
    }

}
