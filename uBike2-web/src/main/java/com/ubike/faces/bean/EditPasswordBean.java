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
import com.ubike.services.AccountServiceLocal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.providers.encoding.ShaPasswordEncoder;

/**
 * {@code EditPasswordBean}
 * <p/>
 *
 * Created on Jun 13, 2011 at 12:10:25 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "editPasswordBean")
@RequestScoped
public class EditPasswordBean extends AbstractBean {

    // Data for Password Edition
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    @Length(min = 7, message = "Password must contain at least 7 characters")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z]).{7,25}$", message = "The password is not valid")
    private String newPassword;
    @NotEmpty
    private String confirm;
    // Util Data
    @EJB
    private AccountServiceLocal accountService;

    /**
     * Create a new instance of {@code EditPasswordBean}
     */
    public EditPasswordBean() {
        super();
    }

    @PostConstruct
    protected void init() {
    }

    /**
     * Edit the user password 
     * @return 
     */
    public void save() {
        FacesContext fc = FacesContext.getCurrentInstance();
        try {
            UbikeUser current = (UbikeUser) BaseBean.getSessionAttribute("user");
            // Verify that the new password is typed correctly
            if (!this.newPassword.equals(confirm)) {
                fc.addMessage("edit_form:edit_status", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Please verify the new Password",
                        "Please verify the new Password"));
                return;
            }

            ShaPasswordEncoder encoder = new ShaPasswordEncoder(512);
            String encodedPW = encoder.encodePassword(this.oldPassword, current.getAccount().getUsername());
            // Verify the old password
            if (!current.getAccount().getPassword().equals(encodedPW)) {
                fc.addMessage("edit_form:edit_status", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Wrong old password! Please try again",
                        "Wrong old password! Please try again"));
                return;
            }

            String keyPass = encoder.encodePassword(getNewPassword(), current.getAccount().getUsername());
            current.getAccount().setPassword(keyPass);
            accountService.update(current.getAccount());
            fc.addMessage("edit_form:edit_status", new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Your password was updated successfully",
                    "Your password was updated successfully"));
        } catch (Exception exp) {
            fc.addMessage("edit_form:edit_status", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "An Error was occur! Please try again",
                    "An Error was occur! Please try again"));
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

    /**
     * @return the accountService
     */
    public AccountServiceLocal getAccountService() {
        return accountService;
    }

    /**
     * @param accountService the accountService to set
     */
    public void setAccountService(AccountServiceLocal accountService) {
        this.accountService = accountService;
    }
}
