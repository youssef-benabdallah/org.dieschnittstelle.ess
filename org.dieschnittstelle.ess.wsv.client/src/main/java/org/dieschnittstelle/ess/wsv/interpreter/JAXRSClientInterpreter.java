package org.dieschnittstelle.ess.wsv.interpreter;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import org.apache.http.client.methods.*;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ByteArrayEntity;

import org.dieschnittstelle.ess.utils.Http;
import org.dieschnittstelle.ess.wsv.interpreter.json.JSONObjectSerialiser;

import static org.dieschnittstelle.ess.utils.Utils.*;


/*
 * TODO WSV1: implement this class such that the crud operations declared on ITouchpointCRUDService in .ess.wsv can be successfully called from the class AccessRESTServiceWithInterpreter in the .esa.wsv.client project
 */
public class JAXRSClientInterpreter implements InvocationHandler {

    // use a logger
    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(JAXRSClientInterpreter.class);

    // declare a baseurl
    private String baseurl;

    // declare a common path segment
    private String commonPath;

    // use our own implementation JSONObjectSerialiser
    private JSONObjectSerialiser jsonSerialiser = new JSONObjectSerialiser();

    // use an attribute that holds the serviceInterface (useful, e.g. for providing a toString() method)
    private Class serviceInterface;

    // use a constructor that takes an annotated service interface and a baseurl. the implementation should read out the path annotation, we assume we produce and consume json, i.e. the @Produces and @Consumes annotations will not be considered here
    public JAXRSClientInterpreter(Class serviceInterface,String baseurl) {

        // TODO: implement the constructor!  //Done
        this.serviceInterface = serviceInterface;
        this.baseurl = baseurl;
        this.commonPath = ((Path)serviceInterface.getAnnotation(Path.class)).value();

        logger.info("<constructor>: " + serviceInterface + " / " + baseurl + " / " + commonPath);
    }

    // TODO: implement this method interpreting jax-rs annotations on the meth argument
    @Override
    public Object invoke(Object proxy, Method meth, Object[] args)
            throws Throwable {

        show("invoke(): ", meth + " with args " + Arrays.asList(args != null ? args: new Class[]{}));

        // TODO check whether we handle the toString method and give some appropriate return value //Done
        if("toString".equals(meth.getName())) {
            return "JAX-RS Client Proxy for " + this.serviceInterface + ", accessing: " + this.baseurl;
        }

        // use a default http client
        HttpClient client = Http.createSyncClient();

        // TODO: create the url using baseurl and commonpath (further segments may be added if the method has an own @Path annotation) //Done
        String url = baseurl + commonPath;
        if(meth.isAnnotationPresent(Path.class)){
            url+= meth.getAnnotation(Path.class).value();
        }

        // TODO: check whether we have a path annotation and append the url (path params will be handled when looking at the method arguments)


        // a value that needs to be sent via the http request body
        Object bodyValue = null;

        // TODO: check whether we have method arguments - only consider pathparam annotations (if any) on the first argument here - if no args are passed, the value of args is null! if no pathparam annotation is present assume that the argument value is passed via the body of the http request
        if (args != null && args.length > 0) {
            if (meth.getParameterAnnotations()[0].length > 0 && meth.getParameterAnnotations()[0][0].annotationType() == PathParam.class) {
                // TODO: handle PathParam on the first argument - do not forget that in this case we might have a second argument providing a bodyValue
                // TODO: if we have a path param, we need to replace the corresponding pattern in the url with the parameter value
            }
            else {
                // if we do not have a path param, we assume the argument value will be sent via the body of the request
                bodyValue = args[0];
            }
        }

        // declare a HttpUriRequest variable
        HttpUriRequest request = null;

        // TODO: check which of the http method annotation is present and instantiate request accordingly passing the url //Done

        if (meth.isAnnotationPresent(POST.class)){
            request = new HttpPost(url);
        } else
        if (meth.isAnnotationPresent(GET.class)){
            request = new HttpGet(url);
        } else
        if (meth.isAnnotationPresent(DELETE.class)){
            request = new HttpDelete(url);
        } else
        if (meth.isAnnotationPresent(PUT.class)){
            request = new HttpPut(url);
        }
        else {
            System.out.println(Arrays.toString(meth.getAnnotations()));
            throw new UnsupportedOperationException("JAX-RS demo can only GET, Post, DELETE, PUT annotations");
        }
        // TODO: add a header on the request declaring that we accept json (for header names, you can use the constants declared in javax.ws.rs.core.HttpHeaders, for content types use the constants from javax.ws.rs.core.MediaType;) //Done
        request.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);


        // if we need to send the method argument in the request body we need to declare an entity
        ByteArrayEntity bae = null;

        // if a body shall be sent, convert the bodyValue to json, create an entity from it and set it on the request
        if (bodyValue != null) {

            // TODO: use a ByteArrayOutputStream for writing json //Done
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // TODO: write the object to the stream using the jsonSerialiser //Done
            jsonSerialiser.writeObject(bodyValue, byteArrayOutputStream);

            byte[] jsonDataForBody = byteArrayOutputStream.toByteArray();

            // TODO: create an ByteArrayEntity from the stream's content //Done
            bae = new ByteArrayEntity(jsonDataForBody);


            // TODO: set the entity on the request, which must be cast to HttpEntityEnclosingRequest //Done
            ((HttpEntityEnclosingRequest)request).setEntity(bae);

            // TODO: and add a content type header for the request //Done
            request.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
        }

        logger.info("invoke(): executing request: " + request);

        // then send the request to the server and get the response
        HttpResponse response = client.execute(request);

        logger.info("invoke(): received response: " + response);

        // check the response code
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            // declare a variable for the return value
            Object returnValue = null;

            // TODO: convert the resonse body to a java object of an appropriate type considering the return type of the method as returned by getGenericReturnType() and set the object as value of returnValue //Done
            Type returnValueType = meth.getGenericReturnType();
            InputStream returnValueJSONData = response.getEntity().getContent();

            returnValue = jsonSerialiser.readObject(returnValueJSONData, returnValueType);
            // and return the return value
            logger.info("invoke(): returning value: " + returnValue);
            return returnValue;

        }
        else {
            throw new RuntimeException("Got unexpected status from server: " + response.getStatusLine());
        }
    }

}