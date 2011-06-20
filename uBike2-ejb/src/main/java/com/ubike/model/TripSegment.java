/*
 * Copyright 2011, Nabil Benothman, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.ubike.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * {@code TripSegment}
 * <p>
 * This class represents a trip segment, it means that a trip is composed
 * by a list of segments. A trip segment is composed by a list of track points
 * </p>
 *
 * Created on Jun 3, 2011 at 10:56:20 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Entity
@Table(name = "TRIP_SEGMENTS")
@NamedQueries({
    @NamedQuery(name = "TripSegment.getAll",
    query = "SELECT o FROM TripSegment o"),
    @NamedQuery(name = "TripSegment.getByTrip",
    query = "SELECT o FROM TripSegment o WHERE o.trip.id=:tripId"),
    @NamedQuery(name = "TripSegment.getByGpsFile",
    query = "SELECT o FROM TripSegment o WHERE o.gpsFile.id=:fileId")
})
public class TripSegment implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name = "ID")
    private Long id;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_GPSFILE")
    private GpsFile gpsFile;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_TRIP", referencedColumnName = "ID")
    private Trip trip;

    /**
     * Create a new instance of {@code TripSegment}
     */
    public TripSegment() {
        super();
    }

    /**
     * @return The id of the <code>UbikeEntity</code>
     */
    public Long getId() {
        return this.id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the GPS file that contains the Trip Segment
     */
    public GpsFile getGpsFile() {
        return this.gpsFile;
    }

    /**
     * @param gpsFile the GPS File to set
     */
    public void setGpsFile(GpsFile gpsFile) {
        this.gpsFile = gpsFile;
    }

    /**
     * @return the trip
     */
    public Trip getTrip() {
        return this.trip;
    }

    /**
     * @param trip the trip to set
     */
    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
