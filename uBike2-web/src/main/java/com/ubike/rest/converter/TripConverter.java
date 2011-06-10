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
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import java.util.Collection;
import com.ubike.model.UbikeUser;
import com.ubike.model.Route;
import com.ubike.model.TripSegment;

/**
 *
 * @author Benothman
 */

@XmlRootElement(name = "trip")
public class TripConverter {
    private Trip entity;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of TripConverter */
    public TripConverter() {
        entity = new Trip();
    }

    /**
     * Creates a new instance of TripConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public TripConverter(Trip entity, URI uri, int expandLevel,
            boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getOwner();
        getSegments();
        getRoute();
    }

    /**
     * Creates a new instance of TripConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public TripConverter(Trip entity, URI uri, int expandLevel) {
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
     * Getter for startDate.
     *
     * @return value for startDate
     */
    @XmlElement
    public Date getStartDate() {
        return (expandLevel > 0) ? entity.getStartDate() : null;
    }

    /**
     * Setter for startDate.
     *
     * @param value the value to set
     */
    public void setStartDate(Date value) {
        entity.setStartDate(value);
    }

    /**
     * Getter for endDate.
     *
     * @return value for endDate
     */
    @XmlElement
    public Date getEndDate() {
        return (expandLevel > 0) ? entity.getEndDate() : null;
    }

    /**
     * Setter for endDate.
     *
     * @param value the value to set
     */
    public void setEndDate(Date value) {
        entity.setEndDate(value);
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
     * Getter for duration.
     *
     * @return value for duration
     */
    @XmlElement
    public Integer getDuration() {
        return (expandLevel > 0) ? entity.getDuration() : null;
    }

    /**
     * Setter for duration.
     *
     * @param value the value to set
     */
    public void setDuration(Integer value) {
        entity.setDuration(value);
    }

    /**
     * Getter for denivele.
     *
     * @return value for denivele
     */
    @XmlElement
    public Double getDenivele() {
        return (expandLevel > 0) ? entity.getDenivele() : null;
    }

    /**
     * Setter for denivele.
     *
     * @param value the value to set
     */
    public void setDenivele(Double value) {
        entity.setDenivele(value);
    }

    /**
     * Getter for avgSpeed.
     *
     * @return value for avgSpeed
     */
    @XmlElement
    public Double getAvgSpeed() {
        return (expandLevel > 0) ? entity.getAvgSpeed() : null;
    }

    /**
     * Setter for avgSpeed.
     *
     * @param value the value to set
     */
    public void setAvgSpeed(Double value) {
        entity.setAvgSpeed(value);
    }

    /**
     * Getter for maxSpeed.
     *
     * @return value for maxSpeed
     */
    @XmlElement
    public Double getMaxSpeed() {
        return (expandLevel > 0) ? entity.getMaxSpeed() : null;
    }

    /**
     * Setter for maxSpeed.
     *
     * @param value the value to set
     */
    public void setMaxSpeed(Double value) {
        entity.setMaxSpeed(value);
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
     * Getter for segments.
     *
     * @return value for segments
     */
    @XmlElement
    public TripSegmentsConverter getSegments() {
        if (expandLevel > 0) {
            if (entity.getSegments() != null) {
                return new TripSegmentsConverter(entity.getSegments(),
                        uri.resolve("segments/"), expandLevel - 1);
            }
        }
        return null;
    }

    /**
     * Setter for segments.
     *
     * @param value the value to set
     */
    public void setSegments(TripSegmentsConverter value) {
        entity.setSegments((value != null) ? value.getEntities() : null);
    }

    /**
     * Getter for route.
     *
     * @return value for route
     */
    @XmlElement
    public RouteConverter getRoute() {
        if (expandLevel > 0) {
            if (entity.getRoute() != null) {
                return new RouteConverter(entity.getRoute(),
                        uri.resolve("route/"), expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for route.
     *
     * @param value the value to set
     */
    public void setRoute(RouteConverter value) {
        entity.setRoute((value != null) ? value.getEntity() : null);
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
     * Returns the Trip entity.
     *
     * @return an entity
     */
    @XmlTransient
    public Trip getEntity() {
        if (entity.getId() == null) {
            TripConverter converter = UriResolver.getInstance().resolve(TripConverter.class,
                    uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved Trip entity.
     *
     * @return an resolved entity
     */
    public Trip resolveEntity(EntityManager em) {
        UbikeUser owner = entity.getOwner();
        if (owner != null) {
            entity.setOwner(em.getReference(UbikeUser.class, owner.getId()));
        }
        Collection<TripSegment> segments = entity.getSegments();
        Collection<TripSegment> newsegments = new java.util.ArrayList<TripSegment>();
        for (TripSegment item : segments) {
            newsegments.add(em.getReference(TripSegment.class, item.getId()));
        }
        entity.setSegments(newsegments);
        Route route = entity.getRoute();
        if (route != null) {
            entity.setRoute(em.getReference(Route.class, route.getId()));
        }
        return entity;
    }
}
