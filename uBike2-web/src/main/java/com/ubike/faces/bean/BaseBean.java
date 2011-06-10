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

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * {@code BaseBean}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "baseBean")
@SessionScoped
public class BaseBean {

    protected static final Logger logger = Logger.getLogger(BaseBean.class.getName());
    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";
    private String template;

    /**
     * Create a new instance of {@code BaseBean}
     */
    public BaseBean() {
        super();
    }

    @PostConstruct
    protected void init() {
        this.template = "";
    }

    /**
     * @param name The name of the session attribute
     * @return The session attribute related to the given name
     */
    public static Object getSessionAttribute(String name) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        return attrs.getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * Add a session attribute and link it with the given name
     *
     * @param name The name of the session attribute
     * @param attribute The session attributze to be added
     */
    public static void setSessionAttribute(String name, Object attribute) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        attrs.setAttribute(name, attribute, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * Remove the session attribute related to thhe given name
     * @param name The name of the session attribute to be removed
     */
    public static void removeSessionAttribute(String name) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        attrs.removeAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    /**
     * Write the given error message to the log console
     * @param msg The error message to be written into the log console
     */
    public static void logError(String msg) {
        logger.severe(msg);
    }

    /**
     * Write the given information message to the log console
     * @param msg The information message to be written into the log console
     */
    public static void logInfo(String msg) {
        logger.info(msg);
    }
}
