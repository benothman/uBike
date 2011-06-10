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

import com.ubike.model.TripSegment;
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
import javax.ws.rs.WebApplicationException;
import javax.persistence.NoResultException;
import javax.persistence.EntityManager;
import com.ubike.model.GpsFile;
import com.ubike.model.Trip;
import com.ubike.rest.converter.TripSegmentConverter;
import javax.ejb.Stateless;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * {@code TripSegmentResource}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Component
@Scope("request")
public class TripSegmentResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    protected Long id;

    /** Creates a new instance of TripSegmentResource */
    public TripSegmentResource() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get method for retrieving an instance of TripSegment identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of TripSegmentConverter
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public TripSegmentConverter get(@QueryParam("expandLevel")
            @DefaultValue("1") int expandLevel) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            return new TripSegmentConverter(getEntity(),
                    uriInfo.getAbsolutePath(), expandLevel);
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Put method for updating an instance of TripSegment identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an TripSegmentConverter entity that is deserialized from a XML stream
     */
    @PUT
    @Consumes({"application/xml", "application/json"})
    public void put(TripSegmentConverter data) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            EntityManager em = persistenceSvc.getEntityManager();
            updateEntity(getEntity(), data.resolveEntity(em));
            persistenceSvc.commitTx();
        } finally {
            persistenceSvc.close();
        }
    }

    /**
     * Delete method for deleting an instance of TripSegment identified by id.
     *
     * @param id identifier for the entity
     */
    @DELETE
    public void delete() {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            deleteEntity(getEntity());
            persistenceSvc.commitTx();
        } finally {
            persistenceSvc.close();
        }
    }

    /**
     * Returns an instance of TripSegment identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of TripSegment
     */
    protected TripSegment getEntity() {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        try {
            return (TripSegment) em.createQuery("SELECT e FROM TripSegment e where e.id = :id").setParameter("id",
                    id).getSingleResult();
        } catch (NoResultException ex) {
            throw new WebApplicationException(new Throwable("Resource for " + uriInfo.getAbsolutePath() + " does not exist."),
                    404);
        }
    }

    /**
     * Updates entity using data from newEntity.
     *
     * @param entity the entity to update
     * @param newEntity the entity containing the new data
     * @return the updated entity
     */
    protected TripSegment updateEntity(TripSegment entity, TripSegment newEntity) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        GpsFile gpsFile = entity.getGpsFile();
        GpsFile gpsFileNew = newEntity.getGpsFile();
        Trip trip = entity.getTrip();
        Trip tripNew = newEntity.getTrip();
        entity = em.merge(newEntity);
        if (gpsFile != null && !gpsFile.equals(gpsFileNew)) {
            gpsFile.getSegments().remove(entity);
        }
        if (gpsFileNew != null && !gpsFileNew.equals(gpsFile)) {
            gpsFileNew.getSegments().add(entity);
        }
        if (trip != null && !trip.equals(tripNew)) {
            trip.getSegments().remove(entity);
        }
        if (tripNew != null && !tripNew.equals(trip)) {
            tripNew.getSegments().add(entity);
        }
        return entity;
    }

    /**
     * Deletes the entity.
     *
     * @param entity the entity to deletle
     */
    protected void deleteEntity(TripSegment entity) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        GpsFile gpsFile = entity.getGpsFile();
        if (gpsFile != null) {
            gpsFile.getSegments().remove(entity);
        }
        Trip trip = entity.getTrip();
        if (trip != null) {
            trip.getSegments().remove(entity);
        }
        em.remove(entity);
    }

    /**
     * Returns a dynamic instance of GpsFileResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of GpsFileResource
     */
    @Path("gpsFile/")
    public GpsFileResource getGpsFileResource() {
        GpsFileResourceSub resource = resourceContext.getResource(GpsFileResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    /**
     * Returns a dynamic instance of TripResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of TripResource
     */
    @Path("trip/")
    public TripResource getTripResource() {
        TripResourceSub resource = resourceContext.getResource(TripResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    public static class GpsFileResourceSub extends GpsFileResource {

        private TripSegment parent;

        public void setParent(TripSegment parent) {
            this.parent = parent;
        }

        @Override
        protected GpsFile getEntity() {
            GpsFile entity = parent.getGpsFile();
            if (entity == null) {
                throw new WebApplicationException(new Throwable("Resource for " + uriInfo.getAbsolutePath() + " does not exist."),
                        404);
            }
            return entity;
        }
    }

    public static class TripResourceSub extends TripResource {

        private TripSegment parent;

        public void setParent(TripSegment parent) {
            this.parent = parent;
        }

        @Override
        public Trip getEntity() {
            Trip entity = parent.getTrip();
            if (entity == null) {
                throw new WebApplicationException(new Throwable("Resource for " + uriInfo.getAbsolutePath() + " does not exist."),
                        404);
            }
            return entity;
        }
    }
}
