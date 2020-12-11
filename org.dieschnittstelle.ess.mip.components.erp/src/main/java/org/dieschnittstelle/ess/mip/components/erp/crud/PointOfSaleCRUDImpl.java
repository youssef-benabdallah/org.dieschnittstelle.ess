package org.dieschnittstelle.ess.mip.components.erp.crud;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

/**
 * very rudimentary implementation without any logging... 
 */
@RequestScoped
@Logged
@Transactional
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
