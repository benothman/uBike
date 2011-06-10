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
import com.ubike.model.MemberShip;
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
import com.ubike.model.UbikeUser;
import com.ubike.model.UbikeGroup;
import com.ubike.rest.converter.MemberShipConverter;
import com.ubike.services.MemberShipServiceLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.springframework.context.annotation.Scope;
import org.springframework.security.annotation.Secured;
import org.springframework.stereotype.Component;

/**
 * {@code MemberShipResource}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Component
@Scope("request")
public class MemberShipResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    protected Long id;
    @EJB
    private MemberShipServiceLocal memberShipService;

    /** 
     * Creates a new instance of MemberShipResource 
     */
    public MemberShipResource() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get method for retrieving an instance of MemberShip identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of MemberShipConverter
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @GET
    @Produces({MediaType.TEXT_HTML, "application/xml", "application/json"})
    public MemberShipConverter get(@QueryParam("expandLevel") @DefaultValue("1") int expandLevel) {

        return new MemberShipConverter(getEntity(),
                uriInfo.getAbsolutePath(), expandLevel);
    }

    /**
     * Put method for updating an instance of MemberShip identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an MemberShipConverter entity that is deserialized from a XML stream
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @PUT
    @Consumes({MediaType.TEXT_HTML, "application/xml", "application/json"})
    public void put(MemberShipConverter data) {
        updateEntity(getEntity(), data.resolveEntity(this.memberShipService.getEntityManager()));
    }

    /**
     * Delete method for deleting an instance of MemberShip identified by id.
     *
     * @param id identifier for the entity
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @DELETE
    public void delete() {
        deleteEntity(getEntity());
    }

    /**
     * Returns an instance of MemberShip identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of MemberShip
     */
    public MemberShip getEntity() {
        try {
            return this.memberShipService.find(id);
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
    protected MemberShip updateEntity(MemberShip entity, MemberShip newEntity) {
        UbikeUser member = entity.getMember();
        UbikeUser memberNew = newEntity.getMember();
        UbikeGroup group = entity.getGroup();
        UbikeGroup groupNew = newEntity.getGroup();
        entity = this.memberShipService.update(entity);
        if (member != null && !member.equals(memberNew)) {
            member.getMemberShips().remove(entity);
        }
        if (memberNew != null && !memberNew.equals(member)) {
            memberNew.getMemberShips().add(entity);
        }
        if (group != null && !group.equals(groupNew)) {
            group.getMemberShips().remove(entity);
        }
        if (groupNew != null && !groupNew.equals(group)) {
            groupNew.getMemberShips().add(entity);
        }
        return entity;
    }

    /**
     * Deletes the entity.
     *
     * @param entity the entity to delete
     */
    protected void deleteEntity(MemberShip entity) {
        UbikeUser member = entity.getMember();
        if (member != null) {
            member.getMemberShips().remove(entity);
        }
        UbikeGroup group = entity.getGroup();
        if (group != null) {
            group.getMemberShips().remove(entity);
        }
        this.memberShipService.remove(entity.getId());
    }

    /**
     * Returns a dynamic instance of UbikeUserResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of UbikeUserResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("member/")
    public UbikeUserResource getMemberResource() {
        MemberResourceSub resource = resourceContext.getResource(MemberResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    /**
     * Returns a dynamic instance of UbikeGroupResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of UbikeGroupResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("group/")
    public UbikeGroupResource getGroupResource() {
        GroupResourceSub resource = resourceContext.getResource(GroupResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    public static class MemberResourceSub extends UbikeUserResource {

        private MemberShip parent;

        public void setParent(MemberShip parent) {
            this.parent = parent;
        }

        @Override
        public UbikeUser getEntity() {
            UbikeUser entity = parent.getMember();
            if (entity == null) {
                throw new WebApplicationException(new Throwable("Resource for " + uriInfo.getAbsolutePath() + " does not exist."),
                        404);
            }
            return entity;
        }
    }

    public static class GroupResourceSub extends UbikeGroupResource {

        private MemberShip parent;

        public void setParent(MemberShip parent) {
            this.parent = parent;
        }

        @Override
        protected UbikeGroup getEntity() {
            UbikeGroup entity = parent.getGroup();
            if (entity == null) {
                throw new WebApplicationException(new Throwable("Resource for " + uriInfo.getAbsolutePath() + " does not exist."),
                        404);
            }
            return entity;
        }
    }
}
