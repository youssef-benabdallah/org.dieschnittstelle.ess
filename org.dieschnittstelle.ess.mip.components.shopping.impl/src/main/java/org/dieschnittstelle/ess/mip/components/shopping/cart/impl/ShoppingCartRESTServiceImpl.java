package org.dieschnittstelle.ess.mip.components.shopping.cart.impl;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.mip.components.shopping.cart.api.ShoppingCartRESTService;
import org.dieschnittstelle.ess.mip.components.shopping.impl.EntityManagerProvider;
import org.dieschnittstelle.ess.entities.crm.ShoppingCartItem;
import org.dieschnittstelle.ess.utils.interceptors.Logged;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by master on 20.02.17.
 *
 * actually, this is a CRUD ejb that uses the entity manager for persisting shopping cart instances. Note, however, that the ShoppingCart class itself is not exposed via the REST interface
 */
@ApplicationScoped
@Logged
@Transactional
public class ShoppingCartRESTServiceImpl implements ShoppingCartRESTService {

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(ShoppingCartRESTServiceImpl.class);

    @Inject
    @EntityManagerProvider.ShoppingDataAccessor
    private EntityManager em;

    // the values for this property a provided by the microprofile config file
    // which is part of the web application project, see https://rieckpil.de/whatis-eclipse-microprofile-config/
//    @Inject
    // TODO MP: handle property injection
    @ConfigProperty(name = "shoppingcart.idletimeoutms", defaultValue = "3600000")
    private long idleTimeout;

    public ShoppingCartRESTServiceImpl() {
        logger.info("<constructor>");
    }

    @Override
    public long createNewCart() {
        logger.info("createNewCart(): idleTimeout is: " + this.idleTimeout);
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
