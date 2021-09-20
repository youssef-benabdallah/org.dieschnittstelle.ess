package org.dieschnittstelle.ess.jrs.opi;

import java.util.List;

import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;
import org.dieschnittstelle.ess.jrs.IProductCRUDService;

/*
 * TODO JRS4: stellen Sie hier unter der URI /opi/products den CRUD Service fuer Produkte
 *  ohne abstrakte Argument- und Rueckgabetyen zur Verfuegung und generieren Sie dann
 *  auf Basis der OpenAPI Beschreibung des Services die fuer den Zugriff erforderlichen
 *  Client Klassen
 */
public class ProductCRUDServiceOPIImpl implements IProductCRUDService {

	private IProductCRUDService service;

	// TODO: use a constructor that allows to instantiate the service attribute using the
	//  implementation created for JRS2 and passing the necessary injections (you need ServletContext)

	@Override
	public IndividualisedProductItem createProduct(
			IndividualisedProductItem prod) {
		return this.service.createProduct(prod);
	}

	@Override
	public List<IndividualisedProductItem> readAllProducts() {
		return this.service.readAllProducts();
	}

	@Override
	public IndividualisedProductItem updateProduct(long id,
			IndividualisedProductItem update) {
		return this.service.updateProduct(id,update);
	}

	@Override
	public boolean deleteProduct(long id) {
		return this.service.deleteProduct(id);
	}

	@Override
	public IndividualisedProductItem readProduct(long id) {
		return this.service.readProduct(id);
	}
	
}
