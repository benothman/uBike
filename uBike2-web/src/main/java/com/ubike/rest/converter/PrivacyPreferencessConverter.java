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

@XmlRootElement(name = "privacyPreferencess")
public class PrivacyPreferencessConverter {
    private Collection<PrivacyPreferences> entities;
    private Collection<PrivacyPreferencesConverter> items;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of PrivacyPreferencessConverter */
    public PrivacyPreferencessConverter() {
    }

    /**
     * Creates a new instance of PrivacyPreferencessConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public PrivacyPreferencessConverter(Collection<PrivacyPreferences> entities,
            URI uri, int expandLevel) {
        this.entities = entities;
        this.uri = uri;
        this.expandLevel = expandLevel;
        getPrivacyPreferences();
    }

    /**
     * Returns a collection of PrivacyPreferencesConverter.
     *
     * @return a collection of PrivacyPreferencesConverter
     */
    @XmlElement
    public Collection<PrivacyPreferencesConverter> getPrivacyPreferences() {
        if (items == null) {
            items = new ArrayList<PrivacyPreferencesConverter>();
        }
        if (entities != null) {
            items.clear();
            for (PrivacyPreferences entity : entities) {
                items.add(new PrivacyPreferencesConverter(entity, uri,
                        expandLevel, true));
            }
        }
        return items;
    }

    /**
     * Sets a collection of PrivacyPreferencesConverter.
     *
     * @param a collection of PrivacyPreferencesConverter to set
     */
    public void setPrivacyPreferences(Collection<PrivacyPreferencesConverter> items) {
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
     * Returns a collection PrivacyPreferences entities.
     *
     * @return a collection of PrivacyPreferences entities
     */
    @XmlTransient
    public Collection<PrivacyPreferences> getEntities() {
        entities = new ArrayList<PrivacyPreferences>();
        if (items != null) {
            for (PrivacyPreferencesConverter item : items) {
                entities.add(item.getEntity());
            }
        }
        return entities;
    }
}
