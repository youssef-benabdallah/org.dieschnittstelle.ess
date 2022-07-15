package org.dieschnittstelle.ess.mip.components.erp.crud.impl;

import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.dieschnittstelle.ess.entities.erp.StockItem;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
@Logged
public class StockItemCRUDImpl implements  StockItemCRUD{

    @Inject
    @EntityManagerProvider.ERPDataAccessor
    private EntityManager entityManager;

    @Override
    public StockItem createStockItem(StockItem item) {
        entityManager.persist(item);
        return item;
    }

    @Override
    public StockItem readStockItem(IndividualisedProductItem prod, PointOfSale pos) {
        Query query = entityManager.createQuery("SELECT si FROM StockItem si WHERE si.product.id = " + prod.getId() + " AND si.pos.id = " + pos.getId());
        List<StockItem> results = query.getResultList();
        return results.size() == 0 ? null : results.get(0);
    }

    @Override
    public StockItem updateStockItem(StockItem item) {
        return  entityManager.merge(item);
    }

    @Override
    public List<StockItem> readStockItemsForProduct(IndividualisedProductItem prod) {
        Query query = entityManager.createQuery("SELECT si FROM StockItem si WHERE si.product.id = " + prod.getId());
        List<StockItem> results = query.getResultList();

        return results;
    }

    @Override
    public List<StockItem> readStockItemsForPointOfSale(PointOfSale pos) {
//        if(pos == null) return new ArrayList<>();
        int a = 0;
        a+=2;
        Query query = entityManager.createQuery("SELECT si FROM StockItem si WHERE si.pos.id = " + pos.getId());
        List<StockItem> results = query.getResultList();
        return results;
    }
}
