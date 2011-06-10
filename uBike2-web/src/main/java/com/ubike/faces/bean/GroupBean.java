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

import com.ubike.model.UbikeGroup;
import com.ubike.model.UbikeUser;
import com.ubike.services.UserManagerLocal;
import java.io.IOException;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * {@code GroupBean}
 * <p>This class is used as managed bean for group creation</p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "createGroup")
@RequestScoped
public class GroupBean {

    @NotEmpty
    @Length(min = 5, max = 50)
    private String name;
    @NotEmpty
    @Length(min = 6, max = 200)
    private String description;
    @EJB
    private UserManagerLocal manager;
    private String createError;

    /**
     * 
     */
    public GroupBean() {
        super();
    }

    /**
     * Create a group, if the user is not authenticated he will be redireceted
     * to the login page, else the group creation process will be done.
     *
     * @return
     */
    public String create() throws IOException {

        ExternalContext ctx = FacesContext.getCurrentInstance().getExternalContext();
        UbikeUser user = (UbikeUser) BaseBean.getSessionAttribute("user");

        UbikeGroup group = new UbikeGroup(this.name, this.description, user);
        if (!manager.addGroup(group)) {
            createError = "The group " + this.name + " is already exists! Please try again";
            return BaseBean.FAILURE;
        }

        BaseBean.logInfo("The group " + this.name + " whas been created with success!");
        this.createError = "";
        HttpServletResponse response = (HttpServletResponse) ctx.getResponse();
        response.sendRedirect(ctx.getRequestContextPath() + "/resources/groups/" + group.getId());
        return BaseBean.SUCCESS;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the manager
     */
    public UserManagerLocal getManager() {
        return manager;
    }

    /**
     * @param manager the manager to set
     */
    public void setManager(UserManagerLocal manager) {
        this.manager = manager;
    }

    /**
     * @return the createError
     */
    public String getCreateError() {
        return createError;
    }

    /**
     * @param createError the createError to set
     */
    public void setCreateError(String createError) {
        this.createError = createError;
    }
}
