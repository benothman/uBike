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
package com.ubike.services.jms;

import com.ubike.model.UbikeUser;
import com.ubike.processor.RawFileProcessor;
import com.ubike.services.GPSFileServiceLocal;
import com.ubike.services.StatisticServiceLocal;
import com.ubike.services.TripManagerLocal;
import com.ubike.services.TripServiceLocal;
import com.ubike.services.UserServiceLocal;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * {@code UbikeMessageBean}
 * <p/>
 *
 * Created on Jun 17, 2011 at 11:02:45 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@MessageDriven(mappedName = "jms/uBikeQueue", name = "jms/uBikeQueue", activationConfig = {
    @ActivationConfigProperty(propertyName = "subscriptionDurability", propertyValue = "Durable"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class UbikeMessageBean implements MessageListener {

    private static final Logger logger = Logger.getLogger(UbikeMessageBean.class.getName());
    @Resource
    private MessageDrivenContext mdc;
    @EJB
    private TripManagerLocal tripManager;
    @EJB
    private TripServiceLocal tripService;
    @EJB
    private GPSFileServiceLocal gpsFileService;
    @EJB
    private StatisticServiceLocal statisticService;
    @EJB
    private UserServiceLocal userService;

    /**
     * Create a new instance of {@code UbikeMessageBean}
     */
    public UbikeMessageBean() {
        super();
    }

    @Override
    public void onMessage(Message message) {
        logger.log(Level.INFO, "New message received -> {0}", message);
        if (message instanceof ObjectMessage) {
            try {
                ObjectMessage objectMessage = (ObjectMessage) message;
                FileMessage fileMessage = (FileMessage) objectMessage.getObject();
                Long userId = fileMessage.getUserId();
                String absolutePath = fileMessage.getAbsolutePath();
                UbikeUser u = userService.findWithTrips(userId);
                File my_file = new File(absolutePath);
                if (my_file.getName().endsWith(".xml")) {
                    // TODO
                } else if (my_file.getName().matches(".+[\\.][tT][xX][tT]")
                        || my_file.getName().matches(".+[\\.][nN][mM][eE][aA]")) {
                    RawFileProcessor processor = RawFileProcessor.create(u);
                    processor.setGpsFileService(gpsFileService);
                    processor.setTripService(tripService);
                    processor.setStatisticService(statisticService);
                    processor.setTml(tripManager);
                    processor.setUserService(userService);
                    logger.log(Level.INFO, "Starting processing file : {0}", absolutePath);
                    processor.process(new File(absolutePath));
                    logger.log(Level.INFO, "Ending processing file : {0}", absolutePath);
                }
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error occurs", ex);
            }
        }
    }
}
