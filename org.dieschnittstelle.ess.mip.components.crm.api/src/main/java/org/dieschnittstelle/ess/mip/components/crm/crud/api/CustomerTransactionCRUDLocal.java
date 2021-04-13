package org.dieschnittstelle.ess.mip.components.crm.crud.api;


import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.Customer;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;
import java.util.List;

/*
 * MIP: for tomee, rest root resources must not be extended by interfaces that do not themselves provide
 * resource operations. Otherwise, resource implementation will not be provided. However, there is no
 * problem if the resource implementation implements an additional interface without JAX-RS background
 * for wildfly, there is no such restriction
 */
public interface CustomerTransactionCRUDLocal /*extends CustomerTransactionCRUD*/ {

    public boolean createTransaction(CustomerTransaction transaction);

    public List<CustomerTransaction> readAllTransactionsForTouchpointAndCustomer(AbstractTouchpoint touchpoint, Customer customer);

    public List<CustomerTransaction> readAllTransactions();

}
