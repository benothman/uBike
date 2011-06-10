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
 * {@code GpsCoordinate}
 * <p/>
 *
 * Created on Jun 3, 2011 at 10:56:20 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public class GpsCoordinate implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 1L;
  
    private double latitude;
    private double longitude;
    private double altitude;


    /**
     * Create a new instance of {@code GpsCoordinate}
     */
    public GpsCoordinate() {
        super();
    }

    /**
     * Create a new instance of {@code GpsCoordinate} with the given
     * coordinates (latitude, longitude and altitude).
     * 
     * @param latitude
     * @param longitude
     */
    public GpsCoordinate(double latitude, double longitude, double altitude) {
        this.latitude  = latitude;
        this.longitude = longitude;
        this.altitude  = altitude;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the altitude
     */
    public double getAltitude() {
        return this.altitude;
    }

    /**
     * @param altitude the altitude to set
     */
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != GpsCoordinate.class) {
            return false;
        }

        GpsCoordinate o2 = (GpsCoordinate) o;
        return o2.getLatitude() == this.latitude && o2.getLongitude() == this.longitude;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(
                this.latitude) >>> 32));
        hash = 43 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(
                this.longitude) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return "GPSCoordinate (" + this.latitude + ", " + this.longitude + ")";
    }

}
