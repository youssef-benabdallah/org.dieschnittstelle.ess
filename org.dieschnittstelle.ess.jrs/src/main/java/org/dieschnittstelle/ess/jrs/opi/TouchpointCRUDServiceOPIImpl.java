package org.dieschnittstelle.ess.jrs.opi;

import org.apache.logging.log4j.Logger;
import org.dieschnittstelle.ess.entities.crm.StationaryTouchpoint;
import org.dieschnittstelle.ess.jrs.ITouchpointCRUDService;
import org.dieschnittstelle.ess.jrs.TouchpointCRUDServiceImpl;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("opi/touchpoints")
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class TouchpointCRUDServiceOPIImpl {

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(TouchpointCRUDServiceOPIImpl.class);

    private ITouchpointCRUDService service;

    public TouchpointCRUDServiceOPIImpl() {

    }

    /**
     * here we will be passed the context parameters by the resteasy framework. Alternatively @Context
     * can  be declared on the respective instance attributes. note that the request context is only
     * declared for illustration purposes, but will not be further used here
     *
     * @param servletContext
     */
    public TouchpointCRUDServiceOPIImpl(@Context ServletContext servletContext, @Context HttpServletRequest request) {
        this.service = new TouchpointCRUDServiceImpl(servletContext,request);
    }

    @GET
    @APIResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(type = SchemaType.ARRAY, implementation = StationaryTouchpoint.class)))
    public List<StationaryTouchpoint> readAllTouchpoints() {
        return (List)this.service.readAllTouchpoints();
    }

    @POST
    @APIResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(type = SchemaType.OBJECT, implementation = StationaryTouchpoint.class)))
    public StationaryTouchpoint createTouchpoint(StationaryTouchpoint touchpoint) {
        return (StationaryTouchpoint) this.service.createTouchpoint( touchpoint);
    }

    @DELETE
    @Path("/{id}")
    public boolean deleteTouchpoint(@PathParam("id") long id) {
        return this.service.deleteTouchpoint(id);
    }

    @GET
    @Path("/{id}")
    @APIResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON,
            schema = @Schema(type = SchemaType.OBJECT, implementation = StationaryTouchpoint.class)))
    public StationaryTouchpoint readTouchpoint(@PathParam("id") long id) {
        return (StationaryTouchpoint)this.service.readTouchpoint(id);
    }

}
