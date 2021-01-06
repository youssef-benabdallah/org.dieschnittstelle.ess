package org.dieschnittstelle.ess.mip.client.ejbclients;

import java.util.List;

import org.dieschnittstelle.ess.mip.components.crm.CrmException;
import org.dieschnittstelle.ess.mip.components.shopping.ShoppingException;
import org.dieschnittstelle.ess.mip.components.crm.TouchpointAccess;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.mip.client.Constants;

public class TouchpointAccessClient implements TouchpointAccess {
	
	private TouchpointAccess ejbProxy;
	
	public TouchpointAccessClient() throws Exception {
		this.ejbProxy = EJBProxyFactory.getInstance().getProxy(TouchpointAccess.class, Constants.TOUCHPOINT_ACCESS_BEAN_URI);
	}
	
	
	public List<AbstractTouchpoint> readAllTouchpoints() {
		return ejbProxy.readAllTouchpoints();
	}

	@Override
	public AbstractTouchpoint createTouchpointAndPointOfSale(AbstractTouchpoint touchpoint) throws CrmException {
		AbstractTouchpoint created = ejbProxy.createTouchpointAndPointOfSale(touchpoint);
		touchpoint.setId(created.getId());
		touchpoint.setErpPointOfSaleId(created.getErpPointOfSaleId());
		
		return created;
	}
		
}
