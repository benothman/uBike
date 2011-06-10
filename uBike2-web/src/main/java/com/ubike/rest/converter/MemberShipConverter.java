/*
 *  Copyright 2009 Nabil BENOTHMAN <nabil.benothman@gmail.com>.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 *
 *  This class is a part of uBike projet (HEIG-VD)
 */

package com.ubike.rest.converter;

import com.ubike.model.MemberShip;
import com.ubike.util.Role;
import java.net.URI;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import com.ubike.model.UbikeUser;
import com.ubike.model.UbikeGroup;

/**
 *
 * @author Benothman
 */

@XmlRootElement(name = "memberShip")
public class MemberShipConverter {
    private MemberShip entity;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of MemberShipConverter */
    public MemberShipConverter() {
        entity = new MemberShip();
    }

    /**
     * Creates a new instance of MemberShipConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public MemberShipConverter(MemberShip entity, URI uri, int expandLevel,
            boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getMember();
        getGroup();
    }

    /**
     * Creates a new instance of MemberShipConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public MemberShipConverter(MemberShip entity, URI uri, int expandLevel) {
        this(entity, uri, expandLevel, false);
    }

    /**
     * Getter for id.
     *
     * @return value for id
     */
    @XmlElement
    public Long getId() {
        return (expandLevel > 0) ? entity.getId() : null;
    }

    /**
     * Setter for id.
     *
     * @param value the value to set
     */
    public void setId(Long value) {
        entity.setId(value);
    }

    /**
     * Getter for role.
     *
     * @return value for role
     */
    @XmlElement
    public Role getRole() {
        return (expandLevel > 0) ? entity.getRole() : null;
    }

    /**
     * Setter for role.
     *
     * @param value the value to set
     */
    public void setRole(Role value) {
        entity.setRole(value);
    }

    /**
     * Getter for date.
     *
     * @return value for date
     */
    @XmlElement
    public Date getDate() {
        return (expandLevel > 0) ? entity.getDate() : null;
    }

    /**
     * Setter for date.
     *
     * @param value the value to set
     */
    public void setDate(Date value) {
        entity.setDate(value);
    }

    /**
     * Getter for actif.
     *
     * @return value for actif
     */
    @XmlElement
    public Boolean getActive() {
        return (expandLevel > 0) ? entity.isActive() : null;
    }

    /**
     * Setter for actif.
     *
     * @param value the value to set
     */
    public void setActive(Boolean value) {
        entity.setActive(value);
    }

    /**
     * Getter for member.
     *
     * @return value for member
     */
    @XmlElement
    public UbikeUserConverter getMember() {
        if (expandLevel > 0) {
            if (entity.getMember() != null) {
                return new UbikeUserConverter(entity.getMember(),
                        uri.resolve("member/"), expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for member.
     *
     * @param value the value to set
     */
    public void setMember(UbikeUserConverter value) {
        entity.setMember((value != null) ? value.getEntity() : null);
    }

    /**
     * Getter for group.
     *
     * @return value for group
     */
    @XmlElement
    public UbikeGroupConverter getGroup() {
        if (expandLevel > 0) {
            if (entity.getGroup() != null) {
                return new UbikeGroupConverter(entity.getGroup(),
                        uri.resolve("group/"), expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for group.
     *
     * @param value the value to set
     */
    public void setGroup(UbikeGroupConverter value) {
        entity.setGroup((value != null) ? value.getEntity() : null);
    }

    /**
     * Returns the URI associated with this converter.
     *
     * @return the uri
     */
    @XmlAttribute
    public URI getUri() {
        return uri;
    }

    /**
     * Sets the URI for this reference converter.
     *
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Returns the MemberShip entity.
     *
     * @return an entity
     */
    @XmlTransient
    public MemberShip getEntity() {
        if (entity.getId() == null) {
            MemberShipConverter converter = UriResolver.getInstance().resolve(MemberShipConverter.class,
                    uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved MemberShip entity.
     *
     * @return an resolved entity
     */
    public MemberShip resolveEntity(EntityManager em) {
        UbikeUser member = entity.getMember();
        if (member != null) {
            entity.setMember(em.getReference(UbikeUser.class, member.getId()));
        }
        UbikeGroup group = entity.getGroup();
        if (group != null) {
            entity.setGroup(em.getReference(UbikeGroup.class, group.getId()));
        }
        return entity;
    }
}
