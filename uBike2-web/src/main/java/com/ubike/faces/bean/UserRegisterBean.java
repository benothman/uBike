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

import com.ubike.faces.validator.FieldMatch;
import com.ubike.model.Account;
import com.ubike.model.UbikeUser;
import com.ubike.services.AccountServiceLocal;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;

/**
 * {@code UserRegisterBean}
 * <p>This class is used as managed bean for user registration</p>
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "registerBean")
@RequestScoped
@FieldMatch.List({
    @FieldMatch(type = UserRegisterBean.class, first = "keyPass", name = "registerBean",
    second = "confirm", message = "The password fields must match")
})
public class UserRegisterBean {

    private String progressString = "Fill the form please";
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
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
    @NotEmpty
    @Length(min = 7, message = "Password must contain at least 7 characters")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z]).{7,25}$", message = "The password is not valid")
    private String keyPass;
    @NotBlank
    private String confirm;
    @EJB
    private AccountServiceLocal accountService;

    /**
     * 
     */
    public UserRegisterBean() {
        super();
    }

    /**
     * 
     * @return
     */
    public String doRegistration() {

        FacesContext fc = FacesContext.getCurrentInstance();

        if (!this.keyPass.equals(this.confirm)) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Please verify your password !", "Please verify your password!"));

            return BaseBean.FAILURE;
        }

        UbikeUser user = new UbikeUser(this.firstName, this.lastName, this.address, this.phone, this.email);
        ShaPasswordEncoder shaPswdEncoder = new ShaPasswordEncoder(512);
        String encodedPassword = shaPswdEncoder.encodePassword(this.keyPass, username);
        Account account = accountService.createAccount(user, username, encodedPassword);

        if (account == null) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The username is already used! Please try again",
                    "The username is already used! Please try again"));

            return BaseBean.FAILURE;
        }

        return BaseBean.SUCCESS;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return this.address;
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
        return this.phone;
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
        return this.email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the keyPass
     */
    public String getKeyPass() {
        return this.keyPass;
    }

    /**
     * @param keyPass the keyPass to set
     */
    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    /**
     * @return the confirm
     */
    public String getConfirm() {
        return this.confirm;
    }

    /**
     * @param confirm the confirm to set
     */
    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    /**
     *
     */
    public void success() {
        setProgressString(getProgressString() + "(Strored successfully)");
    }

    /**
     *
     * @return
     */
    public String getProgressString() {
        return progressString;
    }

    /**
     *
     * @param progressString
     */
    public void setProgressString(String progressString) {
        this.progressString = progressString;
    }
}
