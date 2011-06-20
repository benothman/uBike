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

import com.ubike.model.MemberShip;
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
import com.ubike.model.UbikeGroup;
import com.ubike.rest.converter.MemberShipsConverter;
import com.ubike.rest.converter.MemberShipConverter;
import com.ubike.services.MemberShipServiceLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.springframework.context.annotation.Scope;
import org.springframework.security.annotation.Secured;
import org.springframework.stereotype.Component;

/**
 * {@code MemberShipsResource}
 * <p>Utility class for dealing with persistence.</p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Path("/memberShips/")
@Component
@Scope("request")
public class MemberShipsResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    @EJB
    private MemberShipServiceLocal memberShipService;

    /** 
     * Creates a new instance of MemberShipsResource 
     */
    public MemberShipsResource() {
    }

    /**
     * Get method for retrieving a collection of MemberShip instance in XML format.
     *
     * @return an instance of MemberShipsConverter
     */
    @GET
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Produces({MediaType.TEXT_HTML})
    public Viewable getHTML(
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("max") @DefaultValue("100") int max,
            @QueryParam("expandLevel") @DefaultValue("1") int expandLevel) {

        List<MemberShip> entities = getEntities(start, max);
        BaseBean.setSessionAttribute("tmp_members", entities);

        return new Viewable("/memberShips.jsp", entities);
    }

    @GET
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public MemberShipsConverter getXML(
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("max") @DefaultValue("100") int max,
            @QueryParam("expandLevel") @DefaultValue("1") int expandLevel) {
        MemberShipsConverter converter = new MemberShipsConverter(getEntities(start, max),
                uriInfo.getAbsolutePath(), expandLevel);
        return converter;
    }

    /**
     * Post method for creating an instance of MemberShip using XML as the input format.
     *
     * @param data an MemberShipConverter entity that is deserialized from an XML stream
     * @return an instance of MemberShipConverter
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @POST
    @Consumes({MediaType.TEXT_HTML, "application/xml", "application/json"})
    public Response post(MemberShipConverter data) {
        MemberShip entity = data.resolveEntity(this.memberShipService.getEntityManager());
        createEntity(entity);
        return Response.created(uriInfo.getAbsolutePath().resolve(entity.getId() + "/")).build();
    }

    /**
     * 
     * @param id
     * @return
     */
    public MemberShip getById(long id) {
        return this.memberShipService.find(id);
    }

    /**
     * Returns a dynamic instance of MemberShipResource used for entity navigation.
     *
     * @return an instance of MemberShipResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("{id}/")
    public MemberShipResource getMemberShipResource(@PathParam("id") Long id) {
        MemberShipResource resource = resourceContext.getResource(MemberShipResource.class);
        resource.setId(id);
        return resource;
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @return a collection of MemberShip instances
     */
    protected List<MemberShip> getEntities(int start, int max) {
        return this.memberShipService.findRange(start, max);
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(MemberShip entity) {
        entity.setId(null);
        this.memberShipService.create(entity);
        UbikeUser member = entity.getMember();
        if (member != null) {
            member.getMemberShips().add(entity);
        }
        UbikeGroup group = entity.getGroup();
        if (group != null) {
            group.getMemberShips().add(entity);
        }
    }
}
