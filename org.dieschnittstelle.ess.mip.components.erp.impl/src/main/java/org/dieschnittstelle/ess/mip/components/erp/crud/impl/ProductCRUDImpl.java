package org.dieschnittstelle.ess.mip.components.erp.crud.impl;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.Campaign;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.ProductCRUD;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
@Logged
public class ProductCRUDImpl implements ProductCRUD {

    @Inject
    @EntityManagerProvider.ERPDataAccessor
    private EntityManager entityManager;

    @Override
    public AbstractProduct createProduct(AbstractProduct prod) {
        entityManager.persist(prod);
        return prod;
    }

    @Override
    public List<AbstractProduct> readAllProducts() {
        Query query = entityManager.createQuery("SELECT p FROM AbstractProduct p");
        return query.getResultList();
    }

    @Override
    public AbstractProduct updateProduct(AbstractProduct update) {
        return null;
    }

    @Override
    public AbstractProduct readProduct(long productID) {
        return entityManager.find(AbstractProduct.class, productID);
    }

    @Override
    public boolean deleteProduct(long productID) {
        return false;
    }

    @Override
    public List<Campaign> getCampaignsForProduct(long productID) {
        return null;
    }
}
