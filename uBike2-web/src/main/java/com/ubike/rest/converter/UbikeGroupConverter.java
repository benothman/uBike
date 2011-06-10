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

import com.ubike.model.UbikeGroup;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import com.ubike.model.MemberShip;
import java.util.List;

/**
 *
 * @author Benothman
 */

@XmlRootElement(name = "group")
public class UbikeGroupConverter {
    private UbikeGroup entity;
    private URI uri;
    private int expandLevel;
  
    /**
     * Creates a new instance of UbikeGroupConverter
     */
    public UbikeGroupConverter() {
        entity = new UbikeGroup();
    }

    /**
     * Creates a new instance of UbikeGroupConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public UbikeGroupConverter(UbikeGroup entity, URI uri, int expandLevel,
            boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getMembers();
    }

    /**
     * Creates a new instance of UbikeGroupConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public UbikeGroupConverter(UbikeGroup entity, URI uri, int expandLevel) {
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
     * Getter for name.
     *
     * @return value for name
     */
    @XmlElement
    public String getName() {
        return (expandLevel > 0) ? entity.getName() : null;
    }

    /**
     * Setter for name.
     *
     * @param value the value to set
     */
    public void setName(String value) {
        entity.setName(value);
    }

    /**
     * Getter for description.
     *
     * @return value for description
     */
    @XmlElement
    public String getDescription() {
        return (expandLevel > 0) ? entity.getDescription() : null;
    }

    /**
     * Setter for description.
     *
     * @param value the value to set
     */
    public void setDescription(String value) {
        entity.setDescription(value);
    }

    /**
     * Getter for members.
     *
     * @return value for members
     */
    @XmlElement
    public MemberShipsConverter getMembers() {
        if (expandLevel > 0) {
            if (entity.getMemberShips() != null) {
                return new MemberShipsConverter(entity.getMemberShips(),
                        uri.resolve("members/"), expandLevel - 1);
            }
        }
        return null;
    }

    /**
     * Setter for members.
     *
     * @param value the value to set
     */
    public void setMembers(MemberShipsConverter value) {
        entity.setMemberShips((value != null) ? value.getEntities() : null);
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
     * Returns the UbikeGroup entity.
     *
     * @return an entity
     */
    @XmlTransient
    public UbikeGroup getEntity() {
        if (entity.getId() == null) {
            UbikeGroupConverter converter = UriResolver.getInstance().resolve(UbikeGroupConverter.class,
                    uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved UbikeGroup entity.
     *
     * @return an resolved entity
     */
    public UbikeGroup resolveEntity(EntityManager em) {
        List<MemberShip> members = entity.getMemberShips();
        List<MemberShip> newmembers = new java.util.ArrayList<MemberShip>();
        for (MemberShip item : members) {
            newmembers.add(em.getReference(MemberShip.class, item.getId()));
        }
        entity.setMemberShips(newmembers);
        return entity;
    }
}
