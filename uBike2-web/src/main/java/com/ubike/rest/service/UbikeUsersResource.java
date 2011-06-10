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

import com.ubike.model.UbikeUser;
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
import com.ubike.model.Account;
import com.ubike.model.MemberShip;
import com.ubike.model.PrivacyPreferences;
import com.ubike.model.Trip;
import com.ubike.rest.converter.UbikeUserConverter;
import com.ubike.rest.converter.UbikeUsersConverter;
import com.ubike.services.UserServiceLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.springframework.security.annotation.Secured;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * {@code UbikeUsersResource}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Path("/users/")
@Component
@Scope("request")
public class UbikeUsersResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    @EJB
    private UserServiceLocal userService;

    /**
     * Creates a new instance of UbikeUsersResource
     */
    public UbikeUsersResource() {
    }

    /**
     * Get method for retrieving a collection of UbikeUser instance in XML format.
     *
     * @return an instance of UbikeUsersConverter
     */
    @Secured({"ROLE_ADMIN", "ADMIN_ACCESS"})
    @GET
    @Produces({MediaType.TEXT_HTML, "application/xml", "application/json"})
    public Viewable get(
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("max") @DefaultValue("100") int max,
            @QueryParam("expandLevel") @DefaultValue("1") int expandLevel,
            @QueryParam("query") @DefaultValue("SELECT e FROM UbikeUser e") String query) {

        try {
            UbikeUsersConverter converter = new UbikeUsersConverter(getEntities(start, max, query),
                    uriInfo.getAbsolutePath(), expandLevel);

            BaseBean.setSessionAttribute("tmp_users", converter.getEntities());
            return new Viewable("/usersInfo.jsp", converter.getEntities());
        } finally {
        }
    }

    /**
     * Post method for creating an instance of UbikeUser using XML as the input format.
     *
     * @param data an UbikeUserConverter entity that is deserialized from an XML stream
     * @return an instance of UbikeUserConverter
     */
    @POST
    @Consumes({"application/xml", "application/json"})
    public Response post(UbikeUserConverter data) {
        try {
            UbikeUser entity = data.resolveEntity(this.userService.getEntityManager());
            createEntity(entity);
            return Response.created(uriInfo.getAbsolutePath().resolve(
                    entity.getId() + "/")).build();
        } finally {
        }
    }

    /**
     * Returns a dynamic instance of UbikeUserResource used for entity navigation.
     *
     * @return an instance of UbikeUserResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("{id}/")
    @Produces(MediaType.TEXT_HTML)
    public UbikeUserResource getUbikeUserResource(@PathParam("id") Long id) {
        UbikeUserResource resource = resourceContext.getResource(
                UbikeUserResource.class);
        resource.setId(id);
        return resource;
    }

    /**
     * 
     * @param id
     * @return
     */
    public UbikeUser getById(Long id) {
        return this.userService.find(id);
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of UbikeUser instances
     */
    protected List<UbikeUser> getEntities(int start, int max, String query) {
        return userService.findRange(start, max);
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(UbikeUser entity) {
        entity.setId(null);
        this.userService.create(entity);

        for (Trip value : entity.getTrips()) {
            UbikeUser oldEntity = value.getOwner();
            value.setOwner(entity);
            if (oldEntity != null) {
                oldEntity.getTrips().remove(value);
            }
        }
        Account account = entity.getAccount();
        if (account != null) {
            account.setOwner(entity);
        }
        for (MemberShip value : entity.getMemberShips()) {
            UbikeUser oldEntity = value.getMember();
            value.setMember(entity);
            if (oldEntity != null) {
                oldEntity.getMemberShips().remove(value);
            }
        }
        PrivacyPreferences preferences = entity.getPreferences();
        if (preferences != null) {
            preferences.setOwner(entity);
        }
    }
}
