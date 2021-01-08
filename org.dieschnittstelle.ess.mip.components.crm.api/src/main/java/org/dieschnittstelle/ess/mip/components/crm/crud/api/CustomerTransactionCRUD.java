package org.dieschnittstelle.ess.mip.components.crm.crud.api;

import java.util.Collection;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.Customer;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;

@Path("/transactions")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface CustomerTransactionCRUD {

	// as we take over the signature of the ejbs, we use put request passing the arguments as body (rather than
	// a get request passing an id via a query parameter)
	@PUT
	@Path("/read-for-touchpoint")
	public Collection<CustomerTransaction> readAllTransactionsForTouchpoint(AbstractTouchpoint touchpoint);

	@PUT
	@Path("/read-for-customer")
	public Collection<CustomerTransaction> readAllTransactionsForCustomer(Customer customer);

	@GET
	public Collection<CustomerTransaction> readAllTransactions();

}
