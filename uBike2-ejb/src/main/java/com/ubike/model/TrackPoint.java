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

/**
 * {@code TrackPoint}
 * <p/>
 *
 * Created on Jun 3, 2011 at 10:56:20 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public class TrackPoint implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 1L;
    private GpsCoordinate gpsCoord;
    private TripSegment tripsegment;
    private long timestamp;
    private double speedKnot;
    private int fixQuality;
    private int numberOfSatellites;

    /**
     * Create a new instance of {@code TrackPoint}
     */
    public TrackPoint() {
        this(0, 0, 0, 0);
    }

    /**
     * Create a new instance of {@code TrackPoint}
     * @param latitude
     * @param longitude
     */
    public TrackPoint(double latitude, double longitude) {
        this(latitude, longitude, 0, 0);
    }

    /**
     * Create a new instance of {@code TrackPoint}
     * 
     * @param latitude the latitude
     * @param longitude the longitude
     * @param altitude the altitude
     */
    public TrackPoint(double latitude, double longitude, double altitude) {
        this(latitude, longitude, altitude, 0);
    }

    /**
     * Create a new <code>TrackPoint</code> instance with the given latitude,
     * longitude, altitude and timestamp
     *
     * @param latitude
     * @param longitude
     * @param altitude
     * @param timestamp
     */
    public TrackPoint(double latitude, double longitude, double altitude, long timestamp) {
        this.gpsCoord = new GpsCoordinate(latitude, longitude, altitude);
        this.timestamp = timestamp;
    }

    /**
     * Create a new instance of {@code TrackPoint}
     * @param gpsCoord
     * @param tripsegment
     * @param timestamp
     */
    public TrackPoint(GpsCoordinate gpsCoord, TripSegment tripsegment, Long timestamp) {
        this.gpsCoord = gpsCoord;
        this.tripsegment = tripsegment;
        this.timestamp = timestamp;
    }

    /**
     * @return the gpsCoord
     */
    public GpsCoordinate getGpsCoord() {
        return this.gpsCoord;
    }

    /**
     * @param gpsCoord the gpsCoord to set
     */
    public void setGpsCoord(GpsCoordinate gpsCoord) {
        this.gpsCoord = gpsCoord;
    }

    /**
     * @return the time stamp
     */
    public Long getTimestamp() {
        return this.timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the tripsegment
     */
    public TripSegment getTripsegment() {
        return this.tripsegment;
    }

    /**
     * @param tripsegment the trip segment to set
     */
    public void setTripsegment(TripSegment tripsegment) {
        this.tripsegment = tripsegment;
    }

    /**
     * @return the speedKnot
     */
    public double getSpeedKnot() {
        return this.speedKnot;
    }

    /**
     * @param speedKnot the speedKnot to set
     */
    public void setSpeedKnot(double speedKnot) {
        this.speedKnot = speedKnot;
    }

    /**
     * @return the fixQuality
     */
    public int getFixQuality() {
        return this.fixQuality;
    }

    /**
     * @param fixQuality the fixQuality to set
     */
    public void setFixQuality(int fixQuality) {
        this.fixQuality = fixQuality;
    }

    /**
     * @return the numberOfSatellites
     */
    public int getNumberOfSatellites() {
        return this.numberOfSatellites;
    }

    /**
     * @param numberOfSatellites the numberOfSatellites to set
     */
    public void setNumberOfSatellites(int numberOfSatellites) {
        this.numberOfSatellites = numberOfSatellites;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return this.gpsCoord.getLatitude();
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.gpsCoord.setLatitude(latitude);
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return this.gpsCoord.getLongitude();
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.gpsCoord.setLongitude(longitude);
    }

    /**
     * @return the altitude
     */
    public double getAltitude() {
        return this.gpsCoord.getAltitude();
    }

    /**
     * @param altitude the altitude to set
     */
    public void setAltitude(double altitude) {
        this.gpsCoord.setAltitude(altitude);
    }

    /**
     * Compute the distance between this trackpoint and the given one
     * 
     * @param other The second <code>TrackPoint</code>
     * @return The distance between this trackpoint and the given one
     */
    public double distanceTo(TrackPoint other) {
        return distance(this.getLatitude(), this.getLongitude(), other.getLatitude(), other.getLongitude());
    }

    /**
     * Compute the distance between the given trackpoints
     *
     * @param one The first <code>TrackPoint</code>
     * @param two The second <code>TrackPoint</code>
     * @return The distance between two given trackpoints
     */
    public static double computeDistance(TrackPoint one, TrackPoint two) {
        return distance(one.getLatitude(), one.getLongitude(), two.getLatitude(),
                two.getLongitude());
    }

    /**
     * Compute the distance between two trackpoints given by their latitudes and
     * longitudes
     * 
     * @param lat1 The latitude of the first <code>TrackPoint</code>
     * @param long1 The longitude of the first <code>TrackPoint</code>
     * @param lat2 The latitude of the second <code>TrackPoint</code>
     * @param long2 The longitude of the second <code>TrackPoint</code>
     * @return The distance between two trackpoints
     */
    public static double distance(double lat1, double long1, double lat2, double long2) {
        final double la1 = Math.toRadians(lat1);
        final double la2 = Math.toRadians(lat2);
        final double lo1 = Math.toRadians(long1);
        final double lo2 = Math.toRadians(long2);

        final double dLat = la2 - la1;
        final double dLong = lo2 - lo1;

        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(la1)
                * Math.cos(la2) * Math.sin(dLong / 2) * Math.sin(dLong / 2);

        double earthRadius = 6366.7070194937075;
        earthRadius = 6371; // ????
        // distance is always positive
        return Math.abs(2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)) * earthRadius);
    }
}
