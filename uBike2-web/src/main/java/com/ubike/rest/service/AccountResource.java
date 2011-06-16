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

import com.ubike.model.Account;
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
import com.ubike.model.UbikeUser;
import com.ubike.rest.converter.AccountConverter;
import com.ubike.services.AccountServiceLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.core.MediaType;
import org.springframework.security.annotation.Secured;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * {@code AccountResource}
 * <p>Utility class for dealing with persistence.</p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Component
@Scope("request")
public class AccountResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    protected Long id;
    @EJB
    private AccountServiceLocal accountService;

    /**
     * Creates a new instance of AccountResource
     */
    public AccountResource() {
    }

    /**
     * 
     * @param id 
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get method for retrieving an instance of Account identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of AccountConverter
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AccountConverter get(@QueryParam("expandLevel") @DefaultValue("1") int expandLevel) {
        return new AccountConverter(getEntity(), uriInfo.getAbsolutePath(), expandLevel);
    }

    /**
     * Put method for updating an instance of Account identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an AccountConverter entity that is deserialized from a XML stream
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void put(AccountConverter data) {
        EntityManager em = accountService.getEntityManager();
        updateEntity(getEntity(), data.resolveEntity(em));
    }

    /**
     * Delete method for deleting an instance of Account identified by id.
     *
     * @param id identifier for the entity
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @DELETE
    public void delete() {
        deleteEntity(getEntity());
    }

    /**
     * Returns an instance of Account identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of Account
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    public Account getEntity() {
        try {
            return accountService.find(id);
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
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    protected Account updateEntity(Account entity, Account newEntity) {
        UbikeUser owner = entity.getOwner();
        UbikeUser ownerNew = newEntity.getOwner();
        accountService.update(newEntity);
        if (owner != null && !owner.equals(ownerNew)) {
            owner.setAccount(null);
        }
        if (ownerNew != null && !ownerNew.equals(owner)) {
            ownerNew.setAccount(entity);
        }
        return entity;
    }

    /**
     * Deletes the entity.
     *
     * @param entity the entity to delete
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    protected void deleteEntity(Account entity) {
        UbikeUser owner = entity.getOwner();
        if (owner != null) {
            owner.setAccount(null);
        }
        accountService.remove(id);
    }

    /**
     * Returns a dynamic instance of UbikeUserResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of UbikeUserResource
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @Path("owner/")
    public UbikeUserResource getOwnerResource() {
        OwnerResourceSub resource = resourceContext.getResource(OwnerResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    public static class OwnerResourceSub extends UbikeUserResource {

        private Account parent;

        public void setParent(Account parent) {
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
}
