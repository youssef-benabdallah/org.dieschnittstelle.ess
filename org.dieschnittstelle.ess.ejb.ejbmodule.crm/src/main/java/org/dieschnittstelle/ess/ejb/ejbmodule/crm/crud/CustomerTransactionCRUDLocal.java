package org.dieschnittstelle.ess.ejb.ejbmodule.crm.crud;

import java.util.Collection;


import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.Customer;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;

public interface CustomerTransactionCRUDLocal extends CustomerTransactionCRUD {

    public boolean createTransaction(CustomerTransaction transaction);

    public Collection<CustomerTransaction> readAllTransactionsForTouchpointAndCustomer(AbstractTouchpoint touchpoint, Customer customer);

}
