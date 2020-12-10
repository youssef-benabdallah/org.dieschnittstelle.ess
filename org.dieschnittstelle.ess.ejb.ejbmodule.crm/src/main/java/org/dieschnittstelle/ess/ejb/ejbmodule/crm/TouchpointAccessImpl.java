package org.dieschnittstelle.ess.ejb.ejbmodule.crm;

import java.util.List;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.dieschnittstelle.ess.ejb.ejbmodule.crm.crud.TouchpointCRUD;
import org.dieschnittstelle.ess.ejb.ejbmodule.crm.shopping.ShoppingException;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.ess.ejb.ejbmodule.erp.crud.PointOfSaleCRUD;
import org.dieschnittstelle.ess.entities.erp.PointOfSale;
import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

@WebService(targetNamespace = "http://dieschnittstelle.org/ess/jws", serviceName = "TouchpointAccessWebService", endpointInterface = "org.dieschnittstelle.ess.ejb.ejbmodule.crm.TouchpointAccess")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@Logged
public class TouchpointAccessImpl implements TouchpointAccess {

	protected static Logger logger = org.apache.logging.log4j.LogManager
			.getLogger(TouchpointAccessImpl.class);

	@Inject
	private TouchpointCRUD touchpointCRUD;

	@Inject
	private PointOfSaleCRUD posCRUD;

	@Override
	public AbstractTouchpoint createTouchpointAndPointOfSale(
			AbstractTouchpoint touchpoint) throws ShoppingException {
//		logProductBundleKlass();

		// we first create the posCRUD
		PointOfSale pos = posCRUD.createPointOfSale(new PointOfSale());
		logger.info("createTouchpointAndPointOfSale(): created pointOfSale: "
				+ pos);

		// we pass the id to the touchpoint
		touchpoint.setErpPointOfSaleId(pos.getId());

		// then we persist the touchpoint
		touchpoint = touchpointCRUD.createTouchpoint(touchpoint);
		logger.info("createTouchpointAndPointOfSale(): created touchpoint: "
				+ touchpoint);

		// return it
		return touchpoint;
	}
	
	// for testing class loading
	private void logProductBundleKlass() {
		StringBuffer log = new StringBuffer();
		log.append(ShoppingCartItem.class + "\n");
		ClassLoader cl = ShoppingCartItem.class.getClassLoader();
		do {
			log.append("\t"+ cl + "\n");
			cl = cl.getParent();
		}
		while (cl != null);
		
		logger.info("class loader hierarchy of ShoppingCartItem is: \n" + log);
	}

	@Override
	public List<AbstractTouchpoint> readAllTouchpoints() {
		return touchpointCRUD.readAllTouchpoints();
	}

}
