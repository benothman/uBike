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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * {@code UserProfile}
 * <p/>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Entity
@Table(name = "USER_PROFILES")
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TOTAL_DISTANCE")
    private double totalDistance;
    @Column(name = "MIN_DISTANCE")
    private double minDistance;
    @Column(name = "MAX_DISTANCE")
    private double maxDistance;
    @Column(name = "AVG_DISTANCE")
    private double avgDistance;
    @Column(name = "AVG_SPEED")
    private double avgSpeed;
    @Column(name = "MIN_SPEED")
    private double minSpeed;
    @Column(name = "MAX_SPEED")
    private double maxSpeed;
    @Column(name = "TOTAL_DURATION")
    private int totalDuration;
    @Column(name = "MIN_DURATION")
    private int minDuration;
    @Column(name = "MAX_DURATION")
    private int maxDuration;
    @Column(name = "AVG_DURATION")
    private int avgDuration;

    /**
     * Create a new instance of {@code UserProfile}
     */
    public UserProfile() {
        super();
    }

    /**
     * Getter for the <tt>id</tt>
     * 
     * @return the <tt>id</tt>
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for the <tt>id</tt>
     * 
     * @param id the <tt>id</tt> to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the totalDistance
     */
    public double getTotalDistance() {
        return totalDistance;
    }

    /**
     * @param totalDistance the totalDistance to set
     */
    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    /**
     * @return the avgSpeed
     */
    public double getAvgSpeed() {
        return avgSpeed;
    }

    /**
     * @param avgSpeed the avgSpeed to set
     */
    public void setAvgSpeed(double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    /**
     * @return the totalDuration
     */
    public int getTotalDuration() {
        return totalDuration;
    }

    /**
     * @param totalDuration the totalDuration to set
     */
    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    @Transient
    public String getTotalDurationAsString() {
        return Util.formatDuration(this.totalDuration);
    }

    @Transient
    public String getMaxDurationAsString() {
        return Util.formatDuration(this.maxDuration);
    }

    @Transient
    public String getMinDurationAsString() {
        return Util.formatDuration(this.minDuration);
    }

    @Transient
    public String getAvgDurationAsString() {
        return Util.formatDuration(this.avgDuration);
    }

    /**
     * @return the minDistance
     */
    public double getMinDistance() {
        return minDistance;
    }

    /**
     * @param minDistance the minDistance to set
     */
    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    /**
     * @return the maxDistance
     */
    public double getMaxDistance() {
        return maxDistance;
    }

    /**
     * @param maxDistance the maxDistance to set
     */
    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    /**
     * @return the minDuration
     */
    public int getMinDuration() {
        return minDuration;
    }

    /**
     * @param minDuration the minDuration to set
     */
    public void setMinDuration(int minDuration) {
        this.minDuration = minDuration;
    }

    /**
     * @return the maxDuration
     */
    public int getMaxDuration() {
        return maxDuration;
    }

    /**
     * @param maxDuration the maxDuration to set
     */
    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    /**
     * @return the minSpeed
     */
    public double getMinSpeed() {
        return minSpeed;
    }

    /**
     * @param minSpeed the minSpeed to set
     */
    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    /**
     * @return the maxSpeed
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * @param maxSpeed the maxSpeed to set
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * @return the avgDistance
     */
    public double getAvgDistance() {
        return avgDistance;
    }

    /**
     * @param avgDistance the avgDistance to set
     */
    public void setAvgDistance(double avgDistance) {
        this.avgDistance = avgDistance;
    }

    /**
     * @return the avgDuration
     */
    public int getAvgDuration() {
        return avgDuration;
    }

    /**
     * @param avgDuration the avgDuration to set
     */
    public void setAvgDuration(int avgDuration) {
        this.avgDuration = avgDuration;
    }
}
