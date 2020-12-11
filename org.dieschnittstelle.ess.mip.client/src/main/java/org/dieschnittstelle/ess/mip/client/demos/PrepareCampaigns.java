package org.dieschnittstelle.ess.mip.client.demos;

import org.dieschnittstelle.ess.mip.client.TotalUsecase;
import org.dieschnittstelle.ess.mip.client.ejbclients.EJBProxyFactory;

public class PrepareCampaigns {

	public static void main(String[] args) {
		EJBProxyFactory.initialise();

		TotalUsecase uc;
		try {
			uc = new TotalUsecase();
			uc.prepareCampaigns();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
