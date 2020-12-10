package org.dieschnittstelle.ess.ejb.ejbmodule.crm;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;

@Path("/tracking")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface CustomerTracking {

	@POST
	public void createTransaction(CustomerTransaction transaction);

	@GET
	public List<CustomerTransaction> readAllTransactions();

}
