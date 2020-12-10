package org.dieschnittstelle.ess.ejb.ejbmodule.crm.shopping.cart;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.ejb.ejbmodule.crm.crud.EntityManagerProvider;
import org.dieschnittstelle.ess.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.ess.utils.interceptors.Logged;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by master on 20.02.17.
 *
 * actually, this is a CRUD ejb that uses the entity manager for persisting shopping cart instances. Note, however, that the ShoppingCart class itself is not exposed via the REST interface
 */
@Singleton
@Logged
public class ShoppingCartRESTServiceImpl implements ShoppingCartRESTService {

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ShoppingCartRESTServiceImpl.class);

    @Inject
    @EntityManagerProvider.CRMDataAccessor
    private EntityManager em;

    // here, the value of the env-entry idle-timeout specified in ejb-jar.xml will be injected
    @Resource(name = "idle-timeout")
    private long idleTimeout;

    public ShoppingCartRESTServiceImpl() {
        logger.info("<constructor>");
    }

    @Override
    public long createNewCart() {
        ShoppingCartEntity sc = new ShoppingCartEntity();
        em.persist(sc);
        return sc.getId();
    }

    // note that it is not necessary to explicitly call merge, as merging will done automatically once the transaction associated with this method is committed
    @Override
    public void addItem(long cartId, ShoppingCartItem product) {
        em.find(ShoppingCartEntity.class,cartId).addItem(product);


    }

    @Override
    public List<ShoppingCartItem> getItems(long cartId) {
        return em.find(ShoppingCartEntity.class,cartId).getItems();
    }

    @Override
    public boolean deleteCart(long cartId) {
        em.remove(em.find(ShoppingCartEntity.class,cartId));
        return true;
    }

    // TODO MP: add scheduler
    // https://developer.ibm.com/tutorials/write-a-simple-microprofile-application-3/
    // if a task shall be scheduled every couple of seconds, also hour and minute need to be specied as "any" ('*')
    // because these attributes default to 0
//    @Schedule(second="*/30", hour="*", minute = "*", persistent = false)
    public void removeIdleCarts() {
        logger.info("removeIdleCarts(): idleTimeout is set to: " + idleTimeout);

        // read all carts
        for (ShoppingCartEntity scart : (List<ShoppingCartEntity>)em.createQuery("SELECT c FROM ShoppingCartStateful AS c").getResultList()) {
            if (System.currentTimeMillis() - scart.getLastUpdated() > idleTimeout) {
                logger.info("ShoppingCart has exceeded idle time. Will remove it: " + scart.getId());
                deleteCart(scart.getId());
            }
            else {
                logger.info("ShoppingCart has not yet exceeded idle time. Keep it: " + scart.getId());
            }
        }

    }


}
