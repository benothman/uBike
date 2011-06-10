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

import com.ubike.model.Trip;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benothman
 */

@XmlRootElement(name = "trips")
public class TripsConverter {
    private List<Trip> entities;
    private List<TripConverter> items;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of TripsConverter */
    public TripsConverter() {
    }

    /**
     * Creates a new instance of TripsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public TripsConverter(List<Trip> entities, URI uri, int expandLevel) {
        this.entities = entities;
        this.uri = uri;
        this.expandLevel = expandLevel;
        getTrip();
    }

    /**
     * Returns a collection of TripConverter.
     *
     * @return a collection of TripConverter
     */
    @XmlElement
    public List<TripConverter> getTrip() {
        if (items == null) {
            items = new ArrayList<TripConverter>();
        }
        if (entities != null) {
            items.clear();
            for (Trip entity : entities) {
                items.add(new TripConverter(entity, uri, expandLevel, true));
            }
        }
        return items;
    }

    /**
     * Sets a collection of TripConverter.
     *
     * @param a collection of TripConverter to set
     */
    public void setTrip(List<TripConverter> items) {
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
     * Returns a collection Trip entities.
     *
     * @return a collection of Trip entities
     */
    @XmlTransient
    public List<Trip> getEntities() {
        entities = new ArrayList<Trip>();
        if (items != null) {
            for (TripConverter item : items) {
                entities.add(item.getEntity());
            }
        }
        return entities;
    }
}
