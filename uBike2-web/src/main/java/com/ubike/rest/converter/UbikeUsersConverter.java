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

import com.ubike.model.UbikeUser;
import java.net.URI;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;

/**
 *
 * @author Benothman
 */
@XmlRootElement(name = "ubike-users")
public class UbikeUsersConverter {

    private Collection<UbikeUser> entities;
    private Collection<UbikeUserConverter> items;
    private URI uri;
    private int expandLevel;

    /**
     * Creates a new instance of UbikeUsersConverter 
     */
    public UbikeUsersConverter() {
    }

    /**
     * Creates a new instance of UbikeUsersConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public UbikeUsersConverter(Collection<UbikeUser> entities, URI uri,
            int expandLevel) {
        this.entities = entities;
        this.uri = uri;
        this.expandLevel = expandLevel;
        getUbikeUser();
    }

    /**
     * Returns a collection of UbikeUserConverter.
     *
     * @return a collection of UbikeUserConverter
     */
    @XmlElement
    public Collection<UbikeUserConverter> getUbikeUser() {
        if (items == null) {
            items = new ArrayList<UbikeUserConverter>();
        }
        if (entities != null) {
            items.clear();
            for (UbikeUser entity : entities) {
                items.add(new UbikeUserConverter(entity, uri, expandLevel, true));
            }
        }
        return items;
    }

    /**
     * Sets a collection of UbikeUserConverter.
     *
     * @param a collection of UbikeUserConverter to set
     */
    public void setUbikeUser(Collection<UbikeUserConverter> items) {
        this.items = items;
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
     * Returns a collection UbikeUser entities.
     *
     * @return a collection of UbikeUser entities
     */
    @XmlTransient
    public Collection<UbikeUser> getEntities() {
        entities = new ArrayList<UbikeUser>();
        if (items != null) {
            for (UbikeUserConverter item : items) {
                entities.add(item.getEntity());
            }
        }
        return entities;
    }
}
