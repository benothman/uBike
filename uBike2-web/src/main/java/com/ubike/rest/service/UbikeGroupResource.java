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
import com.ubike.model.UbikeGroup;
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
import com.ubike.faces.bean.BaseBean;
import javax.ws.rs.WebApplicationException;
import javax.persistence.NoResultException;
import com.ubike.model.MemberShip;
import java.util.Collection;
import com.ubike.rest.converter.UbikeGroupConverter;
import com.ubike.model.Statistic;
import com.ubike.services.GroupServiceLocal;
import com.ubike.util.StatisticManager;
import com.ubike.services.TripManagerLocal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.springframework.context.annotation.Scope;
import org.springframework.security.annotation.Secured;
import org.springframework.stereotype.Component;

/**
 * {@code UbikeGroupResource}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Component
@Scope("request")
public class UbikeGroupResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    protected Long id;
    @EJB
    private GroupServiceLocal groupService;

    /**
     * Creates a new instance of UbikeGroupResource
     */
    public UbikeGroupResource() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    @GET
    @Produces({MediaType.APPLICATION_XML})
    public UbikeGroupConverter getXML(@QueryParam("expandLevel") @DefaultValue("0") int expandLevel) {

        UbikeGroup entity = expandLevel > 0 ? getEntityWithMemberShips() : getEntity();

        return new UbikeGroupConverter(entity, uriInfo.getAbsolutePath(), expandLevel);
    }

    /**
     * Get method for retrieving an instance of UbikeGroup identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of UbikeGroupConverter
     */
    @GET
    @Produces({MediaType.TEXT_HTML})
    public Viewable getHTML(@QueryParam("expandLevel") @DefaultValue("1") int expandLevel) {

        UbikeGroup entity = getEntityWithMemberShips();
        BaseBean.setSessionAttribute("tmp_members", entity.getMemberShips());
        // Extracting entity statistics
        //extractStatistics(converter.getEntity());
        BaseBean.setSessionAttribute("client", entity);

        return new Viewable("/groupInfo.jsp", entity);
    }

    /**
     * Put method for updating an instance of UbikeGroup identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an UbikeGroupConverter entity that is deserialized from a XML stream
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @PUT
    @Consumes({"application/xml", "application/json"})
    public void put(UbikeGroupConverter data) {
        updateEntity(getEntity(), data.resolveEntity(this.groupService.getEntityManager()));
    }

    /**
     * Delete method for deleting an instance of UbikeGroup identified by id.
     *
     * @param id identifier for the entity
     */
    @DELETE
    public void delete() {
        deleteEntity(getEntity());
    }

    /**
     * Returns an instance of UbikeGroup identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of UbikeGroup
     */
    protected UbikeGroup getEntityWithMemberShips() {
        try {
            return this.groupService.findWithMemberShips(id);
        } catch (NoResultException ex) {
            throw new NotFoundException("Resource for " + uriInfo.getAbsolutePath() + " does not exist.",
                    uriInfo.getAbsolutePath());
        }
    }

    /**
     * Returns an instance of UbikeGroup identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of UbikeGroup
     */
    protected UbikeGroup getEntity() {
        try {
            return this.groupService.find(id);
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
    protected UbikeGroup updateEntity(UbikeGroup entity, UbikeGroup newEntity) {
        Collection<MemberShip> members = entity.getMemberShips();
        Collection<MemberShip> membersNew = newEntity.getMemberShips();
        entity = this.groupService.update(newEntity);
        for (MemberShip value : members) {
            if (!membersNew.contains(value)) {
                throw new WebApplicationException(new Throwable(
                        "Cannot remove items from members"));
            }
        }
        for (MemberShip value : membersNew) {
            if (!members.contains(value)) {
                UbikeGroup oldEntity = value.getGroup();
                value.setGroup(entity);
                if (oldEntity != null && !oldEntity.equals(entity)) {
                    oldEntity.getMemberShips().remove(value);
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
    protected void deleteEntity(UbikeGroup entity) {
        if (!entity.getMemberShips().isEmpty()) {
            throw new WebApplicationException(new Throwable(
                    "Cannot delete entity because members is not empty."));
        }
        this.groupService.remove(id);
    }

    /**
     * 
     * @param group
     */
    private void extractStatistics(UbikeGroup group) {

        TripManagerLocal tml = (TripManagerLocal) BaseBean.getSessionAttribute("tml");
        StatisticManager statMan = new StatisticManager(tml);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        Collection<Statistic> today = statMan.getDailyStat(group, date);
        Collection<Statistic> thisWeek = statMan.getWeeklyStat(group, date);
        Collection<Statistic> thisMonth = statMan.getMonthlyStat(group, date);
        Collection<Statistic> thisYear = statMan.getYearlyStat(group, date);
        Collection<Statistic> general = statMan.getGeneralStat(group);
        cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR) - 1);
        Collection<Statistic> lastWeek = statMan.getWeeklyStat(group, cal.getTime());
        cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR) + 1);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        Collection<Statistic> lastMonth = statMan.getMonthlyStat(group, cal.getTime());
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        Collection<Statistic> lastYear = statMan.getYearlyStat(group, cal.getTime());
        BaseBean.setSessionAttribute("today", today);
        BaseBean.setSessionAttribute("thisWeek", thisWeek);
        BaseBean.setSessionAttribute("lastWeek", lastWeek);
        BaseBean.setSessionAttribute("thisMonth", thisMonth);
        BaseBean.setSessionAttribute("lastMonth", lastMonth);
        BaseBean.setSessionAttribute("thisYear", thisYear);
        BaseBean.setSessionAttribute("lastYear", lastYear);
        BaseBean.setSessionAttribute("general", general);
    }

    /**
     * Returns a dynamic instance of MemberShipsResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of MemberShipsResource
     */
    @Path("members/")
    public MemberShipsResource getMembersResource() {
        MembersResourceSub resource = resourceContext.getResource(
                MembersResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    public static class MembersResourceSub extends MemberShipsResource {

        private UbikeGroup parent;

        public void setParent(UbikeGroup parent) {
            this.parent = parent;
        }

        @Override
        protected List<MemberShip> getEntities(int start, int max) {
            List<MemberShip> result = new java.util.ArrayList<MemberShip>();
            int index = 0;
            for (MemberShip e : parent.getMemberShips()) {
                if (index >= start && (index - start) < max) {
                    result.add(e);
                }
                index++;
            }
            return result;
        }
    }
}
