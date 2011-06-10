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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * {@code TemplateBean}
 * <p></p>
 *
 * Created on Jun 10, 2011 at 11:42:02 AM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "templateBean")
@SessionScoped
public class TemplateBean {

    private String template;
    public static final String WELCOME_TEMPLATE = "welcome-template.xhtml";
    public static final String AUTHENTICATED_TEMPLATE = "ubike-template.xhtml";
    public static final String TRIP_TEMPLATE = "trip-template.xhtml";

    /** 
     * Creates a new instance of {@code TemplateBean} 
     */
    public TemplateBean() {
    }

    @PostConstruct
    protected void init() {
        this.template = WELCOME_TEMPLATE;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(String template) {
        this.template = template;
    }
}
