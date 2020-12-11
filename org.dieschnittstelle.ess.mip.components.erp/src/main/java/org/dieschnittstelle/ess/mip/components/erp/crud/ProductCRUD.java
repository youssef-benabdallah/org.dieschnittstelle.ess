package org.dieschnittstelle.ess.mip.components.erp.crud;

import java.util.List;

import org.dieschnittstelle.ess.entities.erp.AbstractProduct;

/*
 * TODO EJB+JPA1/2/5:
 * this interface shall be implemented using a stateless EJB with an EntityManager.
 * See TouchpointCRUDStateless for an example EJB with a similar scope of functionality
 */

public interface ProductCRUD {

	public AbstractProduct createProduct(AbstractProduct prod);

	public List<AbstractProduct> readAllProducts();

	public AbstractProduct updateProduct(AbstractProduct update);

	public AbstractProduct readProduct(long productID);

	public boolean deleteProduct(long productID);

}
