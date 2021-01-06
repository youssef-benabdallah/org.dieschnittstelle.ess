package org.dieschnittstelle.ess.mip.client.apiclients;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.mip.components.crm.crud.api.CustomerTransactionCRUD;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.Customer;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;

public class CustomerTransactionCRUDClient implements CustomerTransactionCRUD {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(CustomerTransactionCRUDClient.class);

	private CustomerTransactionCRUD serviceProxy;
	
	public CustomerTransactionCRUDClient() throws Exception {
		this.serviceProxy = ServiceProxyFactory.getInstance().getProxy(CustomerTransactionCRUD.class);
	}
	
	@Override
	public Collection<CustomerTransaction> readAllTransactionsForTouchpoint(
			AbstractTouchpoint touchpoint) {
		try {
			return serviceProxy.readAllTransactionsForTouchpoint(touchpoint);
		}
		catch (Exception e) {
			logger.warn("readAllTransactionsForTouchpoint(): got exception: " + e + ". Look at server-side log for further information");
			return new ArrayList<CustomerTransaction>();
		}
	}

	@Override
	public Collection<CustomerTransaction> readAllTransactionsForCustomer(
			Customer customer) {
		try {
			return serviceProxy.readAllTransactionsForCustomer(customer);
		}
		catch (Exception e) {
			logger.warn("readAllTransactionsForCustomer(): got exception: " + e + ". Look at server-side log for further information");
			return new ArrayList<CustomerTransaction>();
		}
	}

}
