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

import com.sun.jersey.api.NotFoundException;
import com.ubike.model.Trip;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import com.sun.jersey.api.core.ResourceContext;
import com.sun.jersey.api.view.Viewable;
import javax.ws.rs.WebApplicationException;
import javax.persistence.NoResultException;
import java.util.Collection;
import com.ubike.model.UbikeUser;
import com.ubike.model.Route;
import com.ubike.model.TripSegment;
import com.ubike.rest.converter.TripConverter;
import com.ubike.services.TripServiceLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * {@code TripResource}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Component
@Scope("request")
public class TripResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    protected Long id;
    @EJB
    private TripServiceLocal tripService;

    /**
     * Creates a new instance of TripResource
     */
    public TripResource() {
    }

    /**
     * @param id The id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    @GET
    @Produces({MediaType.TEXT_HTML})
    public Viewable get(@QueryParam("expandLevel") @DefaultValue("0") int expandLevel) {
        TripConverter converter = new TripConverter(getEntity(), uriInfo.getAbsolutePath(), expandLevel);
        return new Viewable("/tripInfo.jsp", converter.getEntity());
    }

    /**
     * Get method for retrieving an instance of Trip identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of TripConverter
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public TripConverter getXML(@QueryParam("expandLevel")
            @DefaultValue("1") int expandLevel) {
        return new TripConverter(getEntity(), uriInfo.getAbsolutePath(),
                expandLevel);
    }

    /**
     * Put method for updating an instance of Trip identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an TripConverter entity that is deserialized from a XML stream
     */
    @PUT
    @Consumes({"application/xml", "application/json"})
    public void put(TripConverter data) {
        updateEntity(getEntity(), data.resolveEntity(this.tripService.getEntityManager()));
    }

    /**
     * Delete method for deleting an instance of Trip identified by id.
     *
     * @param id identifier for the entity
     */
    @DELETE
    public void delete() {
        deleteEntity(getEntity());
    }

    /**
     * Returns an instance of Trip identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of Trip
     */
    public Trip getEntity() {
        try {
            return this.tripService.findWithMapCode(id);
        } catch (NoResultException ex) {
            throw new NotFoundException("Resource for " + uriInfo.getAbsolutePath() + " does not exist.",
                    uriInfo.getAbsolutePath());
        }
    }

    /**
     * Updates entity using data from newEntity.
     *
     * @param entity the entity to update
     * @param newEntity the entity containing the new data
     * @return the updated entity
     */
    protected Trip updateEntity(Trip entity, Trip newEntity) {
        UbikeUser owner = entity.getOwner();
        UbikeUser ownerNew = newEntity.getOwner();
        Collection<TripSegment> segments = entity.getSegments();
        Collection<TripSegment> segmentsNew = newEntity.getSegments();
        Route route = entity.getRoute();
        Route routeNew = newEntity.getRoute();
        this.tripService.update(newEntity);
        if (owner != null && !owner.equals(ownerNew)) {
            owner.getTrips().remove(entity);
        }
        if (ownerNew != null && !ownerNew.equals(owner)) {
            ownerNew.getTrips().add(entity);
        }
        for (TripSegment value : segments) {
            if (!segmentsNew.contains(value)) {
                throw new WebApplicationException(new Throwable("Cannot remove items from segments"));
            }
        }
        for (TripSegment value : segmentsNew) {
            if (!segments.contains(value)) {
                Trip oldEntity = value.getTrip();
                value.setTrip(entity);
                if (oldEntity != null && !oldEntity.equals(entity)) {
                    oldEntity.getSegments().remove(value);
                }
            }
        }
        if (route != null && !route.equals(routeNew)) {
            route.getTrips().remove(entity);
        }
        if (routeNew != null && !routeNew.equals(route)) {
            routeNew.getTrips().add(entity);
        }
        return entity;
    }

    /**
     * Deletes the entity.
     *
     * @param entity the entity to delete
     */
    protected void deleteEntity(Trip entity) {
        UbikeUser owner = entity.getOwner();
        if (owner != null) {
            owner.getTrips().remove(entity);
        }
        if (!entity.getSegments().isEmpty()) {
            throw new WebApplicationException(new Throwable("Cannot delete entity because segments is not empty."));
        }
        Route route = entity.getRoute();
        if (route != null) {
            route.getTrips().remove(entity);
        }
        this.tripService.remove(id);
    }

    /**
     * Returns a dynamic instance of UbikeUserResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of UbikeUserResource
     */
    @Path("owner/")
    public UbikeUserResource getOwnerResource() {
        OwnerResourceSub resource = resourceContext.getResource(OwnerResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    /**
     * Returns a dynamic instance of TripSegmentsResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of TripSegmentsResource
     */
    @Path("segments/")
    public TripSegmentsResource getSegmentsResource() {
        SegmentsResourceSub resource = resourceContext.getResource(SegmentsResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    /**
     * Returns a dynamic instance of RouteResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of RouteResource
     */
    @Path("route/")
    public RouteResource getRouteResource() {
        RouteResourceSub resource = resourceContext.getResource(RouteResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    public static class OwnerResourceSub extends UbikeUserResource {

        private Trip parent;

        public void setParent(Trip parent) {
            this.parent = parent;
        }

        @Override
        public UbikeUser getEntity() {
            UbikeUser entity = parent.getOwner();
            if (entity == null) {
                throw new WebApplicationException(new Throwable("Resource for " + uriInfo.getAbsolutePath() + " does not exist."),
                        404);
            }
            return entity;
        }
    }

    public static class SegmentsResourceSub extends TripSegmentsResource {

        private Trip parent;

        public void setParent(Trip parent) {
            this.parent = parent;
        }

        @Override
        protected Collection<TripSegment> getEntities(int start, int max,
                String query) {
            Collection<TripSegment> result = new java.util.ArrayList<TripSegment>();
            int index = 0;
            for (TripSegment e : parent.getSegments()) {
                if (index >= start && (index - start) < max) {
                    result.add(e);
                }
                index++;
            }
            return result;
        }
    }

    public static class RouteResourceSub extends RouteResource {

        private Trip parent;

        public void setParent(Trip parent) {
            this.parent = parent;
        }

        @Override
        protected Route getEntity() {
            Route entity = parent.getRoute();
            if (entity == null) {
                throw new WebApplicationException(new Throwable("Resource for " + uriInfo.getAbsolutePath() + " does not exist."),
                        404);
            }
            return entity;
        }
    }
}
