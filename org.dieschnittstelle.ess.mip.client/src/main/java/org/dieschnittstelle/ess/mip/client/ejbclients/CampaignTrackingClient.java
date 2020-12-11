package org.dieschnittstelle.ess.mip.client.ejbclients;

import java.util.List;

import org.dieschnittstelle.ess.mip.components.crm.CampaignTracking;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;
import org.dieschnittstelle.ess.entities.crm.CampaignExecution;
import org.dieschnittstelle.ess.mip.client.Constants;

public class CampaignTrackingClient implements CampaignTracking {

	private CampaignTracking ejbProxy;
	
	public CampaignTrackingClient() throws Exception {
		ejbProxy = EJBProxyFactory.getInstance().getProxy(CampaignTracking.class,Constants.CAMPAIGN_TRACKING_BEAN_URI);
	}
	
	@Override
	public void addCampaignExecution(CampaignExecution campaign) {
		ejbProxy.addCampaignExecution(campaign);
	}

	@Override
	public int existsValidCampaignExecutionAtTouchpoint(long erpProductId,
			AbstractTouchpoint tp) {
		return ejbProxy.existsValidCampaignExecutionAtTouchpoint(erpProductId, tp);
	}

	@Override
	public void purchaseCampaignAtTouchpoint(long erpProductId,
			AbstractTouchpoint tp, int units) {
		ejbProxy.purchaseCampaignAtTouchpoint(erpProductId, tp, units);
	}

	@Override
	public List<CampaignExecution> getAllCampaignExecutions() {
		return ejbProxy.getAllCampaignExecutions();
	}

}