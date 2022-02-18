package org.dieschnittstelle.ess.mip.components.crm.crud.impl;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Logged
@ApplicationScoped
@Transactional
/*
 * MIP: in contrast to hibernate, openjpa implementation does not accept id comparison for entity attributes in jpql queries.
 * comparison must be done for the id attribute of the entity attribute
 */
public class CustomerTransactionCRUDImpl implements CustomerTransactionCRUD {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(CustomerTransactionCRUDImpl.class);

	@Inject
	@EntityManagerProvider.CRMDataAccessor
	private EntityManager em;

	@Override
	public boolean createTransaction(CustomerTransaction transaction) {
		// check whether the transaction fields are detached or not
		logger.info("createTransaction(): customer attached (before): "
				+ em.contains(transaction.getCustomer()));
		logger.info("createTransaction(): touchpoint attached (before): "
				+ em.contains(transaction.getTouchpoint()));
		/*
		 * UE JPA1.1
		 */
		// persist each bundle
//		for (ShoppingCartItem item : transaction.getItems()) {
//			logger.info("createTransaction(): will manually persist item: " + item);
//			em.persist(item);
//			logger.info("createTransaction(): persisted bundle: " + item);
//		}

		// persit the transaction
		em.persist(transaction);
				
		return true;
	}

	@Override
	public List<CustomerTransaction> readAllTransactionsForTouchpoint(
			long touchpointId) {
		// as there is a bidirectional association between AbstractTouchpoint and
		// CustomerTransaction, we can also read the transactions from the touchpoint
		// object itself
		AbstractTouchpoint touchpoint = em.find(AbstractTouchpoint.class, touchpointId);

		List<CustomerTransaction> trans = new ArrayList<>(touchpoint.getTransactions());

		logger.info("readAllTransactionsForTouchpoint(): transactions on touchpoint object are: "
				+ touchpoint.getTransactions());

		return trans;
	}

	@Override
	public List<CustomerTransaction> readAllTransactionsForCustomer(
			long customerId) {
		Query query = em
				.createQuery("SELECT t FROM CustomerTransaction t WHERE t.customer.id = "
						+ customerId);
		logger.info("readAllTransactionsForCustomer(): created query: " + query);

		List<CustomerTransaction> trans = query.getResultList();
		logger.info("readAllTransactionsForCustomer(): " + trans);
		logger.info("readAllTransactionsForCustomer(): class is: "
				+ (trans == null ? "<null pointer>" : String.valueOf(trans
						.getClass())));

		return trans;
	}

	@Override
	public List<CustomerTransaction> readAllTransactions() {
		return em.createQuery("SELECT t FROM CustomerTransaction t").getResultList();
	}

	@Override
	public List<CustomerTransaction> readAllTransactionsForProduct(long productId) {
		// this is an example for a jpql query using JOIN to express conditions on
		// entities to which entities are related via a ToMany relation
		Query query = em.createQuery("SELECT t FROM CustomerTransaction t JOIN t.items item WHERE item.erpProductId = " + productId);
		return query.getResultList();
	}

	@Override
	public List<CustomerTransaction> readAllTransactionsForTouchpointAndCustomer(
			long touchpointId, long customerId) {
		Query query = em
				.createQuery("SELECT t FROM CustomerTransaction t WHERE t.customer.id = "
						+ customerId
						+ " AND t.touchpoint.id = "
						+ touchpointId);
		logger.info("readAllTransactionsForTouchpointAndCustomer(): created query: "
				+ query);

		List<CustomerTransaction> trans = query.getResultList();
		logger.info("readAllTransactionsForTouchpointAndCustomer(): " + trans);
		logger.info("readAllTransactionsForTouchpointAndCustomer(): class is: "
				+ (trans == null ? "<null pointer>" : String.valueOf(trans
						.getClass())));

		return trans;
	}

}
