package org.dieschnittstelle.ess.mip.client.ejbclients;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.mip.components.crm.crud.CustomerTransactionCRUD;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.Customer;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;
import org.dieschnittstelle.ess.mip.client.Constants;

public class CustomerTransactionCRUDClient implements CustomerTransactionCRUD {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(CustomerTransactionCRUDClient.class);

	private CustomerTransactionCRUD ejbProxy;
	
	public CustomerTransactionCRUDClient() throws Exception {
		this.ejbProxy = EJBProxyFactory.getInstance().getProxy(CustomerTransactionCRUD.class,Constants.TRANSACTIONS_CRUD_BEAN_URI);
	}
	
	@Override
	public Collection<CustomerTransaction> readAllTransactionsForTouchpoint(
			AbstractTouchpoint touchpoint) {
		try {
			return ejbProxy.readAllTransactionsForTouchpoint(touchpoint);
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
			return ejbProxy.readAllTransactionsForCustomer(customer);
		}
		catch (Exception e) {
			logger.warn("readAllTransactionsForCustomer(): got exception: " + e + ". Look at server-side log for further information");
			return new ArrayList<CustomerTransaction>();
		}
	}

}
