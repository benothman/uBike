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
import com.ubike.model.Account;
import com.ubike.model.Statistic;
import com.ubike.model.MemberShip;
import com.ubike.model.PrivacyPreferences;
import com.ubike.model.Trip;
import com.ubike.rest.converter.UbikeUserConverter;
import com.ubike.util.StatisticManager;
import com.ubike.services.TripManagerLocal;
import com.ubike.services.UserServiceLocal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MediaType;
import org.springframework.context.annotation.Scope;
import org.springframework.security.annotation.Secured;
import org.springframework.stereotype.Component;

/**
 * {@code UbikeUserResource}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
@Component
@Scope("request")
public class UbikeUserResource {

    @Context
    protected UriInfo uriInfo;
    @Context
    protected ResourceContext resourceContext;
    protected Long id;
    @EJB
    private UserServiceLocal userService;

    /**
     * Creates a new instance of UbikeUserResource
     */
    public UbikeUserResource() {
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get method for retrieving an instance of UbikeUser identified by id in XML format.
     *
     * @param id identifier for the entity
     * @return an instance of UbikeUserConverter
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @GET
    @Produces({MediaType.TEXT_HTML, MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Viewable get(@QueryParam("expandLevel") @DefaultValue("1") int expandLevel) {

        try {
            UbikeUserConverter converter = new UbikeUserConverter(getEntity(), uriInfo.getAbsolutePath(), expandLevel);
            UbikeUser user = converter.getEntity();

            user.getMemberShips().size();
            user.getTrips().size();
            BaseBean.setSessionAttribute("client", user);
            BaseBean.setSessionAttribute("tmp_trips", user.getTrips());
            BaseBean.setSessionAttribute("tmp_members", user.getMemberShips());
            // Extracting entity statistics
            extractStatistics(user);

            return new Viewable("/userInfo.jsp", user);
        } finally {
        }
    }

    /**
     * Put method for updating an instance of UbikeUser identified by id using XML as the input format.
     *
     * @param id identifier for the entity
     * @param data an UbikeUserConverter entity that is deserialized from a XML stream
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @PUT
    @Consumes({"application/xml", "application/json"})
    public void put(UbikeUserConverter data) {
        updateEntity(getEntity(), data.resolveEntity(this.userService.getEntityManager()));
    }

    /**
     * Delete method for deleting an instance of UbikeUser identified by id.
     *
     * @param id identifier for the entity
     */
    @Secured({"ROLE_ADMIN", "ADMIN_ACCESS"})
    @DELETE
    public void delete() {
        deleteEntity(getEntity());
    }

    /**
     * Returns an instance of UbikeUser identified by id.
     *
     * @param id identifier for the entity
     * @return an instance of UbikeUser
     */
    public UbikeUser getEntity() {
        try {
            return this.userService.find(this.id);
        } catch (NoResultException ex) {
            throw new WebApplicationException(new Throwable("Resource for " + uriInfo.getAbsolutePath() + " does not exist."), 404);
        }
    }

    /**
     * Updates entity using data from newEntity.
     *
     * @param entity the entity to update
     * @param newEntity the entity containing the new data
     * @return the updated entity
     */
    protected UbikeUser updateEntity(UbikeUser entity, UbikeUser newEntity) {
        Collection<Trip> trips = entity.getTrips();
        Collection<Trip> tripsNew = newEntity.getTrips();
        Account account = entity.getAccount();
        Account accountNew = newEntity.getAccount();
        List<MemberShip> memberShips = entity.getMemberShips();
        List<MemberShip> memberShipsNew = newEntity.getMemberShips();
        PrivacyPreferences preferences = entity.getPreferences();
        PrivacyPreferences preferencesNew = newEntity.getPreferences();
        userService.update(newEntity);
        for (Trip value : trips) {
            if (!tripsNew.contains(value)) {
                throw new WebApplicationException(new Throwable(
                        "Cannot remove items from trips"));
            }
        }
        for (Trip value : tripsNew) {
            if (!trips.contains(value)) {
                UbikeUser oldEntity = value.getOwner();
                value.setOwner(entity);
                if (oldEntity != null && !oldEntity.equals(entity)) {
                    oldEntity.getTrips().remove(value);
                }
            }
        }
        if (account != null && !account.equals(accountNew)) {
            account.setOwner(null);
        }
        if (accountNew != null && !accountNew.equals(account)) {
            accountNew.setOwner(entity);
        }
        for (MemberShip value : memberShips) {
            if (!memberShipsNew.contains(value)) {
                throw new WebApplicationException(new Throwable(
                        "Cannot remove items from memberShips"));
            }
        }
        for (MemberShip value : memberShipsNew) {
            if (!memberShips.contains(value)) {
                UbikeUser oldEntity = value.getMember();
                value.setMember(entity);
                if (oldEntity != null && !oldEntity.equals(entity)) {
                    oldEntity.getMemberShips().remove(value);
                }
            }
        }
        if (preferences != null && !preferences.equals(preferencesNew)) {
            preferences.setOwner(null);
        }
        if (preferencesNew != null && !preferencesNew.equals(preferences)) {
            preferencesNew.setOwner(entity);
        }
        return entity;
    }

    /**
     * Deletes the entity.
     *
     * @param entity the entity to delete
     */
    protected void deleteEntity(UbikeUser entity) {
        if (!entity.getTrips().isEmpty()) {
            throw new WebApplicationException(new Throwable(
                    "Cannot delete entity because trips is not empty."));
        }
        Account account = entity.getAccount();
        if (account != null) {
            account.setOwner(null);
        }
        if (!entity.getMemberShips().isEmpty()) {
            throw new WebApplicationException(new Throwable(
                    "Cannot delete entity because memberShips is not empty."));
        }
        PrivacyPreferences preferences = entity.getPreferences();
        if (preferences != null) {
            preferences.setOwner(null);
        }
        this.userService.remove(entity.getId());
    }

    /**
     * Returns a dynamic instance of TripsResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of TripsResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("trips/")
    public TripsResource getTripsResource() {
        TripsResourceSub resource = resourceContext.getResource(
                TripsResourceSub.class);
        
        resource.setParent(getEntity());
        return resource;
    }

    /**
     * Returns a dynamic instance of AccountResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of AccountResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("account/")
    public AccountResource getAccountResource() {
        AccountResourceSub resource = resourceContext.getResource(
                AccountResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    /**
     * Returns a dynamic instance of MemberShipsResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of MemberShipsResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("memberShips/")
    public MemberShipsResource getMemberShipsResource() {
        MemberShipsResourceSub resource = resourceContext.getResource(
                MemberShipsResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    /**
     * @return The list of the all members of all user groups
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @GET
    @Path("friends/")
    public Viewable getFriends() {
        UbikeUser entity = getEntity();
        entity.getMemberShips().size();
        BaseBean.setSessionAttribute("tmp_users", extractFriends(entity));
        return new Viewable("/friends.jsp", entity);
    }

    /**
     * 
     * @param entity
     * @return
     */
    private List<UbikeUser> extractFriends(UbikeUser entity) {

        // We use a HashSet to garantee that an element is not added more than one time
        List<UbikeUser> friends = new ArrayList<UbikeUser>();

        for (MemberShip o : entity.getMemberShips()) {
            for (MemberShip m : o.getGroup().getMemberShips()) {
                if (!m.getMember().getId().equals(entity.getId()) && !friends.contains(m.getMember())) {
                    friends.add(m.getMember());
                }
            }
        }

        friends.remove(entity);

        return friends;
    }

    /**
     * Extract alla the statistic related to the given user
     */
    private void extractStatistics(UbikeUser user) {

        TripManagerLocal tml = (TripManagerLocal) BaseBean.getSessionAttribute("tml");
        StatisticManager statMan = new StatisticManager(tml);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        Collection<Statistic> today = statMan.getDailyStat(user, date);
        Collection<Statistic> thisWeek = statMan.getWeeklyStat(user, date);
        Collection<Statistic> thisMonth = statMan.getMonthlyStat(user, date);
        Collection<Statistic> thisYear = statMan.getYearlyStat(user, date);
        Collection<Statistic> general = statMan.getGeneralStat(user);
        cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR) - 1);
        Collection<Statistic> lastWeek = statMan.getWeeklyStat(user, cal.getTime());
        cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR) + 1);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        Collection<Statistic> lastMonth = statMan.getMonthlyStat(user, cal.getTime());
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
        Collection<Statistic> lastYear = statMan.getYearlyStat(user, cal.getTime());
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
     * Returns a dynamic instance of PrivacyPreferencesResource used for entity navigation.
     *
     * @param id identifier for the parent entity
     * @return an instance of PrivacyPreferencesResource
     */
    @Secured({"ROLE_USER", "USER_ACCESS"})
    @Path("preferences/")
    public PrivacyPreferencesResource getPreferencesResource() {
        PreferencesResourceSub resource = resourceContext.getResource(
                PreferencesResourceSub.class);
        resource.setParent(getEntity());
        return resource;
    }

    public static class TripsResourceSub extends TripsResource {

        private UbikeUser parent;
 
        public void setParent(UbikeUser parent) {
            this.parent = parent;
        }

        @Override
        protected List<Trip> getEntities(int start, int max) {
            List<Trip> result = new java.util.ArrayList<Trip>();
            int index = 0;
            parent.getTrips().size();
            for (Trip e : parent.getTrips()) {
                if (index >= start && (index - start) < max) {
                    result.add(e);
                }
                index++;
            }
            return result;
        }
    }

    public static class AccountResourceSub extends AccountResource {

        private UbikeUser parent;

        public void setParent(UbikeUser parent) {
            this.parent = parent;
        }

        @Override
        public Account getEntity() {
            Account entity = parent.getAccount();
            if (entity == null) {
                throw new WebApplicationException(
                        new Throwable(
                        "Resource for " + uriInfo.getAbsolutePath() + " does not exist."),
                        404);
            }
            return entity;
        }
    }

    public static class MemberShipsResourceSub extends MemberShipsResource {

        private UbikeUser parent;

        public void setParent(UbikeUser parent) {
            this.parent = parent;
        }

        @Override
        protected List<MemberShip> getEntities(int start, int max,
                String query) {
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

    public static class PreferencesResourceSub extends PrivacyPreferencesResource {

        private UbikeUser parent;

        public void setParent(UbikeUser parent) {
            this.parent = parent;
        }

        @Override
        public PrivacyPreferences getEntity() {
            PrivacyPreferences entity = parent.getPreferences();
            if (entity == null) {
                throw new WebApplicationException(
                        new Throwable(
                        "Resource for " + uriInfo.getAbsolutePath() + " does not exist."),
                        404);
            }
            return entity;
        }
    }
}
