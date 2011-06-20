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
import com.ubike.model.UbikeGroup;
import com.ubike.model.UbikeUser;
import com.ubike.services.GroupServiceLocal;
import com.ubike.services.MemberShipServiceLocal;
import com.ubike.util.Role;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * {@code UbikeJoinGroup}
 * <p></p>
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "joinGroup")
@ViewScoped
public class UbikeJoinGroup extends AbstractBean {

    @EJB
    private MemberShipServiceLocal memberShipService;
    @EJB
    private GroupServiceLocal groupService;

    /**
     * Permit to current user (i.e logged user) to join a group if he is not
     * already member.
     */
    public void join() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            UbikeUser current = (UbikeUser) BaseBean.getSessionAttribute("user");
            ExternalContext exctx = fc.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exctx.getRequest();
            long groupId = Long.parseLong(request.getParameter("groupId"));
            long userId = current.getId();
            MemberShip m = memberShipService.getMemberShip(userId, groupId);

            if (m != null) {
                fc.addMessage("join:join_status", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "You are already member of this group",
                        "You are already member of this group"));
                return;
            }

            UbikeGroup group = groupService.find(groupId);
            m = new MemberShip(current, group, Role.Member);
            memberShipService.create(m);
            fc.addMessage("join:join_status", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "You joined the group " + group.getName() + " successfully",
                    "You joined the group " + group.getName() + " successfully"));

            List<MemberShip> members = (List<MemberShip>) BaseBean.getSessionAttribute("tmp_members");
            if (members == null) {
                members = new ArrayList<MemberShip>();
                BaseBean.setSessionAttribute("tmp_members", members);
            }
            members.add(m);
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "An error occurs while joinning group", exp);
            fc.addMessage("join:join_status", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An error occurs while processing your request",
                    "An error occurs while processing your request"));
        }
    }

    /**
     * Permit to current user (i.e logged user) to leave a group if he is already
     * member.
     */
    public void leave() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            UbikeUser current = (UbikeUser) BaseBean.getSessionAttribute("user");
            ExternalContext exctx = fc.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exctx.getRequest();
            long groupId = Long.parseLong(request.getParameter("groupId"));
            long userId = current.getId();
            MemberShip memberShip = memberShipService.getMemberShip(userId, groupId);

            if (memberShip == null) {
                fc.addMessage("join:join_status", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "You are not member of this group yet",
                        "You are not member of this group yet"));
                return;
            }

            memberShipService.remove(memberShip.getId());
            fc.addMessage("join:join_status", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "You leaved the group successfully!",
                    "You leaved the group successfully!"));

            List<MemberShip> members = (List<MemberShip>) BaseBean.getSessionAttribute("tmp_members");
            members.remove(memberShip);

        } catch (Exception exp) {
            logger.log(Level.SEVERE, "An error occurs while leaving group", exp);
            fc.addMessage("join:join_status", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An error occurs while processing your request",
                    "An error occurs while processing your request"));
        }
    }
}
