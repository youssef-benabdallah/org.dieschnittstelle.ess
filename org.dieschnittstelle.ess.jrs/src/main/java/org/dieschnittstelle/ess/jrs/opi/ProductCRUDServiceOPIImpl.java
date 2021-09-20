package org.dieschnittstelle.ess.jrs.opi;

import java.util.List;

import org.dieschnittstelle.ess.entities.crm.StationaryTouchpoint;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.jrs.IProductCRUDService;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/*
 * TODO JRS4: stellen Sie hier unter der URI /opi/products den CRUD Service fuer Produkte
 *  ohne abstrakte Argument- und Rueckgabetyen zur Verfuegung und generieren Sie dann
 *  auf Basis der OpenAPI Beschreibung des Services die fuer den Zugriff erforderlichen
 *  Client Klassen
 */
@Path("opi/products")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class ProductCRUDServiceOPIImpl implements IProductCRUDService {

	private IProductCRUDService service;

	// TODO: use a constructor that allows to instantiate the service attribute using the
	//  implementation created for JRS2 and passing the necessary injections (you need ServletContext)

	@Override
	@POST
	@APIResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON,
			schema = @Schema(type = SchemaType.OBJECT, implementation = IndividualisedProductItem.class)))
	public IndividualisedProductItem createProduct(
			IndividualisedProductItem prod) {
		return this.service.createProduct(prod);
	}

	@Override
	@GET
	@APIResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON,
			schema = @Schema(type = SchemaType.ARRAY, implementation = IndividualisedProductItem.class)))
	public List<IndividualisedProductItem> readAllProducts() {
		return this.service.readAllProducts();
	}

	@Override
	@PUT
	@Path("/{id}")
	@APIResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON,
			schema = @Schema(type = SchemaType.OBJECT, implementation = IndividualisedProductItem.class)))
	public IndividualisedProductItem updateProduct(@PathParam("id") long id,
			IndividualisedProductItem update) {
		return this.service.updateProduct(id,update);
	}

	@Override
	@DELETE
	@Path("/{id}")
	public boolean deleteProduct(@PathParam("id") long id) {
		return this.service.deleteProduct(id);
	}

	@Override
	@GET
	@Path("/{id}")
	@APIResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON,
			schema = @Schema(type = SchemaType.OBJECT, implementation = IndividualisedProductItem.class)))
	public IndividualisedProductItem readProduct(@PathParam("id") long id) {
		return this.service.readProduct(id);
	}
	
}
