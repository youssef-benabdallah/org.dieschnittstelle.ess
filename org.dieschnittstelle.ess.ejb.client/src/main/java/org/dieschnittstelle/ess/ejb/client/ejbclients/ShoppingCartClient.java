package org.dieschnittstelle.ess.ejb.client.ejbclients;

import java.util.List;

import org.dieschnittstelle.ess.ejb.ejbmodule.crm.shopping.cart.ShoppingCartRESTService;
import org.dieschnittstelle.ess.ejb.ejbmodule.crm.shopping.cart.ShoppingCart;
import org.dieschnittstelle.ess.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.ess.ejb.client.Constants;

public class ShoppingCartClient implements ShoppingCart {

	private ShoppingCart ejbProxy;

	private ShoppingCartRESTService serviceProxy;

	// if we are using the REST service rather than the stateful ejb, we will manage the "session id"
	// (= the id of the ShoppingCart entity) manually. In case we use the stateful ejb this will be managed inside of
	// the ejb proxy
	private long shoppingCartEntityId;

	public ShoppingCartClient() throws Exception {

		// we will use the ejb if ejbs shall be used by default
		if (!EJBProxyFactory.getInstance().usesWebAPIAsDefault()) {
			this.ejbProxy = EJBProxyFactory.getInstance().getProxy(ShoppingCart.class, Constants.SHOPPING_CART_BEAN_URI, false);
		}
		else {
			this.serviceProxy = EJBProxyFactory.getInstance().getProxy(ShoppingCartRESTService.class,null,true);
			// a client will be instantiated for each new shopping cart, i.e. we will obtain a cart id here
			this.shoppingCartEntityId = this.serviceProxy.createNewCart();
		}
	}

	@Override
	public void addItem(ShoppingCartItem product) {
		if (ejbProxy != null) {
			ejbProxy.addItem(product);
		}
		else {
			serviceProxy.addItem(this.shoppingCartEntityId,product);
		}
	}

	public List<ShoppingCartItem> getItems() {
		if (ejbProxy != null) {
			return ejbProxy.getItems();
		}
		else {
			return serviceProxy.getItems(this.shoppingCartEntityId);
		}
	}

	public long getShoppingCartEntityId() {
		return shoppingCartEntityId;
	}

}
