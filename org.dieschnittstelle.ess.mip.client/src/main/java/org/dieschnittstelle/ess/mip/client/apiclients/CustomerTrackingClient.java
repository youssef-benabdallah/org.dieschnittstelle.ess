package org.dieschnittstelle.ess.mip.client.apiclients;

import java.util.List;

import org.dieschnittstelle.ess.mip.components.crm.api.CustomerTracking;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;

public class CustomerTrackingClient implements CustomerTracking {

	private CustomerTracking serviceProxy;
	
	public CustomerTrackingClient() throws Exception {
		serviceProxy = ServiceProxyFactory.getInstance().getProxy(CustomerTracking.class);
	}
	
	@Override
	public void createTransaction(CustomerTransaction transaction) {
		serviceProxy.createTransaction(transaction);
	}

	@Override
	public List<CustomerTransaction> readAllTransactions() {
		return serviceProxy.readAllTransactions();
	}

}
