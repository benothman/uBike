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

import java.io.Serializable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * {@code PrivacyPreferences}
 * <p>
 * This class represent the global private preferences of the user. In this
 * class we use the Unix permissions for the user, group and other members witch
 * defined as below :
 *   false -> no access
 *   true  -> read only
 * </p>
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Entity
@Table(name = "PRIVACY_PREFERENCES")
@NamedQueries({
    @NamedQuery(name = "preferences.getAll",
    query = "SELECT o FROM PrivacyPreferences o"),
    @NamedQuery(name = "user.getByOwnerId",
    query = "SELECT o FROM PrivacyPreferences o WHERE o.owner.id=:ownerId")
})
public class PrivacyPreferences implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "GROUPS_ACCESS")
    private boolean groupAccess;
    @Column(name = "OTHERS_ACCESS")
    private boolean othersAccess;
    @OneToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    private UbikeUser owner;

    /**
     * Create a new instance of the <code>PrivacyPreferences</code> with no
     * parameter setting.
     */
    public PrivacyPreferences() {
        super();
    }

    /**
     * Create a new instance of the <code>PrivacyPreferences</code> and initialize
     * the owner and general access for the groups members and other users.
     *
     * @param owner The owner of this private preferences
     * @param groupAccess The group access
     * @param othersAccess The other users access
     */
    public PrivacyPreferences(UbikeUser owner, boolean groupAccess, boolean othersAccess) {
        this.owner = owner;
        this.groupAccess = groupAccess;
        this.othersAccess = othersAccess;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the owner of this privacy preferences.
     */
    public UbikeUser getOwner() {
        return this.owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(UbikeUser owner) {
        this.owner = owner;
    }

    /**
     * @return the groupAccess
     */
    public boolean isGroupAccess() {
        return this.groupAccess;
    }

    /**
     * @param groupAccess the groupAccess to set
     */
    public void setGroupAccess(boolean groupAccess) {
        this.groupAccess = groupAccess;
    }

    /**
     * @return the othersAccess
     */
    public boolean isOthersAccess() {
        return this.othersAccess;
    }

    /**
     * @param othersAccess the othersAccess to set
     */
    public void setOthersAccess(boolean othersAccess) {
        this.othersAccess = othersAccess;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash = hash * 19 + (id != null ? id.hashCode() : 0);
        hash = hash * 19 + (owner != null ? owner.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PrivacyPreferences)) {
            return false;
        }
        PrivacyPreferences other = (PrivacyPreferences) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(
                other.id))) {
            return false;
        }
        if ((this.owner == null && other.owner != null) || (this.owner != null && !this.owner.equals(
                other.owner))) {
            return false;
        }

        return this.groupAccess == other.groupAccess && this.othersAccess == other.othersAccess;
    }

    @Override
    public String toString() {
        return "com.ubike.model.PrivacyPreferences[id=" + id + "]";
    }
}
