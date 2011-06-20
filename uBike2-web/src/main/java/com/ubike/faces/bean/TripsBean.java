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
package com.ubike.faces.bean;

import com.ubike.model.Trip;
import com.ubike.util.CustomArrayList;
import com.ubike.util.CustomList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.richfaces.component.SortOrder;

/**
 * {@code TripsBean}
 * <p/>
 *
 * Created on Jun 15, 2011 at 8:36:19 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "tripsBean")
@ViewScoped
public class TripsBean extends AbstractBean {

    private CustomList<Trip> trips;
    private SortOrder usernameOrder;
    private SortOrder datesOrder;
    private SortOrder distanceOrder;
    private SortOrder durationOrder;
    private SortOrder avgSpeedOrder;
    private SortOrder maxSpeedOrder;

    /**
     * Create a new instance of {@code TripsBean}
     */
    public TripsBean() {
        super();
    }

    @PostConstruct
    protected void init() {
        List<Trip> tmp = (List<Trip>) BaseBean.getSessionAttribute("tmp_trips");
        if (tmp == null) {
            this.trips = new CustomArrayList<Trip>();
        } else {
            this.trips = new CustomArrayList<Trip>(tmp);
        }
        this.datesOrder = SortOrder.ascending;
        this.usernameOrder = SortOrder.unsorted;
        this.durationOrder = SortOrder.unsorted;
        this.avgSpeedOrder = SortOrder.unsorted;
        this.maxSpeedOrder = SortOrder.unsorted;
        this.distanceOrder = SortOrder.unsorted;
    }

    @PreDestroy
    protected void destroy() {
        this.trips = null;
    }

    /**
     * 
     * @return 
     */
    public boolean getPaginate() {
        return this.trips.size() > 10;
    }

    /**
     * Sort trips by date
     */
    public void sortByDate() {
        setUsernameOrder(SortOrder.unsorted);
        setDistanceOrder(SortOrder.unsorted);
        setDurationOrder(SortOrder.unsorted);
        setAvgSpeedOrder(SortOrder.unsorted);
        setMaxSpeedOrder(SortOrder.unsorted);
        setDatesOrder(this.datesOrder.equals(SortOrder.ascending)
                ? SortOrder.descending : SortOrder.ascending);
    }

    /**
     * Sort trips by username
     */
    public void sortByUsername() {
        setDatesOrder(SortOrder.unsorted);
        setDistanceOrder(SortOrder.unsorted);
        setDurationOrder(SortOrder.unsorted);
        setAvgSpeedOrder(SortOrder.unsorted);
        setMaxSpeedOrder(SortOrder.unsorted);
        setUsernameOrder(usernameOrder.equals(SortOrder.ascending)
                ? SortOrder.descending : SortOrder.ascending);
    }

    /**
     * sort trips by average Speed
     */
    public void sortByAvgSpeed() {
        setMaxSpeedOrder(SortOrder.unsorted);
        setUsernameOrder(SortOrder.unsorted);
        setDistanceOrder(SortOrder.unsorted);
        setDurationOrder(SortOrder.unsorted);
        setDatesOrder(SortOrder.unsorted);
        setAvgSpeedOrder(avgSpeedOrder.equals(SortOrder.ascending)
                ? SortOrder.descending : SortOrder.ascending);
    }

    /**
     * Sort trips by max speed
     */
    public void sortByMaxSpeed() {
        setAvgSpeedOrder(SortOrder.unsorted);
        setUsernameOrder(SortOrder.unsorted);
        setDistanceOrder(SortOrder.unsorted);
        setDurationOrder(SortOrder.unsorted);
        setDatesOrder(SortOrder.unsorted);
        setMaxSpeedOrder(maxSpeedOrder.equals(SortOrder.ascending)
                ? SortOrder.descending : SortOrder.ascending);
    }

    /**
     * Sort trips by duration
     */
    public void sortByDuration() {
        setUsernameOrder(SortOrder.unsorted);
        setDistanceOrder(SortOrder.unsorted);
        setAvgSpeedOrder(SortOrder.unsorted);
        setMaxSpeedOrder(SortOrder.unsorted);
        setDatesOrder(SortOrder.unsorted);
        setDurationOrder(durationOrder.equals(SortOrder.ascending)
                ? SortOrder.descending : SortOrder.ascending);
    }

    /**
     * sort trips by distance
     */
    public void sortByDistance() {
        setUsernameOrder(SortOrder.unsorted);
        setDurationOrder(SortOrder.unsorted);
        setAvgSpeedOrder(SortOrder.unsorted);
        setMaxSpeedOrder(SortOrder.unsorted);
        setDatesOrder(SortOrder.unsorted);
        setDistanceOrder(distanceOrder.equals(SortOrder.ascending)
                ? SortOrder.descending : SortOrder.ascending);
    }

    /**
     * @return the trips
     */
    public CustomList<Trip> getTrips() {
        return trips;
    }

    /**
     * @param trips the trips to set
     */
    public void setTrips(CustomList<Trip> trips) {
        this.trips = trips;
    }

    /**
     * @return the usernameOrder
     */
    public SortOrder getUsernameOrder() {
        return usernameOrder;
    }

    /**
     * @param usernameOrder the usernameOrder to set
     */
    public void setUsernameOrder(SortOrder usernameOrder) {
        this.usernameOrder = usernameOrder;
    }

    /**
     * @return the datesOrder
     */
    public SortOrder getDatesOrder() {
        return datesOrder;
    }

    /**
     * @param datesOrder the datesOrder to set
     */
    public void setDatesOrder(SortOrder datesOrder) {
        this.datesOrder = datesOrder;
    }

    /**
     * @return the distanceOrder
     */
    public SortOrder getDistanceOrder() {
        return distanceOrder;
    }

    /**
     * @param distanceOrder the distanceOrder to set
     */
    public void setDistanceOrder(SortOrder distanceOrder) {
        this.distanceOrder = distanceOrder;
    }

    /**
     * @return the durationOrder
     */
    public SortOrder getDurationOrder() {
        return durationOrder;
    }

    /**
     * @param durationOrder the durationOrder to set
     */
    public void setDurationOrder(SortOrder durationOrder) {
        this.durationOrder = durationOrder;
    }

    /**
     * @return the avgSpeedOrder
     */
    public SortOrder getAvgSpeedOrder() {
        return avgSpeedOrder;
    }

    /**
     * @param avgSpeedOrder the avgSpeedOrder to set
     */
    public void setAvgSpeedOrder(SortOrder avgSpeedOrder) {
        this.avgSpeedOrder = avgSpeedOrder;
    }

    /**
     * @return the maxSpeedOrder
     */
    public SortOrder getMaxSpeedOrder() {
        return maxSpeedOrder;
    }

    /**
     * @param maxSpeedOrder the maxSpeedOrder to set
     */
    public void setMaxSpeedOrder(SortOrder maxSpeedOrder) {
        this.maxSpeedOrder = maxSpeedOrder;
    }
}
