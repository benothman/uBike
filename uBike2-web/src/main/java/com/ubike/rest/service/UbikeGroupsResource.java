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

import com.ubike.model.UbikeGroup;
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
import com.ubike.model.MemberShip;
import com.ubike.rest.converter.UbikeGroupConverter;
import com.ubike.rest.converter.UbikeGroupsConverter;
import com.ubike.services.GroupServiceLocal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * {@code UbikeGroupsResource}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Path("/groups/")
@Component
@Scope("request")
public class UbikeGroupsResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    @EJB
    private GroupServiceLocal groupService;

    /** 
     * Creates a new instance of {@code UbikeGroupsResource} 
     */
    public UbikeGroupsResource() {
    }

    /**
     * Get method for retrieving a collection of UbikeGroup instance in HTML format.
     *
     * @return an instance of Viewable
     */
    @GET
    @Produces({MediaType.TEXT_HTML})
    public Viewable getHTML(
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("max") @DefaultValue("10") int max,
            @QueryParam("expandLevel") @DefaultValue("1") int expandLevel) {

        List<UbikeGroup> entities = getEntities(start, max);
        BaseBean.setSessionAttribute("tmp_groups", entities);

        return new Viewable("/groupsInfo.jsp", entities);
    }

    /**
     * Get method for retrieving a collection of UbikeGroup instance in XML format.
     *
     * @return an instance of UbikeGroupsConverter
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public UbikeGroupsConverter getXML(
            @QueryParam("start") @DefaultValue("0") int start,
            @QueryParam("max") @DefaultValue("10") int max,
            @QueryParam("expandLevel") @DefaultValue("1") int expandLevel) {

        return new UbikeGroupsConverter(getEntities(start, max),
                uriInfo.getAbsolutePath(), expandLevel);
    }

    /**
     * Post method for creating an instance of UbikeGroup using XML as the input format.
     *
     * @param data an UbikeGroupConverter entity that is deserialized from an XML stream
     * @return an instance of UbikeGroupConverter
     */
    @POST
    @Consumes({"application/xml", "application/json"})
    public Response post(UbikeGroupConverter data) {
        UbikeGroup entity = data.resolveEntity(groupService.getEntityManager());
        createEntity(entity);
        return Response.created(uriInfo.getAbsolutePath().resolve(
                entity.getId() + "/")).build();
    }

    /**
     * Returns a dynamic instance of UbikeGroupResource used for entity navigation.
     *
     * @return an instance of UbikeGroupResource
     */
    @Path("{id}/")
    public UbikeGroupResource getGroupeResource(@PathParam("id") Long id) {
        UbikeGroupResource resource = resourceContext.getResource(
                UbikeGroupResource.class);
        resource.setId(id);
        return resource;
    }

    /**
     * Returns all the entities associated with this resource.
     *
     * @param start
     * @param max
     * @param query
     * @return a collection of UbikeGroup instances
     */
    protected List<UbikeGroup> getEntities(int start, int max) {
        return groupService.findRange(start, max);
    }

    /**
     * Persist the given entity.
     *
     * @param entity the entity to persist
     */
    protected void createEntity(UbikeGroup entity) {
        entity.setId(null);
        this.groupService.create(entity);
        for (MemberShip value : entity.getMemberShips()) {
            value.setGroup(entity);
        }
    }
}
