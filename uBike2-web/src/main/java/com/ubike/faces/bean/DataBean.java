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
package com.ubike.faces.bean;

import com.ubike.model.MemberShip;
import com.ubike.model.Trip;
import com.ubike.model.UbikeGroup;
import com.ubike.model.UbikeUser;
import com.ubike.services.MemberShipServiceLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * {@code DataBean}
 * <p>This class is used as managed bean for group creation</p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "dataBean")
@RequestScoped
public class DataBean {

    private static final Logger logger = Logger.getLogger(DataBean.class.getName());
    private List<UbikeUser> users;
    private List<UbikeGroup> groups;
    private List<Trip> trips;
    private List<MemberShip> members;
    @EJB
    private MemberShipServiceLocal memberShipService;

    /**
     * Create a new instance of {@code DataBean}
     */
    public DataBean() {
        super();
    }

    @PostConstruct
    protected void init() {
        this.users = BaseBean.getSessionAttribute("tmp_users") != null
                ? (List<UbikeUser>) BaseBean.getSessionAttribute("tmp_users") : new ArrayList<UbikeUser>();

        this.members = BaseBean.getSessionAttribute("tmp_members") != null
                ? (List<MemberShip>) BaseBean.getSessionAttribute("tmp_members") : new ArrayList<MemberShip>();

        this.groups = BaseBean.getSessionAttribute("tmp_groups") != null
                ? (List<UbikeGroup>) BaseBean.getSessionAttribute("tmp_groups") : new ArrayList<UbikeGroup>();

        this.trips = BaseBean.getSessionAttribute("tmp_trips") != null
                ? (List<Trip>) BaseBean.getSessionAttribute("tmp_trips") : new ArrayList<Trip>();
    }

    @PreDestroy
    protected void destroy() {
        logger.log(Level.INFO, "Destroy managed bean {0}", getClass().getName());
    }

    public long countGroupMembers(Long groupId) {
        return this.memberShipService.countActiveMembers(groupId);
    }

    /**
     * @return the users
     */
    public List<UbikeUser> getUsers() {
        return this.users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<UbikeUser> users) {
        this.users = users;
    }

    /**
     * @return the groups
     */
    public List<UbikeGroup> getGroups() {
        return this.groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<UbikeGroup> groups) {
        this.groups = groups;
    }

    /**
     * @return the trips
     */
    public List<Trip> getTrips() {
        return this.trips;
    }

    /**
     * @param trips the trips to set
     */
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    /**
     * @return the members
     */
    public List<MemberShip> getMembers() {
        return this.members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List<MemberShip> members) {
        this.members = members;
    }
}
