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
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

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
public class EditProfileBean extends AbstractBean {

    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private String address;
    @NotEmpty
    @Length(min = 10, max = 17, message = "The phone number must have between 10 and 17 digits")
    @Pattern(regexp = "[0-9]+", message = "The phone number can contain only digits")
    private String phone;
    @Email
    @NotEmpty
    private String email;
    @NotBlank
    @Pattern(regexp = ".*[^\\s]", message = "The username cannot contain spaces")
    private String username;
    // Data for Profile Edition
    private boolean groupAccess;
    private boolean otherAccess;
    @EJB
    private UserServiceLocal userService;

    /**
     * Create a new instance of {@code EditProfileBean}
     */
    public EditProfileBean() {
        super();
    }

    @PostConstruct
    protected void init() {
        UbikeUser current = (UbikeUser) BaseBean.getSessionAttribute("user");
        this.firstname = current.getFirstname();
        this.lastname = current.getLastname();
        this.username = current.getAccount().getUsername();
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
    public void save() {

        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            UbikeUser current = (UbikeUser) BaseBean.getSessionAttribute("user");
            current.setAddress(this.address);
            current.setPhone(this.phone);
            current.setEmail(this.email);
            current.getPreferences().setGroupAccess(this.groupAccess);
            current.getPreferences().setOthersAccess(this.otherAccess);
            this.userService.update(current);
            fc.addMessage("edit_form:edit_error", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "You profile was updated successfully!",
                    "You profile was updated successfully!"));

        } catch (Exception exp) {
            fc.addMessage("edit_form:edit_error", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An error was occur! Please try again.",
                    "An error was occur! Please try again."));

            logger.log(Level.SEVERE, "An error was occur! Please try again -> " + exp.getMessage(), exp);
        }
    }

    /**
     * 
     * @param event 
     */
    public void save(ActionEvent event) {
        save();
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
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
