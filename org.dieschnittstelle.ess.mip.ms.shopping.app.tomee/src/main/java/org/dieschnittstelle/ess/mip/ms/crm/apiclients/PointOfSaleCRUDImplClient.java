package org.dieschnittstelle.ess.mip.ms.crm.apiclients;

import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.PointOfSaleCRUD;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
@Logged
public class PointOfSaleCRUDImplClient implements PointOfSaleCRUD {

    @Override
    public PointOfSale createPointOfSale(PointOfSale pos) {
        return pos;
    }

    @Override
    public List<PointOfSale> readAllPointsOfSale() {
        return null;
    }

    @Override
    public PointOfSale readPointOfSale(long posId) {
        return null;
    }

    @Override
    public boolean deletePointOfSale(long posId) {
        return false;
    }
}
