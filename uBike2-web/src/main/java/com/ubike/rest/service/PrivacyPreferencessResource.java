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

import com.ubike.model.PrivacyPreferences;
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
import com.ubike.model.UbikeUser;
import com.ubike.rest.converter.PrivacyPreferencessConverter;
import com.ubike.rest.converter.PrivacyPreferencesConverter;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.springframework.context.annotation.Scope;
import org.springframework.security.annotation.Secured;
import org.springframework.stereotype.Component;

/**
 * {@code PrivacyPreferencessResource}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Path("/privacyPreferencess/")
@Component
@Scope("request")
public class PrivacyPreferencessResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;

    /** 
     * Creates a new instance of PrivacyPreferencessResource 
     */
    public PrivacyPreferencessResource() {
    }

    /**
     * Get method for retrieving a collection of PrivacyPreferences instance in XML format.
     *
     * @return an instance of PrivacyPreferencessConverter
     */
    @Secured({"ROLE_ADMIN", "ADMIN_ACCESS"})
    @GET
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public PrivacyPreferencessConverter get(@QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("max") @DefaultValue("10") int max,
            @QueryParam("expandLevel") @DefaultValue("1") int expandLevel,
            @QueryParam("query") @DefaultValue("SELECT e FROM PrivacyPreferences e") String query) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            return new PrivacyPreferencessConverter(getEntities(start, max,
                    query), uriInfo.getAbsolutePath(), expandLevel);
        } finally {
            persistenceSvc.commitTx();
            persistenceSvc.close();
        }
    }

    /**
     * Post method for creating an instance of PrivacyPreferences using XML as the input format.
     *
     * @param data an PrivacyPreferencesConverter entity that is deserialized from an XML stream
     * @return an instance of PrivacyPreferencesConverter
     */
    @POST
    @Consumes({"application/xml", "application/json"})
    public Response post(PrivacyPreferencesConverter data) {
        PersistenceService persistenceSvc = PersistenceService.getInstance();
        try {
            persistenceSvc.beginTx();
            EntityManager em = persistenceSvc.getEntityManager();
            PrivacyPreferences entity = data.resolveEntity(em);
            createEntity(data.resolveEntity(em));
            persistenceSvc.commitTx();
            return Response.created(uriInfo.getAbsolutePath().resolve(entity.getId() + "/")).build();
        } finally {
            persistenceSvc.close();
        }
    }

    /**
     * Returns a dynamic instance of PrivacyPreferencesResource used for entity navigation.
     *
     * @return an instance of PrivacyPreferencesResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("{id}/")
    public PrivacyPreferencesResource getPrivacyPreferencesResource(@PathParam("id") Long id) {
        PrivacyPreferencesResource resource = resourceContext.getResource(PrivacyPreferencesResource.class);
        resource.setId(id);
        return resource;
    }

    /**
     * 
     * @param id
     * @return
     */
    public PrivacyPreferences getById(long id) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        return em.find(PrivacyPreferences.class, id);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of PrivacyPreferences instances
     */
    protected Collection<PrivacyPreferences> getEntities(int start, int max,
            String query) {
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        return em.createQuery(query).setFirstResult(start).setMaxResults(max).getResultList();
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(PrivacyPreferences entity) {
        entity.setId(null);
        EntityManager em = PersistenceService.getInstance().getEntityManager();
        em.persist(entity);
        UbikeUser owner = entity.getOwner();
        if (owner != null) {
            owner.setPreferences(entity);
        }
    }
}
