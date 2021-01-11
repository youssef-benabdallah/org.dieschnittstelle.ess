package org.dieschnittstelle.ess.mip.client;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.crm.CampaignExecution;
import org.dieschnittstelle.ess.entities.crm.CustomerTransaction;
import org.dieschnittstelle.ess.mip.client.apiclients.*;
import org.dieschnittstelle.ess.mip.client.shopping.ShoppingBusinessDelegate;
import org.dieschnittstelle.ess.mip.client.shopping.ShoppingSession;
import org.dieschnittstelle.ess.mip.client.shopping.ShoppingSessionClient;
import org.dieschnittstelle.ess.mip.components.crm.api.CampaignTracking;
import org.dieschnittstelle.ess.mip.components.crm.api.CrmException;
import org.dieschnittstelle.ess.mip.components.crm.api.TouchpointAccess;
import org.dieschnittstelle.ess.mip.components.crm.crud.api.CustomerCRUD;
import org.dieschnittstelle.ess.mip.components.crm.crud.api.CustomerTransactionCRUD;
import org.dieschnittstelle.ess.mip.components.erp.api.StockSystem;
import org.dieschnittstelle.ess.mip.components.erp.crud.api.ProductCRUD;

import java.util.Collection;

import static org.dieschnittstelle.ess.mip.client.Constants.*;
import static org.dieschnittstelle.ess.utils.Utils.step;

public class TotalUsecaseMS {

	protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(TotalUsecaseMS.class);

	public static void main(String[] args) {
		// here, we will use ejb proxies for accessing the server-side components
		ServiceProxyFactory.initialise(ServiceProxyFactory.MICROSERVICES_DEPLOYMENT);

		try {
			(new TotalUsecase()).runAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

