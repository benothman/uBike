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

import com.ubike.util.Util;
import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedList;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author BENOTHMAN Nabil.
 */
@Entity
@Table(name = "TRIPS")
@NamedQueries({
    @NamedQuery(name = "Trip.getAll", query = "SELECT o FROM Trip o"),
    @NamedQuery(name = "Trip.getUserTripByDate",
    query = "SELECT o FROM Trip o WHERE o.owner.id=:ownerId AND o.startDate<=:sDate AND o.endDate <=:eDate"),
    @NamedQuery(name = "Trip.getUserTrips",
    query = "SELECT o FROM Trip o WHERE o.owner.id=:ownerId")
})
public class Trip implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "END_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Column(name = "DISTANCE")
    private double distance;
    @Column(name = "DURATION")
    private int duration; // duration en second
    @Column(name = "DENIVELE")
    private double denivele;
    @Column(name = "AVG_SPEED")
    private double avgSpeed;
    @Column(name = "MAX_SPEED")
    private double maxSpeed;
    @Column(name = "AVG_LAT")
    private double avgLat;
    @Column(name = "AVG_LON")
    private double avgLon;
    @Column(name = "START_LAT")
    private double startLat;
    @Column(name = "START_LON")
    private double startLon;
    @Column(name = "END_LAT")
    private double endLat;
    @Column(name = "END_LON")
    private double endLon;
    @Lob
    @Column(name = "MAP_CODE")
    private String mapCode;
    @Lob
    @Column(name = "MAP_LEVELS")
    private String mapLevels;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_OWNER", referencedColumnName = "ID")
    private UbikeUser owner;
    @OneToMany(mappedBy = "trip", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private Collection<TripSegment> segments = new LinkedList<TripSegment>();
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ROUTE", referencedColumnName = "ID")
    private Route route;

    /**
     *
     */
    public Trip() {
        this.startDate = new Date(Calendar.getInstance().getTimeInMillis());
    }

    /**
     * @param sDate
     * @param eDate
     * @param duration
     * @param dist
     * @param avgSpeed
     * @param maxSpeed
     */
    public Trip(Date sDate, Date eDate, int duration, double dist, double avgSpeed, double maxSpeed) {
        this(sDate, eDate, dist, avgSpeed, maxSpeed);
        this.duration = duration;
    }

    /**
     * 
     * @param sDate
     * @param eDate
     * @param dist
     * @param avgSpeed
     * @param maxSpeed
     */
    public Trip(Date sDate, Date eDate, double dist, double avgSpeed, double maxSpeed) {
        this.startDate = sDate;
        this.endDate = eDate;
        this.distance = dist;
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
    }

    /**
     * @param segments
     */
    public Trip(Collection<TripSegment> segments) {
        this();
        this.segments = segments;
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
     * @return the owner of this trip
     */
    public UbikeUser getOwner() {
        return this.owner;
    }

    /**
     * @param owner the owner user to set
     */
    public void setOwner(UbikeUser owner) {
        this.owner = owner;
    }

    /**
     * @return the list of trip segments
     */
    public Collection<TripSegment> getSegments() {
        return this.segments;
    }

    /**
     * @param segments
     */
    public void setSegments(Collection<TripSegment> segments) {
        this.segments = segments;
    }

    /**
     * @return The start date of Trip
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * @param date the start date to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return The end date of Trip
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * @param date the end date to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the total distance of the trip
     */
    public double getDistance() {
        return this.distance;
    }

    /**
     * @param distance the total distance of the trip
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the average speed
     */
    public double getAvgSpeed() {
        return this.avgSpeed;
    }

    /**
     * @param averageSpeed the average speed to set
     */
    public void setAvgSpeed(double averageSpeed) {
        this.avgSpeed = averageSpeed;
    }

    /**
     * @return the denivele
     */
    public double getDenivele() {
        return this.denivele;
    }

    /**
     * @param denivele the denivele to set
     */
    public void setDenivele(double denivele) {
        this.denivele = denivele;
    }

    /**
     * @return the duration of the trip
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * @return the speed max
     */
    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    /**
     * @param maxSpeed the speed max to set
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * @return the route of the trip
     */
    public Route getRoute() {
        return this.route;
    }

    /**
     * @param route the route to set
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * @return the mapCode
     */
    public String getMapCode() {
        return this.mapCode;
    }

    /**
     * @param mapCode the mapCode to set
     */
    public void setMapCode(String mapCode) {
        this.mapCode = mapCode;
    }

    /**
     * @return the mapLevels
     */
    public String getMapLevels() {
        return mapLevels;
    }

    /**
     * @param mapLevels the mapLevels to set
     */
    public void setMapLevels(String mapLevels) {
        this.mapLevels = mapLevels;
    }

    /**
     * @return the avgLat
     */
    public double getAvgLat() {
        return avgLat;
    }

    /**
     * @param avgLat the avgLat to set
     */
    public void setAvgLat(double avgLat) {
        this.avgLat = avgLat;
    }

    /**
     * @return the avgLon
     */
    public double getAvgLon() {
        return avgLon;
    }

    /**
     * @param avgLon the avgLon to set
     */
    public void setAvgLon(double avgLon) {
        this.avgLon = avgLon;
    }

    /**
     * @return the startLat
     */
    public double getStartLat() {
        return startLat;
    }

    /**
     * @param startLat the startLat to set
     */
    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    /**
     * @return the startLon
     */
    public double getStartLon() {
        return startLon;
    }

    /**
     * @param startLon the startLon to set
     */
    public void setStartLon(double startLon) {
        this.startLon = startLon;
    }

    /**
     * @return the endLat
     */
    public double getEndLat() {
        return endLat;
    }

    /**
     * @param endLat the endLat to set
     */
    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    /**
     * @return the endLon
     */
    public double getEndLon() {
        return endLon;
    }

    /**
     * @param endLon the endLon to set
     */
    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }

    @Transient
    public String getStartDateAsString() {
        return Util.formatTimestamp(startDate);
    }

    @Transient
    public String getEndDateAsString() {
        return Util.formatTimestamp(endDate);
    }

    @Transient
    public String getDurationAsString() {
        return Util.formatDuration(this.duration);
    }
}
