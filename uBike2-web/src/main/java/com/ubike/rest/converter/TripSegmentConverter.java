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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import com.ubike.model.GpsFile;
import com.ubike.model.Trip;

/**
 *
 * @author Benothman
 */

@XmlRootElement(name = "tripSegment")
public class TripSegmentConverter {
    private TripSegment entity;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of TripSegmentConverter */
    public TripSegmentConverter() {
        entity = new TripSegment();
    }

    /**
     * Creates a new instance of TripSegmentConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public TripSegmentConverter(TripSegment entity, URI uri, int expandLevel,
            boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getGpsFile();
        getTrip();
    }

    /**
     * Creates a new instance of TripSegmentConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public TripSegmentConverter(TripSegment entity, URI uri, int expandLevel) {
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
     * Getter for gpsFile.
     *
     * @return value for gpsFile
     */
    @XmlElement
    public GpsFileConverter getGpsFile() {
        if (expandLevel > 0) {
            if (entity.getGpsFile() != null) {
                return new GpsFileConverter(entity.getGpsFile(),
                        uri.resolve("gpsFile/"), expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for gpsFile.
     *
     * @param value the value to set
     */
    public void setGpsFile(GpsFileConverter value) {
        entity.setGpsFile((value != null) ? value.getEntity() : null);
    }

    /**
     * Getter for trip.
     *
     * @return value for trip
     */
    @XmlElement
    public TripConverter getTrip() {
        if (expandLevel > 0) {
            if (entity.getTrip() != null) {
                return new TripConverter(entity.getTrip(), uri.resolve("trip/"),
                        expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for trip.
     *
     * @param value the value to set
     */
    public void setTrip(TripConverter value) {
        entity.setTrip((value != null) ? value.getEntity() : null);
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
     * Returns the TripSegment entity.
     *
     * @return an entity
     */
    @XmlTransient
    public TripSegment getEntity() {
        if (entity.getId() == null) {
            TripSegmentConverter converter = UriResolver.getInstance().resolve(TripSegmentConverter.class,
                    uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved TripSegment entity.
     *
     * @return an resolved entity
     */
    public TripSegment resolveEntity(EntityManager em) {
        GpsFile gpsFile = entity.getGpsFile();
        if (gpsFile != null) {
            entity.setGpsFile(em.getReference(GpsFile.class, gpsFile.getId()));
        }
        Trip trip = entity.getTrip();
        if (trip != null) {
            entity.setTrip(em.getReference(Trip.class, trip.getId()));
        }
        return entity;
    }
}
