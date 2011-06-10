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

import com.ubike.model.PrivacyPreferences;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import com.ubike.model.UbikeUser;

/**
 *
 * @author Benothman
 */
@XmlRootElement(name = "privacyPreferences")
public class PrivacyPreferencesConverter {

    private PrivacyPreferences entity;
    private URI uri;
    private int expandLevel;

    /** Creates a new instance of PrivacyPreferencesConverter */
    public PrivacyPreferencesConverter() {
        entity = new PrivacyPreferences();
    }

    /**
     * Creates a new instance of PrivacyPreferencesConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public PrivacyPreferencesConverter(PrivacyPreferences entity, URI uri,
            int expandLevel, boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(
                entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getOwner();
    }

    /**
     * Creates a new instance of PrivacyPreferencesConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public PrivacyPreferencesConverter(PrivacyPreferences entity, URI uri,
            int expandLevel) {
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
     * Getter for readAccess.
     *
     * @return value for group Access
     */
    @XmlElement
    public Boolean isGroupAccess() {
        return (expandLevel > 0) ? entity.isGroupAccess() : null;
    }

    /**
     * Setter for group Access.
     *
     * @param value the value to set
     */
    public void setGroupAccess(Boolean value) {
        entity.setGroupAccess(value);
    }

    /**
     * Getter for others Access.
     *
     * @return value for others Access
     */
    @XmlElement
    public Boolean isOthersAccess() {
        return (expandLevel > 0) ? entity.isOthersAccess() : null;
    }

    /**
     * Setter for readAccess.
     *
     * @param value the value to set
     */
    public void setOthersAccess(Boolean value) {
        entity.setOthersAccess(value);
    }

    /**
     * Getter for owner.
     *
     * @return value for owner
     */
    @XmlElement
    public UbikeUserConverter getOwner() {
        if (expandLevel > 0) {
            if (entity.getOwner() != null) {
                return new UbikeUserConverter(entity.getOwner(),
                        uri.resolve("owner/"), expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for owner.
     *
     * @param value the value to set
     */
    public void setOwner(UbikeUserConverter value) {
        entity.setOwner((value != null) ? value.getEntity() : null);
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
     * Returns the PrivacyPreferences entity.
     *
     * @return an entity
     */
    @XmlTransient
    public PrivacyPreferences getEntity() {
        if (entity.getId() == null) {
            PrivacyPreferencesConverter converter = UriResolver.getInstance().resolve(
                    PrivacyPreferencesConverter.class,
                    uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved PrivacyPreferences entity.
     *
     * @return an resolved entity
     */
    public PrivacyPreferences resolveEntity(EntityManager em) {
        UbikeUser owner = entity.getOwner();
        if (owner != null) {
            entity.setOwner(em.getReference(UbikeUser.class, owner.getId()));
        }
        return entity;
    }
}
