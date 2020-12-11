package org.dieschnittstelle.ess.mip.components.erp.crud;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ApplicationScoped
public class EntityManagerProvider {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public static @interface ERPDataAccessor {

    }

    @Produces
    @ERPDataAccessor
    @PersistenceContext(unitName = "erp_PU")
    private EntityManager em;

}