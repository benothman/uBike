/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubike.services;

import com.ubike.model.GpsFile;
import com.ubike.model.Route;
import com.ubike.model.Statistic;
import com.ubike.model.Trip;
import com.ubike.model.TripSegment;
import com.ubike.util.Metric;
import com.ubike.util.StatisticType;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author BENOTHMAN Nabil.
 */
@Local
public interface TripManagerLocal {

    /**
     * @param trip
     */
    public void addTrip(Trip trip);

    /**
     * @param segment
     */
    public void addSegment(TripSegment segment);

    /**
     * @param file
     */
    public void addGPSFile(GpsFile file);

    /**
     * @param route
     */
    public void addRoute(Route route);

    /**
     *
     * @param stat
     */
    public void addStatistic(Statistic stat);

    /**
     * @param userId
     * @return
     */
    public List<Trip> getUserTrips(long userId);

    /**
     * 
     * @param userId
     * @param sDate
     * @param eDate
     * @return
     */
    public List<Trip> getUserTripsByDate(long userId, Date sDate, Date eDate);

    /**
     * @param tripId
     * @return
     */
    public List<TripSegment> getTripSegments(long tripId);

    /**
     * @param fileName
     * @return
     */
    public GpsFile getFileByName(String fileName);

    /**
     * 
     * @param tripId
     * @return
     */
    public Trip getTripById(long tripId);

    /**
     *
     * @param type
     * @return
     */
    public List<Statistic> getStatByType(StatisticType type);

    /**
     * 
     * @param metric
     * @return
     */
    public List<Statistic> getStatByMetric(Metric metric);

    /**
     * 
     * @param start
     * @param end
     * @return
     */
    public List<Statistic> getStatByInterval(Date start, Date end);

    /**
     * 
     * @param entityId
     * @param type
     * @param start
     * @param end
     * @return
     */
    public List<Statistic> getEntityStatByIntervalAndType(long entityId, StatisticType type, Date start, Date end);

    /**
     *
     * @param entityId
     * @param type
     * @param start
     * @return
     */
    public List<Statistic> getEntityStatAfterByType(long entityId, StatisticType type, Date start);

    /**
     *
     * @param entityId
     * @param start
     * @param end
     * @return
     */
    public List<Statistic> getEntityStatByInterval(long entityId, Date start, Date end);

    /**
     *
     * @param date
     * @return
     */
    public List<Statistic> getStatAfterDate(Date date);

    /**
     *
     * @param date
     * @return
     */
    public List<Statistic> getStatBeforDate(Date date);

    /**
     * 
     * @param metric
     * @param type
     * @param start
     * @param end
     * @return
     */
    public List<Statistic> getStatByData(Metric metric, StatisticType type, Date start, Date end);

    /**
     * 
     * @param entityId
     * @return
     */
    public List<Statistic> getEntityStat(long entityId);

    /**
     * 
     * @param entityId
     * @param type
     * @return
     */
    public List<Statistic> getEntityStatByType(long entityId, StatisticType type);

    /**
     *
     * @param entityId
     * @param metric
     * @return
     */
    public List<Statistic> getEntityStatByMetric(long entityId, Metric metric);

    /**
     * 
     * @param entityId
     * @param type
     * @return
     */
    public List<Statistic> getEntityStatByMetricAndType(long entityId, Metric metric, StatisticType type);

    /**
     * 
     * @param entity
     */
    public void insertEntity(Object entity);

    /**
     *
     */
    public void flush();

    /**
     * @param o
     */
    public void updateEntity(Object o);

    /**
     * 
     */
    public void setEntityManager(EntityManager entityManager);
}
