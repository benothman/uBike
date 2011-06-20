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
package com.ubike.util;

import com.ubike.model.Statistic;
import com.ubike.model.Trip;
import com.ubike.model.UbikeGroup;
import com.ubike.model.UserStatistic;
import com.ubike.services.StatisticServiceLocal;
import com.ubike.services.TripManagerLocal;
import com.ubike.services.TripServiceLocal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author Benothman
 */
public class StatisticManager {

    private TripManagerLocal tml;
    private TripServiceLocal tripService;
    private StatisticServiceLocal statisticService;

    /**
     * 
     * @param tml an instance of a <code>TripManagerLocal</code>
     */
    public StatisticManager(TripManagerLocal tml) {
        this.tml = tml;
    }

    /**
     * Update the statistics of the given <code>UbikeEntity</code> with the given
     * trip data.
     *
     * @param entity
     * @param trip
     */
    public synchronized void updateEntityStatistics(UbikeEntity entity, Trip trip) {
        System.out.println("StatisticManager -> entity : " + entity.toString());

        updateStatistics(getDailyStat(entity, trip.getStartDate()), trip);
        updateStatistics(getWeeklyStat(entity, trip.getStartDate()), trip);
        updateStatistics(getMonthlyStat(entity, trip.getStartDate()), trip);
        updateStatistics(getYearlyStat(entity, trip.getStartDate()), trip);
        updateStatistics(getGeneralStat(entity), trip);
    }

    /**
     * 
     * @param group
     * @param trip
     */
    public synchronized void updateGroupRanking(UbikeGroup group, Trip trip) {
        throw new NotImplementedException();
    }

    /**
     * Update the given statistics with the given trip data.
     *
     * @param stats a collection of statistic
     * @param trip an instance of a trip.
     */
    public void updateStatistics(List<Statistic> stats, Trip trip) {

        double avg;
        for (Statistic o : stats) {
            switch (o.getMetric()) {
                case DISTANCE: {
                    o.setMaxValue(Math.max(o.getMaxValue(), trip.getDistance()));
                    o.setMinValue(o.isValid() ? Math.min(o.getMinValue(), trip.getDistance()) : trip.getDistance());
                    avg = o.getAvgValue() * o.getCount() + trip.getDistance();
                    avg /= (o.getCount() + 1);
                    o.setAvgValue(getDoubleValue(avg));
                    o.setTotalValue(trip.getDistance() + o.getTotalValue());
                    break;
                }
                case DURATION: {
                    o.setMaxValue(Math.max(o.getMaxValue(), trip.getDuration()));
                    o.setMinValue(o.isValid() ? Math.min(o.getMinValue(), trip.getDuration()) : trip.getDuration());
                    avg = o.getAvgValue() * o.getCount() + trip.getDuration();
                    avg /= (o.getCount() + 1);
                    o.setAvgValue(getDoubleValue(avg));
                    o.setTotalValue(trip.getDuration() + o.getTotalValue());
                    break;
                }
                case SPEED: {
                    o.setMaxValue(Math.max(o.getMaxValue(), trip.getMaxSpeed()));
                    o.setMinValue(o.isValid() ? Math.min(o.getMinValue(), trip.getAvgSpeed()) : trip.getAvgSpeed());
                    avg = o.getAvgValue() * o.getCount() + trip.getAvgSpeed();
                    avg /= (o.getCount() + 1);
                    o.setAvgValue(getDoubleValue(avg));
                    break;
                }
                default:
                    break;
            }
            o.setValid(true);
            o.setCount(o.getCount() + 1);
            statisticService.update(o);
            getTml().updateEntity(o);
        }
    }

    /**
     *
     * @param entity The owner of the statistics
     * @param date A date of statistics
     * @return a collection of an entity statistics for the given date
     */
    public List<Statistic> getDailyStat(UbikeEntity entity, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date start = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date end = cal.getTime();

        List<Statistic> collection = getTml().getEntityStatByIntervalAndType(entity.getId(), StatisticType.DAILY, start, end);

        // We have to be sure that the collection contains all statistic metrics
        Map<Metric, Statistic> stats = new EnumMap<Metric, Statistic>(Metric.class);
        for (Statistic o : collection) {
            stats.put(o.getMetric(), o);
        }
        boolean hasChanged = false;
        for (Metric m : Metric.values()) {
            if (stats.get(m) == null) {
                hasChanged = true;
                Statistic s = new UserStatistic();
                s.setMetric(m);
                s.setType(StatisticType.DAILY);
                s.setStartDate(start);
                s.setEndDate(end);
                entity.getStatistics().add(s);
                stats.put(m, s);
            }
        }

        if (hasChanged) {
            this.getTml().updateEntity(entity);
        }

        return new ArrayList<Statistic>(stats.values());
    }

    /**
     *
     * @param user
     * @return
     */
    public List<Statistic> getWeeklyStat(UbikeEntity entity, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date start = cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date end = cal.getTime();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        List<Statistic> collection = getTml().getEntityStatByIntervalAndType(entity.getId(),
                StatisticType.WEEKLY, start, end);

        // We have to be sure that the collection contains all statistic metrics
        Map<Metric, Statistic> stats = new EnumMap<Metric, Statistic>(Metric.class);

        for (Statistic o : collection) {
            stats.put(o.getMetric(), o);
        }
        boolean hasChanged = false;
        for (Metric m : Metric.values()) {
            if (stats.get(m) == null) {
                hasChanged = true;
                Statistic s = new UserStatistic();
                s.setMetric(m);
                s.setType(StatisticType.WEEKLY);
                s.setStartDate(start);
                s.setEndDate(end);
                entity.getStatistics().add(s);

                stats.put(m, s);
            }
        }
        if (hasChanged) {
            this.getTml().updateEntity(entity);
        }

        return new ArrayList<Statistic>(stats.values());
    }

