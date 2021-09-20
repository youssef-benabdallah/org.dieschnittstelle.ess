package org.dieschnittstelle.ess.jrs.client.openapi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.apache.logging.log4j.Logger;
import org.openapitools.api.DefaultApi;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.openapitools.model.*;
//import org.openapitools.model.ApiOpiTouchpointsAddress;
//import org.openapitools.model.OrgDieschnittstelleEssEntitiesCrmAddress;
//import org.openapitools.model.OrgDieschnittstelleEssEntitiesCrmStationaryTouchpoint;

import java.util.ArrayList;
import java.util.List;

public class RunOpenAPIRestServiceClient {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(RunOpenAPIRestServiceClient.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// create the service proxy
		JacksonJsonProvider provider = new JacksonJsonProvider();
		provider.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
		List providers = new ArrayList();
		providers.add(provider);

		DefaultApi serviceProxy = JAXRSClientFactory.create("http://localhost:8080", DefaultApi.class, providers);
		org.apache.cxf.jaxrs.client.Client client = WebClient.client(serviceProxy);

		ClientConfiguration config = WebClient.getConfig(client);

		List<OrgDieschnittstelleEssEntitiesCrmStationaryTouchpoint> tps = serviceProxy.myReadAllTouchpoints();
		logger.info("read touchpoints: " + tps);

//		// create a new touchpoint - note that all attributes with primitive types need to be set to a default value
		// as the generated code uses the wrapper types, which will be passed as null values otherwise, causing
		// server-side trouble
		OrgDieschnittstelleEssEntitiesCrmStationaryTouchpoint tp = new OrgDieschnittstelleEssEntitiesCrmStationaryTouchpoint();
		InlineResponseDefaultAddress address = new InlineResponseDefaultAddress();
		address.setCity("Berlin");
		address.setStreet("Luxemburger Str.");
		address.setZipCode("13353");
		address.setHouseNr("10");
		address.setGeoLat(0);
		address.setGeoLong(0);
		address.setId(0);
		tp.setErpPointOfSaleId(0);
		tp.setAddress(address);
		tp.setName("BHT OpenAPI Touchpoint");
		tp.setId(0);

		Object tpp = serviceProxy.createTouchpoint(tp);
		logger.info("received: " + tpp);
		
	}

}
