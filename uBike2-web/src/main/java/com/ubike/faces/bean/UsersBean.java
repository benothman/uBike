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
import com.ubike.services.UserServiceLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * {@code UsersBean}
 * <p/>
 *
 * Created on Jun 15, 2011 at 8:45:30 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "usersBean")
@RequestScoped
public class UsersBean {

    private List<UbikeUser> users;
    @EJB
    private UserServiceLocal userService;

    /**
     * Create a new instance of {@code UsersBean}
     */
    public UsersBean() {
        super();
    }

    @PostConstruct
    protected void init() {
        this.users = BaseBean.getSessionAttribute("tmp_users") != null
                ? (List<UbikeUser>) BaseBean.getSessionAttribute("tmp_users") : new ArrayList<UbikeUser>();
    }

    @PreDestroy
    protected void destroy() {
        this.users = null;
    }

    /**
     * @return the users
     */
    public List<UbikeUser> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<UbikeUser> users) {
        this.users = users;
    }
}
