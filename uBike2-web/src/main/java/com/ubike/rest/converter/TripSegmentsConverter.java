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

import com.ubike.model.TripSegment;
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
@XmlRootElement(name = "tripSegments")
public class TripSegmentsConverter {

    private Collection<TripSegment> entities;
    private Collection<TripSegmentConverter> items;
    private URI uri;
    private int expandLevel;

    /** 
     * Creates a new instance of TripSegmentsConverter 
     */
    public TripSegmentsConverter() {
    }

    /**
     * Creates a new instance of TripSegmentsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public TripSegmentsConverter(Collection<TripSegment> entities, URI uri,
            int expandLevel) {
        this.entities = entities;
        this.uri = uri;
        this.expandLevel = expandLevel;
        //getTripSegment();
    }

    /**
     * Returns a collection of TripSegmentConverter.
     *
     * @return a collection of TripSegmentConverter
     */
    @XmlElement
    public Collection<TripSegmentConverter> getTripSegment() {
        if (items == null) {
            items = new ArrayList<TripSegmentConverter>();
        }
        if (entities != null) {
            items.clear();
            for (TripSegment entity : entities) {
                items.add(new TripSegmentConverter(entity, uri, expandLevel,
                        true));
            }
        }
        return items;
    }

    /**
     * Sets a collection of TripSegmentConverter.
     *
     * @param a collection of TripSegmentConverter to set
     */
    public void setTripSegment(Collection<TripSegmentConverter> items) {
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
     * Returns a collection TripSegment entities.
     *
     * @return a collection of TripSegment entities
     */
    @XmlTransient
    public Collection<TripSegment> getEntities() {
        entities = new ArrayList<TripSegment>();
        if (items != null) {
            for (TripSegmentConverter item : items) {
                entities.add(item.getEntity());
            }
        }
        return entities;
    }
}
