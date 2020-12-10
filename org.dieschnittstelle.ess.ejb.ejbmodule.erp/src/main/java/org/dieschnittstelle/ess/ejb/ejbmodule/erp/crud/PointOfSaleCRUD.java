package org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import org.dieschnittstelle.ess.entities.erp.PointOfSale;

@Path("/pointsOfSale")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface PointOfSaleCRUD {

	@POST
	public PointOfSale createPointOfSale(PointOfSale pos);

	@GET
	public List<PointOfSale> readAllPointsOfSale();

	@GET
	@Path("/{posId}")
	public PointOfSale readPointOfSale(@PathParam("posId") long posId);

	@DELETE
	@Path("/{posId}")
	public boolean deletePointOfSale(@PathParam("posId") long posId);

}
