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

import com.ubike.services.MessageServiceLocal;
import java.io.Serializable;
import java.util.logging.Level;
import javax.jms.Queue;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * {@code JmsMessageProducer}
 * <p/>
 *
 * Created on Jun 17, 2011 at 11:01:43 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
public class JmsMessageProducer implements MessageServiceLocal {

    private static final Logger logger = Logger.getLogger(JmsMessageProducer.class.getName());
    @Resource
    private SessionContext sc;
    @Resource(mappedName = "jms/uBikeCF")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "jms/uBikeQueue")
    private Queue queue;
    private Connection connection;

    /**
     * Create a new instance of {@code JmsMessageProducer}
     */
    public JmsMessageProducer() {
        super();
    }

    @PostConstruct
    protected void makeConnection() throws JMSException {
        logger.log(Level.INFO, "Create JMS connection");
        connection = connectionFactory.createConnection();
        logger.log(Level.INFO, "JMS connection created successfully");
    }

    @PreDestroy
    protected void endConnection() throws JMSException {
        logger.log(Level.INFO, "Ending  JMS connection");
        connection.close();
        logger.log(Level.INFO, "JMS connection closed successfully");
    }

    @Override
    public void sendMessage(Message message) throws JMSException {
        logger.log(Level.INFO, "Sending message : {0}", message);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer producer = session.createProducer(queue);
        producer.send(message);
    }

    @Override
    public void sendObjectMessage(Serializable obj) throws JMSException {
        logger.log(Level.INFO, "Sending object message : {0}", obj);
        logger.log(Level.INFO, "Create JMS Session");
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        logger.log(Level.INFO, "Create JMS Message producer");
        MessageProducer producer = session.createProducer(queue);
        logger.log(Level.INFO, "Create JMS Object Message");
        ObjectMessage om = session.createObjectMessage();
        om.setObject(obj);
        logger.log(Level.INFO, "Sending JMS Message");
        producer.send(om);
        logger.log(Level.INFO, "JMS Message sent with success");
    }
}
