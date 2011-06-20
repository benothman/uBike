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

import com.ubike.model.UbikeUser;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@code UbikePanelMenu}
 * <p></p>
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "panelMenu")
@RequestScoped
public class UbikePanelMenu extends AbstractBean {

    private UbikeUser current;

    /**
     * Create a new <code>UbikePanelMenu</code> instance
     */
    public UbikePanelMenu() {
        super();
    }

    @PostConstruct
    public void init() {
        this.current = (UbikeUser) BaseBean.getSessionAttribute("user");
    }

    /**
     * Check the action id sent by the user and redirect to the good page.
     * This method is related to the user data.
     */
    public String myData() {

        String string = "";
        try {
            ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exctx.getRequest();
            string = request.getParameter("action");
            if (string.equals("user-profile")) {
                if (this.current == null) {
                    return BaseBean.LOGIN_REQUIRED;
                }
                String url = exctx.getRequestContextPath() + "/resources/users/" + this.current.getId() + "/";
                HttpServletResponse response = (HttpServletResponse) exctx.getResponse();
                response.sendRedirect(url);
            }
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "Error while retrieving data", exp);
        }
        return string;
    }

    /**
     * Check the action id sent by the user and redirect to the good page.
     * This method is related to the user trips.
     */
    public String myBuddies() {

        String string = "";

        try {
            ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exctx.getRequest();
            String param = request.getParameter("action");

            if (param.equals("user-trips")) {
                if (this.current == null) {
                    return BaseBean.LOGIN_REQUIRED;
                }
                String url = exctx.getRequestContextPath() + "/resources/users/" + this.current.getId() + "/trips/";
                HttpServletResponse response = (HttpServletResponse) exctx.getResponse();
                response.sendRedirect(url);
            }

            return param;
        } catch (Exception exp) {
            BaseBean.logError(exp.getMessage());
        }

        return string;
    }

    /**
     * Check the action id sent by the user and redirect to the good page.
     * This method is related to the user community.
     */
    public String myCommunity() {

        try {
            ExternalContext exCtx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exCtx.getRequest();
            String param = request.getParameter("action");
            int action = -1;
            try {
                action = Integer.parseInt(param);
            } catch (NumberFormatException nfe) {
                logger.log(Level.SEVERE, "Unknown request parameter : " + param, nfe);
                return param;
            }

            String url = exCtx.getRequestContextPath();
            HttpServletResponse response = (HttpServletResponse) exCtx.getResponse();
            if (action == 3) {
                response.sendRedirect(url + "/resources/groups/");
                return "";
            }

            if (this.current == null) {
                return BaseBean.LOGIN_REQUIRED;
            }

            url += "/resources/users/" + this.current.getId();
            boolean error = false;
            switch (action) {
                case 1:
                    url += "/memberShips/";
                    break;
                case 2:
                    url += "/friends/";
                    break;
                default:
                    error = true;
                    logger.log(Level.SEVERE, "Action not recognized : {0}", action);
                    break;
            }

            if (!error) {
                response.sendRedirect(url);
            }
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "Error occurs while processing user action", exp);
        }
        return "";
    }

    /**
     * Check the action id sent by the user and redirect to the good page.
     * This method is related to an additional links
     */
    public String additional() {
        try {
            ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exctx.getRequest();
            String param = request.getParameter("action");
            return param;
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "Error while reteiving request parameter", exp);
            return "";
        }
    }

    /**
     * @return the current
     */
    public UbikeUser getCurrent() {
        return current;
    }

    /**
     * @param current the current to set
     */
    public void setCurrent(UbikeUser current) {
        this.current = current;
    }
}
