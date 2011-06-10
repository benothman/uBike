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
import com.ubike.services.UserManagerLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
@RequestScoped
public class UbikeJoinGroup {

    private String error;
    private String success;
    @EJB
    private UserManagerLocal uml;

    /**
     * Permit to current user (i.e logged user) to join a group if he is not
     * already member.
     */
    public void join() {
        try {
            UbikeUser current = (UbikeUser) BaseBean.getSessionAttribute("user");
            ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exctx.getRequest();
            long groupId = Long.parseLong(request.getParameter("groupId"));

            UbikeGroup group = getUml().getGroupById(groupId);

            if (!getUml().addMember(group, current)) {
                this.error = "You are already member of this group";
                this.success = "";
                return;
            }

            MemberShip m = getUml().getMemberShip(current.getId(), groupId);

            List<MemberShip> members = (List<MemberShip>) BaseBean.getSessionAttribute("tmp_members");
            if (members != null) {
                members.add(m);
            } else {
                members = new ArrayList<MemberShip>();
                members.add(m);
                BaseBean.setSessionAttribute("tmp_members", members);
            }

            this.error = "";
            this.success = "You are joined the " + group.getName() + " group successfully!";
        } catch (Exception exp) {
            this.success = "";
            this.error = "An Error was occur! Please try again";
        }
    }

    /**
     * Permit to current user (i.e logged user) to leave a group if he is already
     * member.
     */
    public void leave() {
        try {
            UbikeUser current = (UbikeUser) BaseBean.getSessionAttribute("user");
            ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) exctx.getRequest();
            long groupId = Long.parseLong(request.getParameter("groupId"));

            MemberShip memberShip = getUml().getMemberShip(current.getId(), groupId);

            if (memberShip == null) {
                this.error = "You are not member of this group !";
                this.success = "";
                return;
            }

            UbikeGroup group = memberShip.getGroup();
            if (getUml().leaveGroup(memberShip)) {
                current.getMemberShips().remove(memberShip);

                for (MemberShip o : group.getMemberShips()) {
                    if (o.getId().equals(memberShip.getId())) {
                        memberShip = o;
                        break;
                    }
                }
                group.getMemberShips().remove(memberShip);

                this.error = "";
                this.success = "You are leaved the " + group.getName() + " group successfully!";
                BaseBean.setSessionAttribute("tmp_members", group.getMemberShips());
            } else {
                this.success = "";
                this.error = "An Error was occur! Please try again";
            }

        } catch (Exception exp) {
            this.success = "";
            this.error = "An Error was occur! Please try again";
            System.err.println("UbikeJoinGroup error -> " + exp);
        }

    }

    /**
     * @return the uml
     */
    public UserManagerLocal getUml() {
        return uml;
    }

    /**
     * @param uml the uml to set
     */
    public void setUml(UserManagerLocal uml) {
        this.uml = uml;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the success
     */
    public String getSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(String success) {
        this.success = success;
    }
}
