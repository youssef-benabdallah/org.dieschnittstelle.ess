package org.dieschnittstelle.ess.mip.components.crm.crud;

import java.util.List;

import org.dieschnittstelle.ess.mip.components.crm.shopping.ShoppingException;
import org.dieschnittstelle.ess.entities.crm.AbstractTouchpoint;

public interface TouchpointCRUD {

	public AbstractTouchpoint createTouchpoint(AbstractTouchpoint Touchpoint) throws ShoppingException;

	public AbstractTouchpoint readTouchpoint(long id);

	public List<AbstractTouchpoint> readAllTouchpoints();
	
	public AbstractTouchpoint updateTouchpoint(AbstractTouchpoint Touchpoint);
		
	public boolean deleteTouchpoint(int id);

}