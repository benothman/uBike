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

import com.ubike.model.Statistic;
import com.ubike.util.CustomArrayList;
import com.ubike.util.CustomList;
import com.ubike.util.Metric;
import com.ubike.util.StatisticType;
import java.util.List;

/**
 * {@code StatisticBean}
 * <p/>
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public abstract class StatisticBean extends AbstractBean {

    private CustomList<Statistic> stats;
    private Statistic generalStat;

    /**
     * 
     */
    public StatisticBean() {
        this.stats = new CustomArrayList<Statistic>();
    }

    /**
     * 
     * @param metric
     */
    public StatisticBean(Metric metric) {
        this();
        // Asserting today statistics
        List<Statistic> daily = (List<Statistic>) BaseBean.getSessionAttribute("today");
        extractStat(daily, metric);

        // Asserting this week statistics
        List<Statistic> weekly = (List<Statistic>) BaseBean.getSessionAttribute("thisWeek");
        extractStat(weekly, metric);
        // Asserting last week statistics
        List<Statistic> lastWeekly = (List<Statistic>) BaseBean.getSessionAttribute("lastWeek");
        extractStat(lastWeekly, metric);
        // Asserting this month statistics
        List<Statistic> monthly = (List<Statistic>) BaseBean.getSessionAttribute("thisMonth");
        extractStat(monthly, metric);
        // Asserting last month statistics
        List<Statistic> lastMonthly = (List<Statistic>) BaseBean.getSessionAttribute("lastMonth");
        extractStat(lastMonthly, metric);

        // Asserting this year statistics
        List<Statistic> yearly = (List<Statistic>) BaseBean.getSessionAttribute("thisYear");
        extractStat(yearly, metric);
        
        // Asserting last year statistics
        List<Statistic> lastYearly = (List<Statistic>) BaseBean.getSessionAttribute("lastYear");
        extractStat(lastYearly, metric);

        // Asserting global statistics
        List<Statistic> generaly = (List<Statistic>) BaseBean.getSessionAttribute("general");
        assertGeneralStat(generaly, metric);
    }

    /**
     * Extract the statistics with the metric from the given list
     * @param statistics The list of statistics
     * @param metric The metric of statistics to be extracted
     */
    private void extractStat(List<Statistic> statistics, Metric metric) {
        for (Statistic o : statistics) {
            if (o.getMetric() == metric) {
                if (o.getType() == StatisticType.GENERAL) {
                    o.setStartDate(null);
                    o.setEndDate(null);
                }
                this.getStats().add(o);
                break;
            }
        }
    }

    /**
     *
     * @param statistics
     * @param metric
     */
    private void assertGeneralStat(List<Statistic> statistics, Metric metric) {
        for (Statistic o : statistics) {
            if (o.getMetric() == metric) {
                if (o.getType() == StatisticType.GENERAL) {
                    o.setStartDate(null);
                    o.setEndDate(null);
                }
                this.generalStat = o;
                break;
            }
        }

    }

    /**
     * @return the stats
     */
    public CustomList<Statistic> getStats() {
        return stats;
    }

    /**
     * @param stats the stats to set
     */
    public void setStats(CustomList<Statistic> stats) {
        this.stats = stats;
    }

    /**
     * @return the generalStat
     */
    public Statistic getGeneralStat() {
        return generalStat;
    }

    /**
     * @param generalStat the generalStat to set
     */
    public void setGeneralStat(Statistic generalStat) {
        this.generalStat = generalStat;
    }
}
