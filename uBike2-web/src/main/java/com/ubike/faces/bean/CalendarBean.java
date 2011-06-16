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
import com.ubike.util.UbikeEntity;
import com.ubike.util.StatisticManager;
import com.ubike.services.TripManagerLocal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

/**
 * {@code CalendarBean}
 * <p></p>
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "calendarBean")
@ViewScoped
public class CalendarBean {

    private boolean popup;
    private boolean showApply;
    private String pattern;
    private Date selectedDate;
    private Locale locale;
    private SpeedStatisticBean speedStat;
    private DistanceStatisticBean distStat;
    private DurationStatisticBean durStat;
    private Calendar cal;

    /**
     * Create a new instance of {@code CalendarBean}
     */
    public CalendarBean() {
        super();
    }

    @PostConstruct
    protected void init() {
        this.popup = true;
        this.showApply = true;
        this.pattern = "MMM d, yyyy";
        cal = Calendar.getInstance();
        this.selectedDate = cal.getTime();
        this.locale = Locale.US;
        this.speedStat = new SpeedStatisticBean();
        this.durStat = new DurationStatisticBean();
        this.distStat = new DistanceStatisticBean();
    }

    /**
     * Filter the statistics by date and extract the daily, weekly, monthly yearly
     * and general statistics
     */
    public void filter(ValueChangeEvent event) {
        try {
            TripManagerLocal tml = (TripManagerLocal) BaseBean.getSessionAttribute("tml");
            UbikeEntity client = (UbikeEntity) BaseBean.getSessionAttribute("client");
            StatisticManager statMan = new StatisticManager(tml);
            cal.setTime(selectedDate);
            Collection<Statistic> today = statMan.getDailyStat(client, selectedDate);
            Collection<Statistic> thisWeek = statMan.getWeeklyStat(client, selectedDate);
            Collection<Statistic> thisMonth = statMan.getMonthlyStat(client, selectedDate);
            Collection<Statistic> thisYear = statMan.getYearlyStat(client, selectedDate);
            Collection<Statistic> general = statMan.getGeneralStat(client);
            cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR) - 1);
            Collection<Statistic> lastWeek = statMan.getWeeklyStat(client, cal.getTime());
            cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR) + 1);
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
            Collection<Statistic> lastMonth = statMan.getMonthlyStat(client, cal.getTime());
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
            Collection<Statistic> lastYear = statMan.getYearlyStat(client, cal.getTime());
            BaseBean.setSessionAttribute("today", today);
            BaseBean.setSessionAttribute("thisWeek", thisWeek);
            BaseBean.setSessionAttribute("lastWeek", lastWeek);
            BaseBean.setSessionAttribute("thisMonth", thisMonth);
            BaseBean.setSessionAttribute("lastMonth", lastMonth);
            BaseBean.setSessionAttribute("thisYear", thisYear);
            BaseBean.setSessionAttribute("lastYear", lastYear);
            BaseBean.setSessionAttribute("general", general);
            BaseBean.setSessionAttribute("filter", Boolean.TRUE);

            this.setSpeedStat(new SpeedStatisticBean());
            this.setDurStat(new DurationStatisticBean());
            this.setDistStat(new DistanceStatisticBean());

        } catch (Exception exp) {
        }
    }

    /**
     * 
     * @param event
     */
    public void selectLocale(ValueChangeEvent event) {

        String str = (String) event.getNewValue();

        if (str.equals("en/US")) {
            this.locale = Locale.US;
        } else if (str.equals("fr/FR")) {
            this.locale = Locale.FRANCE;
        } else if (str.equals("de/DE")) {
            this.locale = Locale.GERMANY;
        }
    }

    /**
     * @return the popup
     */
    public boolean isPopup() {
        return popup;
    }

    /**
     * @param popup the popup to set
     */
    public void setPopup(boolean popup) {
        this.popup = popup;
    }

    /**
     * @return the showApply
     */
    public boolean isShowApply() {
        return showApply;
    }

    /**
     * @param showApply the showApply to set
     */
    public void setShowApply(boolean showApply) {
        this.showApply = showApply;
    }

    /**
     * @return the pattern
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * @param pattern the pattern to set
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * @return the selectedDate
     */
    public Date getSelectedDate() {
        return selectedDate;
    }

    /**
     * @param selectedDate the selectedDate to set
     */
    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    /**
     * @return the local
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param local the local to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @return the speedStat
     */
    public SpeedStatisticBean getSpeedStat() {
        return speedStat;
    }

    /**
     * @param speedStat the speedStat to set
     */
    public void setSpeedStat(SpeedStatisticBean speedStat) {
        this.speedStat = speedStat;
    }

    /**
     * @return the distStat
     */
    public DistanceStatisticBean getDistStat() {
        return distStat;
    }

    /**
     * @param distStat the distStat to set
     */
    public void setDistStat(DistanceStatisticBean distStat) {
        this.distStat = distStat;
    }

    /**
     * @return the durStat
     */
    public DurationStatisticBean getDurStat() {
        return durStat;
    }

    /**
     * @param durStat the durStat to set
     */
    public void setDurStat(DurationStatisticBean durStat) {
        this.durStat = durStat;
    }
}
