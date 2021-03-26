package org.dieschnittstelle.ess.mip.components.crm.crud.api;

import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.Customer;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/transactions")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface CustomerTransactionCRUD {

	// as we take over the signature of the ejbs, we use put requests passing the arguments as body (rather than
	// a get request passing an id via a query parameter)
	@PUT
	@Path("/read-for-touchpoint")
	public List<CustomerTransaction> readAllTransactionsForTouchpoint(AbstractTouchpoint touchpoint);

	@PUT
	@Path("/read-for-customer")
	public List<CustomerTransaction> readAllTransactionsForCustomer(Customer customer);

	@GET
	public List<CustomerTransaction> readAllTransactions();


}
