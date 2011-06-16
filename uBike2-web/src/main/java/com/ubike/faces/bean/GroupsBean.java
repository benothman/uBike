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

import com.ubike.model.UbikeGroup;
import com.ubike.services.GroupServiceLocal;
import com.ubike.services.MemberShipServiceLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.component.SortOrder;

/**
 * {@code GroupsBean}
 * <p/>
 *
 * Created on Jun 15, 2011 at 8:43:43 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "groupsBean")
@ViewScoped
public class GroupsBean {

    private List<UbikeGroup> groups;
    private SortOrder sortByName = SortOrder.unsorted;
    private SortOrder sortByActiveMembers = SortOrder.unsorted;
    private SortOrder sortByNonActiveMembers = SortOrder.unsorted;
    private SortOrder sortByTotalMembers = SortOrder.unsorted;
    @EJB
    private GroupServiceLocal groupService;
    @EJB
    private MemberShipServiceLocal memberShipService;

    /**
     * Create a new instance of {@code GroupsBean}
     */
    public GroupsBean() {
        super();
    }

    @PostConstruct
    protected void init() {
        this.groups = BaseBean.getSessionAttribute("tmp_groups") != null
                ? (List<UbikeGroup>) BaseBean.getSessionAttribute("tmp_groups") : new ArrayList<UbikeGroup>();
    }

    @PreDestroy
    protected void destroy() {
        this.groups = null;
    }

    /**
     * Sort elements by name
     */
    public void sortByName() {
        sortByActiveMembers = SortOrder.unsorted;
        sortByNonActiveMembers = SortOrder.unsorted;
        sortByTotalMembers = SortOrder.unsorted;
        setSortByName(sortByName.equals(SortOrder.ascending)
                ? SortOrder.descending : SortOrder.ascending);
    }

    /**
     * Sort elements by the number of active members
     */
    public void sortByActiveMembers() {
        sortByName = SortOrder.unsorted;
        sortByNonActiveMembers = SortOrder.unsorted;
        sortByTotalMembers = SortOrder.unsorted;
        setSortByActiveMembers(sortByActiveMembers.equals(SortOrder.ascending)
                ? SortOrder.descending : SortOrder.ascending);
    }

    /**
     * Sort elements by the number of non active members
     */
    public void sortByNonActiveMembers() {
        sortByName = SortOrder.unsorted;
        sortByActiveMembers = SortOrder.unsorted;
        sortByTotalMembers = SortOrder.unsorted;
        setSortByNonActiveMembers(sortByNonActiveMembers.equals(SortOrder.ascending)
                ? SortOrder.descending : SortOrder.ascending);
    }

    /**
     * sort elements by the total number of members
     */
    public void sortByTotalMembers() {
        sortByName = SortOrder.unsorted;
        sortByActiveMembers = SortOrder.unsorted;
        sortByNonActiveMembers = SortOrder.unsorted;
        setSortByTotalMembers(sortByTotalMembers.equals(SortOrder.ascending)
                ? SortOrder.descending : SortOrder.ascending);
    }

    /**
     * 
     * @param groupId
     * @return 
     */
    public int activeMembers(Long groupId) {
        return 0;
    }

    /**
     * 
     * @param groupId
     * @return 
     */
    public int nonActiveMembers(Long groupId) {
        return 0;
    }

    /**
     * 
     * @param groupId
     * @return 
     */
    public int countMembers(Long groupId) {
        return 0;
    }

    /**
     * @return the groups
     */
    public List<UbikeGroup> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<UbikeGroup> groups) {
        this.groups = groups;
    }

    /**
     * @return the sortByName
     */
    public SortOrder getSortByName() {
        return sortByName;
    }

    /**
     * @param sortByName the sortByName to set
     */
    public void setSortByName(SortOrder sortByName) {
        this.sortByName = sortByName;
    }

    /**
     * @return the sortByActiveMembers
     */
    public SortOrder getSortByActiveMembers() {
        return sortByActiveMembers;
    }

    /**
     * @param sortByActiveMembers the sortByActiveMembers to set
     */
    public void setSortByActiveMembers(SortOrder sortByActiveMembers) {
        this.sortByActiveMembers = sortByActiveMembers;
    }

    /**
     * @return the sortByNonActiveMembers
     */
    public SortOrder getSortByNonActiveMembers() {
        return sortByNonActiveMembers;
    }

    /**
     * @param sortByNonActiveMembers the sortByNonActiveMembers to set
     */
    public void setSortByNonActiveMembers(SortOrder sortByNonActiveMembers) {
        this.sortByNonActiveMembers = sortByNonActiveMembers;
    }

    /**
     * @return the sortByTotalMembers
     */
    public SortOrder getSortByTotalMembers() {
        return sortByTotalMembers;
    }

    /**
     * @param sortByTotalMembers the sortByTotalMembers to set
     */
    public void setSortByTotalMembers(SortOrder sortByTotalMembers) {
        this.sortByTotalMembers = sortByTotalMembers;
    }
}
