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

@XmlRootElement(name = "groupes")
public class UbikeGroupsConverter {
    private Collection<UbikeGroup> entities;
    private Collection<UbikeGroupConverter> items;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instanceUbikeGroupsConverterrter */
    public UbikeGroupsConverter() {
    }

    /**
     * Creates a new instanceUbikeGroupsConverterrter.
     *
     * @param entities associated entities
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public UbikeGroupsConverter(Collection<UbikeGroup> entities, URI uri,
            int expandLevel) {
        this.entities = entities;
        this.uri = uri;
        this.expandLevel = expandLevel;
        getGroupe();
    }

    /**
     * Returns a collection of UbikeGroupConverter.
     *
     * @return a collection of UbikeGroupConverter
     */
    @XmlElement
    public Collection<UbikeGroupConverter> getGroupe() {
        if (items == null) {
            items = new ArrayList<UbikeGroupConverter>();
        }
        if (entities != null) {
            items.clear();
            for (UbikeGroup entity : entities) {
                items.add(new UbikeGroupConverter(entity, uri, expandLevel, true));
            }
        }
        return items;
    }

    /**
     * Sets a collection of UbikeGroupConverter.
     *
     * @param a collection of UbikeGroupConverter to set
     */
    public void setGroupe(Collection<UbikeGroupConverter> items) {
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
     * Returns a collection UbikeGroup entities.
     *
     * @return a collection of UbikeGroup entities
     */
    @XmlTransient
    public Collection<UbikeGroup> getEntities() {
        entities = new ArrayList<UbikeGroup>();
        if (items != null) {
            for (UbikeGroupConverter item : items) {
                entities.add(item.getEntity());
            }
        }
        return entities;
    }
}
