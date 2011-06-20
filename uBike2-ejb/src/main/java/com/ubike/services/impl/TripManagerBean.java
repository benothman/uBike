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
import java.util.List;
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
    @Override
    public void addTrip(Trip trip) {
        addEntity(trip);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#addSegment(com.ubike.entities.TripSegment)
     */
    @Override
    public void addSegment(TripSegment segment) {
        addEntity(segment);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#addGPSFile(com.ubike.entities.GpsFile)
     */
    @Override
    public void addGPSFile(GpsFile file) {
        addEntity(file);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#addRoute(com.ubike.entities.Route)
     */
    @Override
    public void addRoute(Route route) {
        addEntity(route);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#addStatistic(com.ubike.model.Statistic)
     */
    @Override
    public void addStatistic(Statistic stat) {
        addEntity(stat);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getUserTrips(java.lang.Long)
     */
    @Override
    public List<Trip> getUserTrips(long userId) {
        try {
            return (List<Trip>) manager.createNamedQuery("Trip.getUserTrips").setParameter(
                    "ownerId", userId).getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<Trip>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getUserTripsByDate(java.lang.Long)
     */
    @Override
    public List<Trip> getUserTripsByDate(long userId, Date sDate, Date eDate) {
        try {
            return (List<Trip>) manager.createNamedQuery("Trip.getUserTripByDate").setParameter(
                    "ownerId", userId).setParameter("sDate", sDate).setParameter("eDate", eDate).getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<Trip>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getTripSegments(java.lang.Long)
     */
    @Override
    public List<TripSegment> getTripSegments(long tripId) {
        try {
            return (List<TripSegment>) manager.createNamedQuery(
                    "TripSegment.getByTrip").setParameter(
                    "tripId", tripId).getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<TripSegment>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getFileByName(java.lang.String)
     */
    @Override
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
    @Override
    public Trip getTripById(long tripId) {
        return manager.find(Trip.class, tripId);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatByType(com.ubike.util.StatisticType)
     */
    @Override
    public List<Statistic> getStatByType(StatisticType type) {
        try {
            return manager.createNamedQuery("Statistic.getByType").setParameter("param", type).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatByMetric(com.ubike.util.Metric)
     */
    @Override
    public List<Statistic> getStatByMetric(Metric metric) {
        try {
            return manager.createNamedQuery("Statistic.getByMetric").setParameter("param", metric).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatByInterval(java.util.Date, java.util.Date)
     */
    @Override
    public List<Statistic> getStatByInterval(Date start, Date end) {
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
    @Override
    public List<Statistic> getEntityStatByInterval(long entityId, Date start, Date end) {
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
    @Override
    public List<Statistic> getStatAfterDate(Date date) {
        try {
            return manager.createNamedQuery("Statistic.getAfter").setParameter("start", date).getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<Statistic>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#getStatBeforDate(java.util.Date)
     */
    @Override
    public List<Statistic> getStatBeforDate(Date date) {
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
    @Override
    public List<Statistic> getStatByData(Metric metric, StatisticType type, Date start, Date end) {
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
    @Override
    public List<Statistic> getEntityStat(long entityId) {
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
    @Override
    public List<Statistic> getEntityStatByIntervalAndType(long entityId, StatisticType type, Date start, Date end) {
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
    @Override
    public List<Statistic> getEntityStatAfterByType(long entityId, StatisticType type, Date start) {
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
    @Override
    public List<Statistic> getEntityStatByType(long entityId, StatisticType type) {
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
    @Override
    public List<Statistic> getEntityStatByMetric(long entityId, Metric metric) {
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
    @Override
    public List<Statistic> getEntityStatByMetricAndType(long entityId, Metric metric, StatisticType type) {
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
    @Override
    public void insertEntity(Object entity) {
        manager.persist(entity);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#flush()
     */
    @Override
    public void flush() {
        manager.flush();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.TripManagerLocal#updateEntity(java.lang.Object)
     */
    @Override
    public void updateEntity(Object o) {
        try {
            System.out.println("TripManagerBean#updateEntity( " + o + " )");
            manager.merge(o);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.manager = entityManager;
    }
}
