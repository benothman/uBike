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

import com.ubike.model.GpsFile;
import java.net.URI;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import java.util.Collection;
import com.ubike.model.TripSegment;

/**
 *
 * @author Benothman
 */

@XmlRootElement(name = "gpsFile")
public class GpsFileConverter {
    private GpsFile entity;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of GpsFileConverter */
    public GpsFileConverter() {
        entity = new GpsFile();
    }

    /**
     * Creates a new instance of GpsFileConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public GpsFileConverter(GpsFile entity, URI uri, int expandLevel,
            boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getSegments();
    }

    /**
     * Creates a new instance of GpsFileConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public GpsFileConverter(GpsFile entity, URI uri, int expandLevel) {
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
     * Getter for fileName.
     *
     * @return value for fileName
     */
    @XmlElement
    public String getFileName() {
        return (expandLevel > 0) ? entity.getFileName() : null;
    }

    /**
     * Setter for fileName.
     *
     * @param value the value to set
     */
    public void setFileName(String value) {
        entity.setFileName(value);
    }

    /**
     * Getter for length.
     *
     * @return value for length
     */
    @XmlElement
    public Long getLength() {
        return (expandLevel > 0) ? entity.getLength() : null;
    }

    /**
     * Setter for length.
     *
     * @param value the value to set
     */
    public void setLength(Long value) {
        entity.setLength(value);
    }

    /**
     * Getter for creationDate.
     *
     * @return value for creationDate
     */
    @XmlElement
    public Date getCreationDate() {
        return (expandLevel > 0) ? entity.getCreationDate() : null;
    }

    /**
     * Setter for creationDate.
     *
     * @param value the value to set
     */
    public void setCreationDate(Date value) {
        entity.setCreationDate(value);
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
     * Returns the GpsFile entity.
     *
     * @return an entity
     */
    @XmlTransient
    public GpsFile getEntity() {
        if (entity.getId() == null) {
            GpsFileConverter converter = UriResolver.getInstance().resolve(GpsFileConverter.class,
                    uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved GpsFile entity.
     *
     * @return an resolved entity
     */
    public GpsFile resolveEntity(EntityManager em) {
        Collection<TripSegment> segments = entity.getSegments();
        Collection<TripSegment> newsegments = new java.util.ArrayList<TripSegment>();
        for (TripSegment item : segments) {
            newsegments.add(em.getReference(TripSegment.class, item.getId()));
        }
        entity.setSegments(newsegments);
        return entity;
    }
}