    /**
     *
     * @param user
     * @return
     */
    public List<Statistic> getMonthlyStat(UbikeEntity entity, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date start = cal.getTime();
        int month = cal.get(Calendar.MONTH);
        if (month == Calendar.FEBRUARY) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.YEAR) % 4 == 0 ? 29 : 28);
        } else if (month == Calendar.APRIL || month == Calendar.JUNE
                || month == Calendar.SEPTEMBER || month == Calendar.NOVEMBER) {
            cal.set(Calendar.DAY_OF_MONTH, 30);
        } else {
            cal.set(Calendar.DAY_OF_MONTH, 31);
        }

        Date end = cal.getTime();

        // Getting statistics of the current month.
        List<Statistic> collection = getTml().getEntityStatByIntervalAndType(entity.getId(),
                StatisticType.MONTHLY, start, end);

        // We have to be sure that the collection contains all statistic metrics
        Map<Metric, Statistic> stats = new EnumMap<Metric, Statistic>(Metric.class);

        for (Statistic o : collection) {
            stats.put(o.getMetric(), o);
        }

        boolean hasChanged = false;
        for (Metric m : Metric.values()) {
            if (stats.get(m) == null) {
                hasChanged = true;
                Statistic s = new UserStatistic();
                s.setMetric(m);
                s.setType(StatisticType.MONTHLY);
                s.setStartDate(start);
                s.setEndDate(end);
                entity.getStatistics().add(s);

                stats.put(m, s);
            }
        }

        if (hasChanged) {
            this.getTml().updateEntity(entity);
        }

        return new ArrayList<Statistic>(stats.values());
    }

    /**
     *
     * @param user
     * @return
     */
    public List<Statistic> getYearlyStat(UbikeEntity entity, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // configure to 01/01/yyyy 00:00:00
        cal.set(cal.get(Calendar.YEAR), Calendar.JANUARY, 1);
        Date start = cal.getTime();
        cal.set(cal.get(Calendar.YEAR), Calendar.DECEMBER, 31);
        Date end = cal.getTime();
        List<Statistic> collection = getTml().getEntityStatByIntervalAndType(entity.getId(),
                StatisticType.YEARLY, start, end);

        // We have to be sure that the collection contains all statistic metrics
        Map<Metric, Statistic> stats = new EnumMap<Metric, Statistic>(Metric.class);
        for (Statistic o : collection) {
            stats.put(o.getMetric(), o);
        }

        boolean hasChanged = false;
        for (Metric m : Metric.values()) {
            if (stats.get(m) == null) {
                Statistic s = new UserStatistic();
                s.setMetric(m);
                s.setType(StatisticType.YEARLY);
                s.setStartDate(start);
                s.setEndDate(end);
                entity.getStatistics().add(s);

                stats.put(m, s);
            }
        }

        if (hasChanged) {
            this.getTml().updateEntity(entity);
        }

        return new ArrayList<Statistic>(stats.values());
    }

    /**
     *
     * @param user
     * @return
     */
    public List<Statistic> getGeneralStat(UbikeEntity entity) {
        Calendar cal = Calendar.getInstance();
        // configure to 01/01/yyyy 00:00:00
        cal.set(cal.get(Calendar.YEAR), Calendar.JANUARY, 1);
        Date start = cal.getTime();
        cal.set(2020, Calendar.DECEMBER, 31);
        Date end = cal.getTime();

        List<Statistic> collection = getTml().getEntityStatByType(entity.getId(), StatisticType.GENERAL);
        Map<Metric, Statistic> stats = new EnumMap<Metric, Statistic>(Metric.class);

        for (Statistic o : collection) {
            stats.put(o.getMetric(), o);
        }
        boolean hasChanged = false;
        for (Metric m : Metric.values()) {
            if (stats.get(m) == null) {
                hasChanged = true;
                Statistic s = new UserStatistic();
                s.setMetric(m);
                s.setType(StatisticType.GENERAL);
                s.setStartDate(start);
                s.setEndDate(end);
                entity.getStatistics().add(s);

                stats.put(m, s);
            }
        }
        if (hasChanged) {
            this.getTml().updateEntity(entity);
        }

        return new ArrayList<Statistic>(stats.values());
    }

    /**
     * 
     * @param value
     * @return
     */
    private double getDoubleValue(double value) {
        String tmpTab[] = ("" + value).split("\\.");
        if (tmpTab[1].length() > 3) {
            tmpTab[1] = tmpTab[1].substring(0, 3);
            value = Double.parseDouble(tmpTab[0] + "." + tmpTab[1]);
        }

        return value;
    }

    /**
     * @return the tml
     */
    public TripManagerLocal getTml() {
        return tml;
    }

    /**
     * @param tml the tml to set
     */
    public void setTml(TripManagerLocal tml) {
        this.tml = tml;
    }

    /**
     * @return the tripService
     */
    public TripServiceLocal getTripService() {
        return tripService;
    }

    /**
     * @param tripService the tripService to set
     */
    public void setTripService(TripServiceLocal tripService) {
        this.tripService = tripService;
    }

    /**
     * @return the statisticService
     */
    public StatisticServiceLocal getStatisticService() {
        return statisticService;
    }

    /**
     * @param statisticService the statisticService to set
     */
    public void setStatisticService(StatisticServiceLocal statisticService) {
        this.statisticService = statisticService;
    }
}
