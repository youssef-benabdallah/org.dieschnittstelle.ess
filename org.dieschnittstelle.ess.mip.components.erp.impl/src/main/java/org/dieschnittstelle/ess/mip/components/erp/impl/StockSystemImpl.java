package org.dieschnittstelle.ess.mip.components.erp.impl;

import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.dieschnittstelle.ess.entities.erp.StockItem;
import org.dieschnittstelle.ess.mip.components.erp.api.StockSystem;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.PointOfSaleCRUD;
import org.dieschnittstelle.ess.mip.components.erp.crud.impl.StockItemCRUD;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@ApplicationScoped
@Transactional
@Logged
public class StockSystemImpl implements StockSystem {

    @Inject
    private PointOfSaleCRUD pointOfSaleCRUD;

    @Inject
    private StockItemCRUD stockItemCRUD;

    @Override
    public void addToStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        PointOfSale pointOfSale = pointOfSaleCRUD.readPointOfSale(pointOfSaleId);

        StockItem stockItem = stockItemCRUD.readStockItem(product, pointOfSale);

        if (stockItem == null) {
            stockItem = new StockItem(product, pointOfSale, units);
            stockItemCRUD.createStockItem(stockItem);
        } else {
            stockItem.setUnits(stockItem.getUnits() + units);
            stockItemCRUD.updateStockItem(stockItem);
        }

    }

    @Override
    public void removeFromStock(IndividualisedProductItem product, long pointOfSaleId, int units) {
        PointOfSale pointOfSale = pointOfSaleCRUD.readPointOfSale(pointOfSaleId);
        StockItem stockItem = stockItemCRUD.readStockItem(product, pointOfSale);
        if (stockItem != null) {
            stockItem.setUnits(stockItem.getUnits() - units);
            stockItemCRUD.updateStockItem(stockItem);
        }

    }

    @Override
    public List<IndividualisedProductItem> getProductsOnStock(long pointOfSaleId) {
        PointOfSale pointOfSale = pointOfSaleCRUD.readPointOfSale(pointOfSaleId);
        if(pointOfSale == null) return new ArrayList<>();

        List<StockItem> stockItemList = stockItemCRUD.readStockItemsForPointOfSale(pointOfSale);

        List<IndividualisedProductItem> individualisedProductItemList = new ArrayList<>();

        for (StockItem s:stockItemList             ) {
            individualisedProductItemList.add(s.getProduct());
        }
        System.out.println("pointOfSaleId: " + pointOfSaleId + "  && Youssef msg: " + individualisedProductItemList);

        return individualisedProductItemList ;
    }

    @Override
    public List<IndividualisedProductItem> getAllProductsOnStock() {
        List<PointOfSale> pointOfSaleList = pointOfSaleCRUD.readAllPointsOfSale();
        List<IndividualisedProductItem> individualisedProductItemList = new ArrayList<>();

        for (PointOfSale pos:pointOfSaleList             ) {
            individualisedProductItemList.addAll(getProductsOnStock(pos.getId()));
        }

        List<IndividualisedProductItem> listWithoutDuplicates = new ArrayList<>(
                new HashSet<>(individualisedProductItemList));

        return listWithoutDuplicates;
    }

    @Override
    public int getUnitsOnStock(IndividualisedProductItem product, long pointOfSaleId) {
        PointOfSale pointOfSale = pointOfSaleCRUD.readPointOfSale(pointOfSaleId);

        if(pointOfSale == null){
            List<StockItem> stockItemList = stockItemCRUD.readStockItemsForProduct(product);
            int sum = 0;
            for (StockItem stockItem:stockItemList) {
                sum += stockItem.getUnits();
            }
            return sum;
        }

        StockItem stockItem = stockItemCRUD.readStockItem(product, pointOfSale);
        return stockItem != null ? stockItem.getUnits() : 0;

    }

    @Override
    public int getTotalUnitsOnStock(IndividualisedProductItem product) {
        List<StockItem> stockItemList = stockItemCRUD.readStockItemsForProduct(product);
        int sum = 0;
        for (StockItem stockItem:stockItemList) {
            sum += stockItem.getUnits();
        }
        return sum;
    }

    @Override
    public List<Long> getPointsOfSale(IndividualisedProductItem product) {
        List<StockItem> stockItemList = stockItemCRUD.readStockItemsForProduct(product);
        List<Long> stockItemsIds = new ArrayList<>();
        for (StockItem stockItem:stockItemList) {
            stockItemsIds.add(stockItem.getPos().getId());
        }
        return stockItemsIds;
    }
}
