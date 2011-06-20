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

import com.ubike.model.MemberShip;
import com.ubike.services.MemberShipServiceLocal;
import com.ubike.util.CustomArrayList;
import com.ubike.util.CustomList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * {@code MembersBean}
 * <p/>
 *
 * Created on Jun 15, 2011 at 8:40:45 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "membersBean")
@RequestScoped
public class MembersBean extends AbstractBean {

    private CustomList<MemberShip> members;
    @EJB
    private MemberShipServiceLocal memberShipService;

    /**
     * Create a new instance of {@code MembersBean}
     */
    public MembersBean() {
        super();
    }

    @PostConstruct
    protected void init() {
        List<MemberShip> tmp = (List<MemberShip>) BaseBean.getSessionAttribute("tmp_members");
        if (tmp == null) {
            this.members = new CustomArrayList<MemberShip>();
        } else {
            this.members = new CustomArrayList<MemberShip>(tmp);
        }
    }

    @PreDestroy
    protected void destroy() {
        this.members = null;
    }

    public boolean getPaginate() {
        return this.members.size() > 10;
    }

    /**
     * @return the members
     */
    public CustomList<MemberShip> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(CustomList<MemberShip> members) {
        this.members = members;
    }
}
