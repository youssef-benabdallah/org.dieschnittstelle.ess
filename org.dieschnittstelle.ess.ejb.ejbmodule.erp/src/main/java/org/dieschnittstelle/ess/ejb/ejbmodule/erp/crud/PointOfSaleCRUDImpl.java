package org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

/**
 * very rudimentary implementation without any logging... 
 */
@Logged
public class PointOfSaleCRUDImpl implements PointOfSaleCRUD {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(PointOfSaleCRUDImpl.class);

	@Inject
	@EntityManagerProvider.ERPDataAccessor
	private EntityManager em;
	
	/*
	 * TODO ADD1: comment in/out @TransactionAttribute
	 */
	@Override
	//@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public PointOfSale createPointOfSale(PointOfSale pos) {
		em.persist(pos);

		return pos;
	}

	@Override
	public PointOfSale readPointOfSale(long posId) {
		return em.find(PointOfSale.class,posId);
	}

	@Override
	public boolean deletePointOfSale(long posId) {
		em.remove(em.find(PointOfSale.class,posId));
		return true;
	}

	@Override
	public List<PointOfSale> readAllPointsOfSale() {
		return em.createQuery("SELECT p FROM PointOfSale AS p").getResultList();
	}

}
