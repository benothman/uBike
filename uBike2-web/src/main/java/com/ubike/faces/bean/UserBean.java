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

import com.ubike.model.Account;
import javax.ejb.EJB;
import com.ubike.services.UserManagerLocal;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * {@code UserBean}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public class UserBean extends AbstractBean {

    @NotEmpty
    @Length(min = 5, max = 12)
    @Pattern(regexp = ".*[^\\s]", message = "Username cannot contain spaces")
    private String userName;
    @NotEmpty
    @Length(min = 6, max = 20)
    private String password;
    @EJB
    private UserManagerLocal uml;
    private String loginError;
    private AuthenticationManager authenticationManager;

    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }

    /**
     * Creates a new instance of UserBean.
     * 
     * @param userName
     * @param password
     */
    public UserBean(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * Try to do the login for the given userName and password.
     *
     * @return The loign status, i.e if the login is valid return "succes" else
     *         "failure"
     */
    public String authenticate() throws Exception {

        try {
            Authentication auth = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(this.userName, this.password));
            if (auth.getPrincipal() != null) {
                System.out.println(auth.getPrincipal().getClass().getName());
            }
            if (auth.getCredentials() != null) {
                System.out.println(auth.getCredentials().getClass().getName());
            }

            auth.setAuthenticated(true);
            SecurityContext ctx = SecurityContextHolder.getContext();
            ctx.setAuthentication(auth);


            Account account = getUml().getByUserName(userName);
            ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();
            exctx.getSessionMap().put("user", account.getOwner());
            exctx.getRequestMap().put("username", account.getUsername());
            exctx.getSessionMap().put("role", "ROLE_USER");
            HttpServletResponse response = (HttpServletResponse) exctx.getResponse();
            response.sendRedirect("/uBikeREST-war/resources/users/" + account.getOwner().getId());
            return BaseBean.SUCCESS;
        } catch (Exception e) {
            this.loginError = "The username and/or password is not correct! Please try again";
            e.printStackTrace();
            return BaseBean.FAILURE;
        }
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "[" + this.userName + ", " + this.password + "]";
    }

    /**
     * @return the loginError
     */
    public String getLoginError() {
        return loginError;
    }

    /**
     * @param loginError the loginError to set
     */
    public void setLoginError(String loginError) {
        this.loginError = loginError;
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
     * @return the authenticationManager
     */
    public AuthenticationManager getAuthenticationManager() {
        return this.authenticationManager;
    }

    /**
     * @param authenticationManager the authenticationManager to set
     */
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
