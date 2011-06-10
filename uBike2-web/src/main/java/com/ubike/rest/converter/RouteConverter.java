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

import com.ubike.model.Route;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import com.ubike.model.Trip;
import java.util.List;

/**
 *
 * @author Benothman
 */
@XmlRootElement(name = "route")
public class RouteConverter {

    private Route entity;
    private URI uri;
    private int expandLevel;

    /** Creates a new instance of RouteConverter */
    public RouteConverter() {
        entity = new Route();
    }

    /**
     * Creates a new instance of RouteConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public RouteConverter(Route entity, URI uri, int expandLevel,
            boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getTrips();
    }

    /**
     * Creates a new instance of RouteConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public RouteConverter(Route entity, URI uri, int expandLevel) {
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
     * Getter for boundingBox.
     *
     * @return value for boundingBox
     */
    @XmlElement
    public String getBoundingBox() {
        return (expandLevel > 0) ? entity.getBoundingBox() : null;
    }

    /**
     * Setter for boundingBox.
     *
     * @param value the value to set
     */
    public void setBoundingBox(String value) {
        entity.setBoundingBox(value);
    }

    /**
     * Getter for distance.
     *
     * @return value for distance
     */
    @XmlElement
    public Double getDistance() {
        return (expandLevel > 0) ? entity.getDistance() : null;
    }

    /**
     * Setter for distance.
     *
     * @param value the value to set
     */
    public void setDistance(Double value) {
        entity.setDistance(value);
    }

    /**
     * Getter for trips.
     *
     * @return value for trips
     */
    @XmlElement
    public TripsConverter getTrips() {
        if (expandLevel > 0) {
            if (entity.getTrips() != null) {
                return new TripsConverter(entity.getTrips(),
                        uri.resolve("trips/"), expandLevel - 1);
            }
        }
        return null;
    }

    /**
     * Setter for trips.
     *
     * @param value the value to set
     */
    public void setTrips(TripsConverter value) {
        entity.setTrips((value != null) ? value.getEntities() : null);
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
     * Returns the Route entity.
     *
     * @return an entity
     */
    @XmlTransient
    public Route getEntity() {
        if (entity.getId() == null) {
            RouteConverter converter = UriResolver.getInstance().resolve(RouteConverter.class,
                    uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved Route entity.
     *
     * @return an resolved entity
     */
    public Route resolveEntity(EntityManager em) {
        List<Trip> trips = entity.getTrips();
        List<Trip> newtrips = new java.util.ArrayList<Trip>();
        for (Trip item : trips) {
            newtrips.add(em.getReference(Trip.class, item.getId()));
        }
        entity.setTrips(newtrips);
        return entity;
    }
}
