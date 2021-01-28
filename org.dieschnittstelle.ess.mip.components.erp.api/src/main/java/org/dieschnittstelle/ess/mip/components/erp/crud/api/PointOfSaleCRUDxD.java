package org.dieschnittstelle.ess.mip.components.erp.crud.api;

import org.dieschnittstelle.ess.entities.erp.PointOfSale;

import javax.inject.Qualifier;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public interface PointOfSaleCRUDxD {

	public PointOfSale createPointOfSale(PointOfSale pos);

	public List<PointOfSale> readAllPointsOfSale();

	public PointOfSale readPointOfSale(long posId);

	public boolean deletePointOfSale(long posId);

}
