package org.dieschnittstelle.ess.mip.components.erp.crud.impl;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.Campaign;
import org.dieschnittstelle.ess.entities.erp.ProductBundle;
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

import static org.dieschnittstelle.ess.utils.Utils.show;

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
        List<AbstractProduct> prods = query.getResultList();
        show("read all products: " + prods);

        return prods;
    }

    @Override
    public AbstractProduct updateProduct(AbstractProduct update) {
        return entityManager.merge(update);
    }

    @Override
    public AbstractProduct readProduct(long productID) {
        return entityManager.find(AbstractProduct.class, productID);
    }

    @Override
    public boolean deleteProduct(long productID) {
        AbstractProduct product = readProduct(productID);
        if (product != null) {
            entityManager.remove(product);
            return true;
        }
        return false;
    }

    @Override
    public List<Campaign> getCampaignsForProduct(long productID) {
        AbstractProduct product = readProduct(productID);

        Query query = entityManager.createQuery("SELECT c FROM Campaign c JOIN c.bundles bundle WHERE bundle.product.id = " + productID);
//        return new ArrayList<>();
        return query.getResultList();
    }
}
