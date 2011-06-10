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
import com.ubike.rest.converter.AccountsConverter;
import com.ubike.rest.converter.AccountConverter;
import com.ubike.services.AccountServiceLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.springframework.security.annotation.Secured;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * {@code AccountsResource}
 * <p>Utility class for dealing with persistence.</p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Path("/accounts/")
@Component
@Scope("request")
public class AccountsResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    @EJB
    private AccountServiceLocal accountService;

    /** 
     * Creates a new instance of AccountsResource
     */
    public AccountsResource() {
    }

    /**
     * Get method for retrieving a collection of Account instance in XML format.
     *
     * @return an instance of AccountsConverter
     */
    @Secured({"ROLE_ADMIN", "ADMIN_ACCESS"})
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public AccountsConverter get(
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("max") @DefaultValue("10") int max,
            @QueryParam("expandLevel") @DefaultValue("1") int expandLevel,
            @QueryParam("query") @DefaultValue("SELECT e FROM Account e") String query) {

        return new AccountsConverter(getEntities(start, max, query),
                uriInfo.getAbsolutePath(), expandLevel);
    }

    /**
     * Post method for creating an instance of Account using XML as the input format.
     *
     * @param data an AccountConverter entity that is deserialized from an XML stream
     * @return an instance of AccountConverter
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @POST
    @Consumes({"application/xml", "application/json"})
    public Response post(AccountConverter data) {
        EntityManager em = accountService.getEntityManager();
        Account entity = data.resolveEntity(em);
        createEntity(data.resolveEntity(em));
        return Response.created(uriInfo.getAbsolutePath().resolve(entity.getId() + "/")).build();
    }

    /**
     * Returns a dynamic instance of AccountResource used for entity navigation.
     *
     * @return an instance of AccountResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("{id}/")
    public AccountResource getAccountResource(@PathParam("id") Long id) {
        AccountResource resource = resourceContext.getResource(AccountResource.class);
        resource.setId(id);
        return resource;
    }

    /**
     * 
     * @param id
     * @return
     */
    public Account getById(long id) {
        return accountService.find(id);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of Account instances
     */
    @Secured({"ROLE_ADMIN", "ADMIN_ACCESS"})
    protected List<Account> getEntities(int start, int max, String query) {
        return accountService.findRange(start, max);
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(Account entity) {
        entity.setId(null);
        accountService.create(entity);
        UbikeUser owner = entity.getOwner();
        if (owner != null) {
            owner.setAccount(entity);
        }
    }
}
