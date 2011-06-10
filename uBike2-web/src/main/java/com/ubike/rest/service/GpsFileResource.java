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

import com.ubike.model.GpsFile;
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
import java.util.Collection;
import com.ubike.model.TripSegment;
import com.ubike.rest.converter.GpsFileConverter;
import javax.ejb.Stateless;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * {@code GpsFileResource}
 * <p>Utility class for dealing with persistence.</p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Component
@Scope("request")
public class GpsFileResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    protected Long id;

    /** Creates a new instance of GpsFileResource */
    public GpsFileResource() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get method for retrieving an instance of GpsFile identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of GpsFileConverter
     */
    @GET
    @Produces({"application/xml", "application/json"})
    public GpsFileConverter get(@QueryParam("expandLevel")
            @DefaultValue("1") int expandLevel) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            return new GpsFileConverter(getEntity(), uriInfo.getAbsolutePath(),
                    expandLevel);
        } finally {
            PersistenceService.getInstance().close();
        }
    }

    /**
     * Put method for updating an instance of GpsFile identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an GpsFileConverter entity that is deserialized from a XML stream
     */
    @PUT
    @Consumes({"application/xml", "application/json"})
    public void put(GpsFileConverter data) {
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
     * Delete method for deleting an instance of GpsFile identified by id.
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
     * Returns an instance of GpsFile identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of GpsFile
     */
    protected GpsFile getEntity() {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        try {
            return (GpsFile) em.createQuery("SELECT e FROM GpsFile e where e.id = :id").setParameter("id",
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
    protected GpsFile updateEntity(GpsFile entity, GpsFile newEntity) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        Collection<TripSegment> segments = entity.getSegments();
        Collection<TripSegment> segmentsNew = newEntity.getSegments();
        entity = em.merge(newEntity);
        for (TripSegment value : segments) {
            if (!segmentsNew.contains(value)) {
                throw new WebApplicationException(new Throwable("Cannot remove items from segments"));
            }
        }
        for (TripSegment value : segmentsNew) {
            if (!segments.contains(value)) {
                GpsFile oldEntity = value.getGpsFile();
                value.setGpsFile(entity);
                if (oldEntity != null && !oldEntity.equals(entity)) {
                    oldEntity.getSegments().remove(value);
                }
            }
        }
        return entity;
    }

    /**
     * Deletes the entity.
     *
     * @param entity the entity to deletle
     */
    protected void deleteEntity(GpsFile entity) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        if (!entity.getSegments().isEmpty()) {
            throw new WebApplicationException(new Throwable("Cannot delete entity because segments is not empty."));
        }
        em.remove(entity);
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

    public static class SegmentsResourceSub extends TripSegmentsResource {

        private GpsFile parent;

        public void setParent(GpsFile parent) {
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
}
