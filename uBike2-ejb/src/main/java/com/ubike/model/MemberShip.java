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
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author BENOTHMAN Nabil.
 */
@Entity
@Table(name = "MEMBERSHIPS")
@NamedQueries({
    @NamedQuery(name = "MemberShip.getAll", query = "SELECT o FROM MemberShip o ORDER BY o.group.id"),
    @NamedQuery(name = "MemberShip.getByGroup", query = "SELECT o FROM MemberShip o WHERE o.group.id=:groupId"),
    @NamedQuery(name = "MemberShip.getByUser", query = "SELECT o FROM MemberShip o WHERE o.member.id=:userId"),
    @NamedQuery(name = "MemberShip.getByGroupUser",
    query = "SELECT o FROM MemberShip o WHERE o.member.id=:userId AND o.group.id=:groupId")
})
public class MemberShip implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "MEMBER_ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "JOIN_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "ACTIVE")
    private boolean active;
    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_MEMBER", referencedColumnName = "ID")
    private UbikeUser member;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_GROUPE", referencedColumnName = "ID")
    private UbikeGroup group;

    /**
     *
     */
    public MemberShip() {
        this.active = true;
        this.date = new Date(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * @param member
     * @param group
     * @param role
     */
    public MemberShip(UbikeUser member, UbikeGroup group, Role role) {
        this();
        this.member = member;
        this.group = group;
        this.role = role;
        this.group.getMemberShips().add(this);
        this.member.getMemberShips().add(this);
    }

    /**
     * @return
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return The group
     */
    public UbikeGroup getGroup() {
        return this.group;
    }

    /**
     * @param group
     */
    public void setGroup(UbikeGroup group) {
        this.group = group;
    }

    /**
     * @return The member of the group
     */
    public UbikeUser getMember() {
        return this.member;
    }

    /**
     * @param member
     */
    public void setMember(UbikeUser member) {
        this.member = member;
    }

    /**
     * @return The role of the member in the group
     */
    public Role getRole() {
        return this.role;
    }

    /**
     * @param role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * @return Tha date at witch the member was joined the group
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return True if the member is actif in the group else False.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * @param actif the activity state of the member
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    @Transient
    public String getDateAsString() {
         return com.ubike.util.Util.formatDate(this.date);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        hash += (group != null ? group.hashCode() : 0);
        hash += (member != null ? member.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MemberShip)) {
            return false;
        }

        MemberShip other = (MemberShip) object;

        return this.id == other.id && this.group.equals(other.group) && this.member.equals(
                other.member);
    }

    @Override
    public String toString() {
        return "MemberShip[" + this.group.getName() + ", " + this.member.getAccount().getUsername() + "]";
    }
}
