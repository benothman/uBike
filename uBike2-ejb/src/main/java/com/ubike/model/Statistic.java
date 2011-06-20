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

import com.ubike.util.Metric;
import com.ubike.util.StatisticType;
import com.ubike.util.Util;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * {@code Statistic}
 * <p>
 * This class represents a measurable value from the Users and/or Groups trips
 * There are many types of statistics such as Route statistics, Group Statistic,
 * User Statistic, etc...
 * A statistic still valid while there was not a new upload by a concerned user.
 * </p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Entity
@Table(name = "STATISTICS")
@NamedQueries({
    @NamedQuery(name = "Statistic.getAll", query = "SELECT o FROM Statistic o"),
    @NamedQuery(name = "Statistic.getByInterval", query = "SELECT o FROM Statistic o WHERE o.startDate>=:start AND o.endDate <=:end"),
    @NamedQuery(name = "Statistic.getAfter", query = "SELECT o FROM Statistic o WHERE o.startDate>=:start"),
    @NamedQuery(name = "Statistic.getBefore", query = "SELECT o FROM Statistic o WHERE o.endDate>=:end"),
    @NamedQuery(name = "Statistic.getByType", query = "SELECT o FROM Statistic o WHERE o.type=:param"),
    @NamedQuery(name = "Statistic.getByMetric", query = "SELECT o FROM Statistic o WHERE o.metric=:param"),
    @NamedQuery(name = "Statistic.getByData",
    query = "SELECT o FROM Statistic o WHERE o.metric=:metric AND o.type=:type AND o.startDate>=:start AND o.endDate <=:end")
})
@Inheritance(strategy = InheritanceType.JOINED)
public class Statistic implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "END_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Column(name = "VALID")
    private boolean valid;
    @Column(name = "STAT_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatisticType type;
    @Column(name = "STAT_METRIC", nullable = false)
    @Enumerated(EnumType.STRING)
    private Metric metric;
    @Column(name = "STAT_COUNT")
    private int count;
    @Column(name = "STAT_AVG_VALUE")
    private double avgValue;
    @Column(name = "STAT_MAX_VALUE")
    private double maxValue;
    @Column(name = "STAT_MIN_VALUE")
    private double minValue;
    @Column(name = "STAT_TOTAL")
    private double totalValue;

    /**
     * Create a new <code>Statistic</code> instance with no settings
     */
    public Statistic() {
        super();
    }

    /**
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return this.endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the valid
     */
    public boolean isValid() {
        return this.valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return the type
     */
    public StatisticType getType() {
        return this.type;
    }

    /**
     * @param type the type to set
     */
    public void setType(StatisticType type) {
        this.type = type;
    }

    /**
     * @return the metric
     */
    public Metric getMetric() {
        return this.metric;
    }

    /**
     * @param metric the metric to set
     */
    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return this.count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the avgValue
     */
    public double getAvgValue() {
        return this.avgValue;
    }

    /**
     * @param avgValue the avgValue to set
     */
    public void setAvgValue(double avgValue) {
        this.avgValue = avgValue;
    }

    /**
     * @return the maxValue
     */
    public double getMaxValue() {
        return this.maxValue;
    }

    /**
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @return the minValue
     */
    public double getMinValue() {
        return this.minValue;
    }

    /**
     * @param minValue the minValue to set
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    /**
     * @return the totalValue
     */
    public double getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue the totalValue to set
     */
    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    @Transient
    public String getStartDateAsString() {
        return this.startDate == null ? "N/A" : Util.formatDate(startDate);
    }

    @Transient
    public String getEndDateAsString() {
        return this.endDate == null ? "N/A" : Util.formatDate(endDate);
    }

    @Transient
    public String getMaxDurationAsString() {
        return Util.formatDuration((int) maxValue);
    }

    @Transient
    public String getMinDurationAsString() {
        return Util.formatDuration((int) minValue);
    }

    @Transient
    public String getAvgDurationAsString() {
        return Util.formatDuration((int) avgValue);
    }

    @Transient
    public String getTotalDurationAsString() {
        return Util.formatDuration((int) this.totalValue);
    }
}