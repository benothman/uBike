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
import com.ubike.services.UserManagerLocal;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;

/**
 * {@code EditProfileBean}
 * <p>This class is used as managed bean for profile edition</p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "editProfileBean")
@RequestScoped
public class EditProfileBean {

    // Data for Profile Edition
    @NotEmpty
    private String address;
    @NotEmpty
    @Length(min = 10, max = 17)
    @Pattern(regexp = "[0-9]+", message = "The phone number can contain only digits")
    private String phone;
    @Email
    @NotEmpty
    private String email;
    private boolean groupAccess;
    private boolean otherAccess;
    // Data for Password Edition
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    @Length(min = 6, max = 20)
    private String newPassword;
    @NotEmpty
    private String confirm;
    // Util Data
    private String error;
    private String success;
    @EJB
    private UserManagerLocal uml;

    /**
     * 
     */
    public EditProfileBean() {
        UbikeUser current = (UbikeUser) BaseBean.getSessionAttribute("user");
        this.address = current.getAddress();
        this.phone = current.getPhone();
        this.email = current.getEmail();
        this.groupAccess = current.getPreferences().isGroupAccess();
        this.otherAccess = current.getPreferences().isOthersAccess();
    }

    /**
     * Try to update the current logged user data.
     *
     * @return <tt>success</tt> if the processing finish with successfully else
     * <tt>failure</tt>
     */
    public String editProfile() {

        try {
            UbikeUser current = (UbikeUser) BaseBean.getSessionAttribute("user");
            current.setAddress(this.address);
            current.setPhone(this.phone);
            current.setEmail(this.email);
            current.getPreferences().setGroupAccess(this.groupAccess);
            current.getPreferences().setOthersAccess(this.otherAccess);
            getUml().updateEntity(current);
            getUml().updateEntity(current.getPreferences());

            ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) exctx.getResponse();
            response.sendRedirect(exctx.getRequestContextPath() + "/resources/users/" + current.getId());

            this.error = "";
            this.success = "You profile was updated successfully!";
            return BaseBean.SUCCESS;
        } catch (Exception exp) {
            this.error = "An error was occur! Please try again.";
            this.success = "";
            return BaseBean.FAILURE;
        }
    }

    /**
     *
     * @return
     */
    public String editPassword() {
        try {
            UbikeUser current = (UbikeUser) BaseBean.getSessionAttribute("user");

            ShaPasswordEncoder encoder = new ShaPasswordEncoder(512);
            String encodedPW = encoder.encodePassword(this.getOldPassword(), current.getAccount().getUsername());
            // Verify the old password
            if (!current.getAccount().getKeyPass().equals(encodedPW)) {
                this.success = "";
                this.error = "Wrong old password! Please try again";
                return BaseBean.FAILURE;
            }
            // Verify that the new password is typed correctly
            if (!this.newPassword.equals(confirm)) {
                this.success = "";
                this.error = "Please verify the new Password";
                return BaseBean.FAILURE;
            }

            String keyPass = encoder.encodePassword(getNewPassword(), current.getAccount().getUsername());
            current.getAccount().setKeyPass(keyPass);
            getUml().updateEntity(current.getAccount());

            ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletResponse response = (HttpServletResponse) exctx.getResponse();
            response.sendRedirect(exctx.getRequestContextPath() + "/resources/users/" + current.getId());

            this.error = "";
            this.success = "";
            return BaseBean.SUCCESS;
        } catch (Exception exp) {
            this.success = "";
            this.error = "An Error was occur! Please try again";
            return BaseBean.FAILURE;
        }
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the groupAccess
     */
    public boolean isGroupAccess() {
        return groupAccess;
    }

    /**
     * @param groupAccess the groupAccess to set
     */
    public void setGroupAccess(boolean groupAccess) {
        this.groupAccess = groupAccess;
    }

    /**
     * @return the otherAccess
     */
    public boolean isOtherAccess() {
        return otherAccess;
    }

    /**
     * @param otherAccess the otherAccess to set
     */
    public void setOtherAccess(boolean otherAccess) {
        this.otherAccess = otherAccess;
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

    /**
     * @return the oldPassword
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * @param oldPassword the oldPassword to set
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * @return the confirm
     */
    public String getConfirm() {
        return confirm;
    }

    /**
     * @param confirm the confirm to set
     */
    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
