package org.dieschnittstelle.ess.mip.components.crm.api;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import jdk.jfr.Timespan;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;

@Path("/tracking")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface CustomerTracking {

	@POST
	public void createTransaction(CustomerTransaction transaction);

	@GET
	@Path("/transactions-for-touchpoint-and-customer")
	public List<CustomerTransaction> readTransactions(@QueryParam("touchpointId") long touchpointId,@QueryParam("customerId") long customerId);

	@GET
	@Path("/transactions-for-product")
	public List<CustomerTransaction> readTransactionsForProduct(@QueryParam("productId") long productId);

}
