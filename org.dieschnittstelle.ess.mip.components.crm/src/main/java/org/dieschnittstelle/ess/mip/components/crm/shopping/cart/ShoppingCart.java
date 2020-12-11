package org.dieschnittstelle.ess.mip.components.crm.shopping.cart;

import java.util.List;

import org.dieschnittstelle.ess.entities.crm.ShoppingCartItem;

public interface ShoppingCart {

	public void addItem(ShoppingCartItem product);
	
	public List<ShoppingCartItem> getItems();

}
