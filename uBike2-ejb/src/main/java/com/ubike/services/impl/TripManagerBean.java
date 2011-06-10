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
package com.ubike.services.impl;

import com.ubike.model.GpsFile;
import com.ubike.model.Route;
import com.ubike.model.Statistic;
import com.ubike.model.Trip;
import com.ubike.model.TripSegment;
import com.ubike.services.TripManagerLocal;
import com.ubike.util.Metric;
import com.ubike.util.StatisticType;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author BENOTHMAN Nabil.
 */
@Stateless
public class TripManagerBean implements TripManagerLocal {

    @PersistenceContext(unitName = "ubikeEJB")
    private EntityManager manager;

    /**
     * Persist the given entity in the database
     * @param entity The entity to be persisted
     */
    private void addEntity(Object entity) {
        manager.persist(entity);
        manager.flush();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#addTrip(com.ubike.entities.Trip)
     */
    public void addTrip(Trip trip) {
        addEntity(trip);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#addSegment(com.ubike.entities.TripSegment)
     */
    public void addSegment(TripSegment segment) {
        addEntity(segment);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#addGPSFile(com.ubike.entities.GpsFile)
     */
    public void addGPSFile(GpsFile file) {
        addEntity(file);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#addRoute(com.ubike.entities.Route)
     */
    public void addRoute(Route route) {
        addEntity(route);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#addStatistic(com.ubike.model.Statistic)
     */
    public void addStatistic(Statistic stat) {
        addEntity(stat);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getUserTrips(java.lang.Long)
     */
    public Collection<Trip> getUserTrips(long userId) {
        try {
            return (Collection<Trip>) manager.createNamedQuery("Trip.getUserTrips").setParameter(
                    "ownerId", userId).getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<Trip>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getUserTripsByDate(java.lang.Long)
     */
    public Collection<Trip> getUserTripsByDate(long userId, Date sDate, Date eDate) {
        try {
            return (Collection<Trip>) manager.createNamedQuery("Trip.getUserTripByDate").setParameter(
                    "ownerId", userId).setParameter("sDate", sDate).setParameter("eDate", eDate).getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<Trip>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getTripSegments(java.lang.Long)
     */
    public Collection<TripSegment> getTripSegments(long tripId) {
        try {
            return (Collection<TripSegment>) manager.createNamedQuery(
                    "TripSegment.getByTrip").setParameter(
                    "tripId", tripId).getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<TripSegment>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getFileByName(java.lang.String)
     */
    public GpsFile getFileByName(String fileName) {
        try {
            return (GpsFile) manager.createNamedQuery("GPSFILE.getByName").
                    setParameter("param", fileName).getSingleResult();
        } catch (Exception exp) {
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getTripById(java.lang.Long)
     */
    public Trip getTripById(long tripId) {
        return manager.find(Trip.class, tripId);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatByType(com.ubike.util.StatisticType)
     */
    public Collection<Statistic> getStatByType(StatisticType type) {
        try {
            return manager.createNamedQuery("Statistic.getByType").setParameter("param", type).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatByMetric(com.ubike.util.Metric)
     */
    public Collection<Statistic> getStatByMetric(Metric metric) {
        try {
            return manager.createNamedQuery("Statistic.getByMetric").setParameter("param", metric).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatByInterval(java.util.Date, java.util.Date)
     */
    public Collection<Statistic> getStatByInterval(Date start, Date end) {
        try {
            return manager.createNamedQuery("Statistic.getByInterval").
                    setParameter("start", start).setParameter("end", end).getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatByInterval(java.lang.Long,java.util.Date, java.util.Date)
     */
    public Collection<Statistic> getEntityStatByInterval(long entityId, Date start, Date end) {
        try {
            return manager.createNamedQuery("Statistic.getEntityStatByInterval").setParameter("entityId", entityId).
                    setParameter("start", start).setParameter("end", end).getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatAfterDate(java.util.Date)
     */
    public Collection<Statistic> getStatAfterDate(Date date) {
        try {
            return manager.createNamedQuery("Statistic.getAfter").setParameter("start", date).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatBeforDate(java.util.Date)
     */
    public Collection<Statistic> getStatBeforDate(Date date) {
        try {
            return manager.createNamedQuery("Statistic.getBefore").setParameter("end", date).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatByData(com.ubike.util.Metric,
     *          com.ubike.util.StatisticType,java.util.Date,java.util.Date)
     */
    public Collection<Statistic> getStatByData(Metric metric, StatisticType type, Date start, Date end) {
        try {
            return manager.createNamedQuery("Statistic.getByData").setParameter("type", type).
                    setParameter("metric", metric).setParameter("start", start).
                    setParameter("end", end).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getEntityStat(java.lang.Long)
     */
    public Collection<Statistic> getEntityStat(long entityId) {
        try {
            return manager.createNamedQuery("Statistic.getEntityStat").setParameter("entityId", entityId).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getEntityStatByIntervalAndType(java.lang.Long,
     *          com.ubike.util.StatisticType,java.util.Date,java.util.Date)
     */
    public Collection<Statistic> getEntityStatByIntervalAndType(long entityId, StatisticType type, Date start, Date end) {
        try {
            return manager.createNamedQuery("Statistic.getEntityStatByIntervalAndType").
                    setParameter("entityId", entityId).setParameter("type", type).setParameter("start", start).
                    setParameter("end", end).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getEntityStatAfterByType(java.lang.Long,
     *          com.ubike.util.StatisticType,java.util.Date)
     */
    public Collection<Statistic> getEntityStatAfterByType(long entityId, StatisticType type, Date start) {
        try {
            return manager.createNamedQuery("Statistic.getEntityStatAfterByType").
                    setParameter("entityId", entityId).setParameter("type", type).
                    setParameter("start", start).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getEntityStatByType(java.lang.Long)
     */
    public Collection<Statistic> getEntityStatByType(long entityId, StatisticType type) {
        try {
            return manager.createNamedQuery("Statistic.getEntityStatByType").
                    setParameter("entityId", entityId).setParameter("type", type).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getEntityStatByMetric(java.lang.Long)
     */
    public Collection<Statistic> getEntityStatByMetric(long entityId, Metric metric) {
        try {
            return manager.createNamedQuery("Statistic.getEntityStatByMetric").
                    setParameter("entityId", entityId).setParameter("metric", metric).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getEntityStatByMetricAndType(java.lang.Long)
     */
    public Collection<Statistic> getEntityStatByMetricAndType(long entityId, Metric metric, StatisticType type) {
        try {
            return manager.createNamedQuery("Statistic.getEntityStatByMetricAndType").
                    setParameter("entityId", entityId).setParameter("metric", metric).
                    setParameter("type", type).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#insertEntity(java.lang.Object)
     */
    public void insertEntity(Object entity) {
        manager.persist(entity);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#flush()
     */
    public void flush() {
        manager.flush();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#updateEntity(java.lang.Object)
     */
    public void updateEntity(Object o) {
        try {
            System.out.println("TripManagerBean#updateEntity( " + o + " )");
            manager.merge(o);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }
}