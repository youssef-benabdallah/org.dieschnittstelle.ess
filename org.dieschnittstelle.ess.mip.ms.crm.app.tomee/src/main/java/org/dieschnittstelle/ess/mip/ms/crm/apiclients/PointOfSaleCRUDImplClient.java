package org.dieschnittstelle.ess.mip.ms.crm.apiclients;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.PointOfSaleCRUD;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.PointOfSaleCRUDxD;
import org.dieschnittstelle.ess.utils.interceptors.Logged;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;

/*
 * this qualifier is used to distinguish the dependencies to PointOfSaleCRUDImplClient from the level
 * of other beans (concretely, TouchpointAccessImpl) from the local dependency to the rest client
 * that uses the same interface, let's see...
 */
@RequestScoped
@Logged
public class PointOfSaleCRUDImplClient implements PointOfSaleCRUDxD {

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(PointOfSaleCRUDImplClient.class);

    @Inject
    @RestClient
    private PointOfSaleCRUD serviceProxy;

    @Override
    public PointOfSale createPointOfSale(PointOfSale pos) {
        logger.info("createPointOfSale(): " + pos);
        logger.info("createPointOfSale(): serviceProxy is: " + serviceProxy);
        return serviceProxy.createPointOfSale(pos);
    }

    @Override
    public List<PointOfSale> readAllPointsOfSale() {
        return serviceProxy.readAllPointsOfSale();
    }

    @Override
    public PointOfSale readPointOfSale(long posId) {
        return serviceProxy.readPointOfSale(posId);
    }

    @Override
    public boolean deletePointOfSale(long posId) {
        return serviceProxy.deletePointOfSale(posId);
    }
}
