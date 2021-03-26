package org.dieschnittstelle.ess.mip.components.shopping.api;

// TODO: PAT1: this is the interface to be provided as a rest service if rest service access is used
public interface PurchaseService {

	void purchaseCartAtTouchpointForCustomer(long shoppingCartId, long touchpointId, long customerId) throws ShoppingException;
	
}
