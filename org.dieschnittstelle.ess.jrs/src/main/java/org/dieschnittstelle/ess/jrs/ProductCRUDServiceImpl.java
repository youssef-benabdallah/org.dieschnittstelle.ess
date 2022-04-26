package org.dieschnittstelle.ess.jrs;

import org.dieschnittstelle.ess.entities.GenericCRUDExecutor;
import org.dieschnittstelle.ess.entities.erp.AbstractProduct;
import org.dieschnittstelle.ess.entities.erp.IndividualisedProductItem;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;

/*
 * TODO JRS2: implementieren Sie hier die im Interface deklarierten Methoden
 */

public class ProductCRUDServiceImpl implements IProductCRUDService {

	private GenericCRUDExecutor<AbstractProduct> productCRUD;

	public ProductCRUDServiceImpl(@Context ServletContext servletContext, @Context HttpServletRequest request) {
		this.productCRUD = (GenericCRUDExecutor<AbstractProduct>) servletContext.getAttribute("productCRUD");
	}

	private GenericCRUDExecutor<AbstractProduct> getExecutor() {
		return  this.productCRUD;
	}

	@Override
	public IndividualisedProductItem createProduct(
			IndividualisedProductItem prod) {
		return (IndividualisedProductItem) productCRUD.createObject(prod);
		// TODO Auto-generated method stub

	}

	@Override
	public List<IndividualisedProductItem> readAllProducts() {
		// TODO Auto-generated method stub
		return (List) productCRUD.readAllObjects();

	}

	@Override
	public IndividualisedProductItem updateProduct(long id,
			IndividualisedProductItem update) {
		// TODO Auto-generated method stub

		update.setId(id);
		return (IndividualisedProductItem) productCRUD.updateObject(update);
	}

	@Override
	public boolean deleteProduct(long id) {
		// TODO Auto-generated method stub
		return productCRUD.deleteObject(id);
	}

	@Override
	public IndividualisedProductItem readProduct(long id) {
		// TODO Auto-generated method stub
		return (IndividualisedProductItem) productCRUD.readObject(id);
	}
	
}
