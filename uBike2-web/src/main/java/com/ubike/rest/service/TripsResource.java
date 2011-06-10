/*
 * Copyright 2011, Nabil Benothman, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.ubike.rest.service;

import com.ubike.model.Trip;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.api.view.Viewable;
import com.ubike.faces.bean.BaseBean;
import com.ubike.model.UbikeUser;
import com.ubike.model.Route;
import com.ubike.model.TripSegment;
import com.ubike.rest.converter.TripsConverter;
import com.ubike.rest.converter.TripConverter;
import com.ubike.services.TripServiceLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.springframework.context.annotation.Scope;
import org.springframework.security.annotation.Secured;
import org.springframework.stereotype.Component;

/**
 * {@code TripsResource}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Path("/trips/")
@Component
@Scope("request")
public class TripsResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    @EJB
    private TripServiceLocal tripService;

    /**
     * Creates a new instance of TripsResource
     */
    public TripsResource() {
    }

    /**
     * Get method for retrieving a collection of Trip instance in XML format.
     *
     * @return an instance of TripsConverter
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @GET
    @Produces({MediaType.TEXT_HTML, "application/xml", "application/json"})
    public Viewable get(
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("max") @DefaultValue("100") int max,
            @QueryParam("expandLevel") @DefaultValue("1") int expandLevel,
            @QueryParam("query") @DefaultValue("SELECT e FROM Trip e") String query) {

        TripsConverter converter = new TripsConverter(getEntities(start, max),
                uriInfo.getAbsolutePath(), expandLevel);

        BaseBean.setSessionAttribute("tmp_trips", converter.getEntities());
        return new Viewable("/tripsInfo.jsp", converter.getEntities());
    }

    /**
     * Post method for creating an instance of Trip using XML as the input format.
     *
     * @param data an TripConverter entity that is deserialized from an XML stream
     * @return an instance of TripConverter
     */
    @POST
    @Consumes({"application/xml", "application/json"})
    public Response post(TripConverter data) {
        Trip entity = data.resolveEntity(this.tripService.getEntityManager());
        createEntity(entity);
        return Response.created(uriInfo.getAbsolutePath().resolve(entity.getId() + "/")).build();
    }

    /**
     * Returns a dynamic instance of TripResource used for entity navigation.
     *
     * @return an instance of TripResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("{id}/")
    public TripResource getTripResource(@PathParam("id") Long id) {
        TripResource resource = resourceContext.getResource(TripResource.class);
        resource.setId(id);
        return resource;
    }

    /**
     *
     * @param id
     * @return
     */
    public Trip getById(Long id) {
        return this.tripService.find(id);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of Trip instances
     */
    protected List<Trip> getEntities(int start, int max) {
        return this.tripService.findRange(start, max);
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(Trip entity) {
        entity.setId(null);
        this.tripService.create(entity);
        UbikeUser owner = entity.getOwner();
        if (owner != null) {
            owner.getTrips().add(entity);
        }
        for (TripSegment value : entity.getSegments()) {
            value.setTrip(entity);
        }
        Route route = entity.getRoute();
        if (route != null) {
            route.getTrips().add(entity);
        }
    }
}
