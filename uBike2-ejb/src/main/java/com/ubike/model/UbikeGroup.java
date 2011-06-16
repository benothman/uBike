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
package com.ubike.model;

import com.ubike.util.Role;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * {@code UbikeGroup}
 * <p/>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Entity
@Table(name = "UBIKE_GROUPS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"NAME"})
})
@NamedQueries({
    @NamedQuery(name = "Group.getAll", query = "SELECT o FROM UbikeGroup o ORDER BY o.name"),
    @NamedQuery(name = "Group.getByName", query = "SELECT o FROM UbikeGroup o WHERE o.name=:groupName")
})
public class UbikeGroup implements com.ubike.util.UbikeEntity {

    /**
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    public static final long serialVersionUID = 1L;
    @Column(name = "NAME", unique = true, length = 50, nullable = false)
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<MemberShip> memberShips = new ArrayList<MemberShip>();
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Statistic> statistics;

    /**
     * Create a new instance of {@code UbikeGroup}
     */
    public UbikeGroup() {
        super();
        this.memberShips = new ArrayList<MemberShip>();
    }

    /**
     * Create a new instance of {@code UbikeGroup}
     * 
     * @param name the group name
     * @param description the group description
     */
    public UbikeGroup(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    /**
     * Create a new instance of {@code UbikeGroup}
     * 
     * @param name the group name
     * @param description the group description
     * @param owner the group creator
     */
    public UbikeGroup(String name, String description, UbikeUser owner) {
        this.name = name;
        this.description = description;
        this.memberShips.add(new MemberShip(owner, this, Role.Creator));
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * @param description
     * @param price the price to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the members
     */
    @Override
    public List<MemberShip> getMemberShips() {
        return this.memberShips;
    }

    /**
     * @param members the members to set
     */
    @Override
    public void setMemberShips(List<MemberShip> members) {
        this.memberShips = members;
    }

    @Transient
    public int getListSize() {
        return this.memberShips == null ? 0 : this.memberShips.size();
    }

    @Transient
    public int getActiveMembers() {
        int count = 0;
        for (MemberShip m : this.getMemberShips()) {
            if (m.isActive()) {
                count++;
            }
        }
        return count;
    }

    @Transient
    public int getNonActiveMembers() {
        int count = 0;
        for (MemberShip m : this.getMemberShips()) {
            if (!m.isActive()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return this.name + ", " + this.description;
    }

    /**
     * @return the id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the statistics
     */
    @Override
    public List<Statistic> getStatistics() {
        return statistics;
    }

    /**
     * @param statistics the statistics to set
     */
    @Override
    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
    }
}
