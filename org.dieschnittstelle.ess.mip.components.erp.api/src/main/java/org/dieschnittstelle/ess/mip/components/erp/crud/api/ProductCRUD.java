package org.dieschnittstelle.ess.mip.components.erp.crud.api;

import java.awt.*;
import java.util.List;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.Campaign;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.interceptor.Interceptor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/*
 * TODO MIP+JPA1/2/5:
 * this interface shall be implemented using an ApplicationScoped CDI bean with an EntityManager.
 * See TouchpointCRUDImpl for an example bean with a similar scope of functionality
 */

@RegisterRestClient
@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ProductCRUD {

	@POST
	public AbstractProduct createProduct(AbstractProduct prod);

	@GET
	public List<AbstractProduct> readAllProducts();

	@PUT
	public AbstractProduct updateProduct(AbstractProduct update);

	@GET
	@Path("/{productID}")
	public AbstractProduct readProduct(@PathParam("productID") long productID);

	@DELETE
	@Path("/{productID}")
	public boolean deleteProduct(@PathParam("productID") long productID);

	@GET
	@Path("/{productID}/campaigns")
	public List<Campaign> getCampaignsForProduct(@PathParam("productID")long productID);

}
