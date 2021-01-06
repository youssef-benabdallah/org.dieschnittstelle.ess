package org.dieschnittstelle.ess.mip.client.ejbclients;

import java.util.List;

import org.dieschnittstelle.ess.mip.components.shopping.cart.ShoppingCartRESTService;
import org.dieschnittstelle.ess.mip.components.shopping.cart.ShoppingCart;
import org.dieschnittstelle.ess.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.ess.mip.client.Constants;

public class ShoppingCartClient implements ShoppingCart {

	private ShoppingCartRESTService serviceProxy;

	// if we are using the REST service rather than the stateful ejb, we will manage the "session id"
	// (= the id of the ShoppingCart entity) manually. In case we use the stateful ejb this will be managed inside of
	// the ejb proxy
	private long shoppingCartEntityId;

	public ShoppingCartClient() throws Exception {

		// we will use the ejb if ejbs shall be used by default
		this.serviceProxy = ServiceProxyFactory.getInstance().getProxy(ShoppingCartRESTService.class);
	}

	@Override
	public void addItem(ShoppingCartItem product) {
		serviceProxy.addItem(this.shoppingCartEntityId,product);
	}

	public List<ShoppingCartItem> getItems() {
		return serviceProxy.getItems(this.shoppingCartEntityId);
	}

	public long getShoppingCartEntityId() {
		return shoppingCartEntityId;
	}

}
