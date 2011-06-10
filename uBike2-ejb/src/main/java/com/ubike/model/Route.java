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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * {@code Route}
 * <p/>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Entity
@Table(name = "ROUTES")
@NamedQueries({
    @NamedQuery(name = "Route.getAll", query = "SELECT o FROM Route o"),
    @NamedQuery(name = "Route.getByDistance",
    query = "SELECT o FROM Route o WHERE o.distance=:param"),
    @NamedQuery(name = "Route.getByBoundingBox",
    query = "SELECT o FROM Route o WHERE o.boundingBox=:param")
})
public class Route implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Lob
    @Column(name = "BOUNDING_BOX", nullable = false)
    private String boundingBox;
    @Column(name = "DISTANCE", nullable = false)
    private double distance;
    @OneToMany(mappedBy = "route", cascade = {CascadeType.MERGE,
        CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Trip> trips = new ArrayList<Trip>();

    /**
     * Create a new instance of {@code Route}
     */
    public Route() {
        super();
    }

    /**
     * Create a new instance of {@code Route}
     * 
     * @param boundingBox
     * @param distance 
     */
    public Route(String boundingBox, double distance) {
        this.boundingBox = boundingBox;
        this.distance = distance;
    }

    /**
     * @return the bounding box of the route
     */
    public String getBoundingBox() {
        return boundingBox;
    }

    /**
     * @param boundingBox the bounding box to set
     */
    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * @return the total distance of the route
     */
    public double getDistance() {
        return distance;
    }

    /**
     * @param distance the distance to set
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @return the list of trips
     */
    public List<Trip> getTrips() {
        return trips;
    }

    /**
     * @param trips the list of trips to set
     */
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }
}
