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

import com.ubike.model.Route;
import java.util.Collection;
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
import javax.persistence.EntityManager;
import com.ubike.model.Trip;
import com.ubike.rest.converter.RoutesConverter;
import com.ubike.rest.converter.RouteConverter;
import javax.ejb.Stateless;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * {@code RoutesResource}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Path("/routes/")
@Component
@Scope("request")
public class RoutesResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;

    /** 
     * Creates a new instance of RoutesResource 
     */
    public RoutesResource() {
    }

    /**
     * Get method for retrieving a collection of Route instance in XML format.
     *
     * @return an instance of RoutesConverter
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public RoutesConverter get(@QueryParam("start")
            @DefaultValue("0") int start,
            @QueryParam("max")
            @DefaultValue("10") int max,
            @QueryParam("expandLevel")
            @DefaultValue("1") int expandLevel,
            @QueryParam("query")
            @DefaultValue("SELECT e FROM Route e") String query) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            return new RoutesConverter(getEntities(start, max, query),
                    uriInfo.getAbsolutePath(), expandLevel);
        } finally {
            persistenceSvc.commitTx();
            persistenceSvc.close();
        }
    }

    /**
     * Post method for creating an instance of Route using XML as the input format.
     *
     * @param data an RouteConverter entity that is deserialized from an XML stream
     * @return an instance of RouteConverter
     */
    @POST
    @Consumes({"application/xml", "application/json"})
    public Response post(RouteConverter data) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            EntityManager em = persistenceSvc.getEntityManager();
            Route entity = data.resolveEntity(em);
            createEntity(data.resolveEntity(em));
            persistenceSvc.commitTx();
            return Response.created(uriInfo.getAbsolutePath().resolve(entity.getId() + "/")).build();
        } finally {
            persistenceSvc.close();
        }
    }

    /**
     * Returns a dynamic instance of RouteResource used for entity navigation.
     *
     * @return an instance of RouteResource
     */
    @Path("{id}/")
    public RouteResource getRouteResource(@PathParam("id") Long id) {
        RouteResource resource = resourceContext.getResource(RouteResource.class);
        resource.setId(id);
        return resource;
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of Route instances
     */
    protected Collection<Route> getEntities(int start, int max, String query) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        return em.createQuery(query).setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(Route entity) {
        entity.setId(null);
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        em.persist(entity);
        for (Trip value : entity.getTrips()) {
            Route oldEntity = value.getRoute();
            value.setRoute(entity);
            if (oldEntity != null) {
                oldEntity.getTrips().remove(entity);
            }
        }
    }
}
