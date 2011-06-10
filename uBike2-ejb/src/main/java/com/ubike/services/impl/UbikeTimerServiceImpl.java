/*
 *  Copyright 2009 Nabil BENOTHMAN <nabil.benothman@gmail.com>.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 *
 *  This class is a part of uBike projet (HEIG-VD)
 */
package com.ubike.services.impl;

import com.ubike.services.UbikeTimerService;
import java.util.Calendar;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

/**
 *
 * @author Benothman
 */
@Stateless
public class UbikeTimerServiceImpl implements UbikeTimerService {

    @Resource
    private TimerService timerService;

    /**
     * 
     */
    public UbikeTimerServiceImpl() {
        createTimers();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UbikeTimerService#createTimers()
     */
    public void createTimers() {

        Calendar c = Calendar.getInstance();
        //long day = 24 * 60 * 60 * 1000;
        long day = 30 * 1000;
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        timerService.createTimer(c.getTimeInMillis(), day, "DAILY");
        c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR) + 1);
        timerService.createTimer(c.getTimeInMillis(), day * 7, "WEEKLY");
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        timerService.createTimer(c.getTimeInMillis(), day * 30, "MONTHLY");
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) + 1);
        timerService.createTimer(c.getTimeInMillis(), day * 30 * 12, "YEARLY");
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UbikeTimerService#updateDailyStat(javax.ejb.Timer)
     */
    @Timeout
    public void updateDailyStat(Timer timer) {
        String name = (String) timer.getInfo();
        if (name.equals("DAILY")) {
            // TODO
            System.out.println("com.ubike.services.UbikeTimerServiceImpl#updateDailyStat(javax.ejb.Timer)");
        }
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UbikeTimerService#updateWeeklyStat(javax.ejb.Timer)
     */
    @Timeout
    public void updateWeeklyStat(Timer timer) {
        String name = (String) timer.getInfo();
        if (name.equals("WEEKLY")) {
            // TODO
        }
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UbikeTimerService#updateMonthlyStat(javax.ejb.Timer)
     */
    @Timeout
    public void updateMonthlyStat(Timer timer) {
        String name = (String) timer.getInfo();
        if (name.equals("MONTHLY")) {
            // TODO
        }
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UbikeTimerService#updateYearlyStat(javax.ejb.Timer)
     */
    @Timeout
    public void updateYearlyStat(Timer timer) {
        String name = (String) timer.getInfo();
        if (name.equals("YEARLY")) {
            // TODO
        }
    }
}
