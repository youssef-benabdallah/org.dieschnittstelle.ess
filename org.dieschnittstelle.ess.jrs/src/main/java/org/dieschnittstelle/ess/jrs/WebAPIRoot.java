package org.dieschnittstelle.ess.jrs;

import org.dieschnittstelle.ess.jrs.opi.ProductCRUDServiceOPIImpl;
import org.dieschnittstelle.ess.jrs.opi.TouchpointCRUDServiceOPIImpl;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class WebAPIRoot extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet(Arrays.asList(new Class[]{TouchpointCRUDServiceImpl.class, ProductCRUDServiceImpl.class, TouchpointCRUDServiceImplAsync.class, TouchpointCRUDServiceOPIImpl.class, ProductCRUDServiceOPIImpl.class}));
    }
}
