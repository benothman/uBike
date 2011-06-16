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
package com.ubike.rest.security;

import com.ubike.faces.bean.BaseBean;
import com.ubike.model.Account;
import com.ubike.model.UbikeUser;
import com.ubike.services.AccountServiceLocal;
import com.ubike.services.TripManagerLocal;
import com.ubike.services.UserManagerLocal;
import java.io.IOException;
import javax.ejb.EJB;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.context.HttpSessionContextIntegrationFilter;
import org.springframework.security.ui.WebAuthenticationDetails;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * This class is used as managed bean for user authentication
 * The authentication is done by a Spring Security authentication manager, so if
 * the user has a valid username, password and authorities so he can be authenticated
 * else the operation fails.
 * @author Benothman Nabil
 */
@ManagedBean(name = "authenticationController")
@RequestScoped
public final class AuthenticationController {

    @NotEmpty
    @Length(min = 5, max = 12, message = "Username must contain between 7 and 25 characters")
    @Pattern(regexp = ".*[^\\s]", message = "Username cannot contain spaces")
    private String username;
    @NotEmpty
    @Length(min = 7, message = "Password must contain at least 7 characters")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-zA-Z]).{7,25}$", message = "The password is not valid")
    private String password;
    private boolean remember;
    @EJB
    private UserManagerLocal uml;
    @EJB
    private AccountServiceLocal accountService;
    @EJB
    private TripManagerLocal tml;
    // injected properties
    @ManagedProperty(value = "#{authenticationManager}")
    private AuthenticationManager authenticationManager;

    /**
     * Try to authenticate the user using Spring Security authentication mecanism
     * The authentication is considered as accepted if there's no exception
     * handled
     *
     * @return <tt>success</tt> if the authentication succeeded else <tt>failure</tt>
     */
    @SuppressWarnings("unchecked")
    public String authenticate() {

        try {
            final UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(getUsername(), getPassword());

            final HttpServletRequest request = getRequest();
            authReq.setDetails(new WebAuthenticationDetails(request));

            final HttpSession session = request.getSession();
            session.setAttribute(AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY, getUsername());

            // perform authentication
            final Authentication auth = getAuthenticationManager().authenticate(authReq);

            // initialize the security context.
            final SecurityContext secCtx = SecurityContextHolder.getContext();
            secCtx.setAuthentication(auth);
            session.setAttribute(HttpSessionContextIntegrationFilter.SPRING_SECURITY_CONTEXT_KEY, secCtx);

            Account account = accountService.getByUsername(username);
            UbikeUser user = account.getOwner();
            user.getAccount().setLoggedIn(true);
            ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();

            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            attrs.setAttribute("user", user, RequestAttributes.SCOPE_SESSION);
            attrs.setAttribute("uml", getUml(), RequestAttributes.SCOPE_SESSION);
            attrs.setAttribute("tml", getTml(), RequestAttributes.SCOPE_SESSION);
            HttpServletResponse response = (HttpServletResponse) exctx.getResponse();
            response.sendRedirect(exctx.getRequestContextPath() + "/resources/users/" + user.getId());

            return BaseBean.LOGIN_SUCCESS;
        } catch (Exception e) {
            FacesContext fc = FacesContext.getCurrentInstance();
            fc.addMessage("login:login_error", new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The username and/or password is not correct! Please try again",
                    "The username and/or password is not correct! Please try again"));
            return BaseBean.FAILURE;
        }
    }

    /**
     * Try to logout the client
     *
     * @param e The action event of the logout action
     */
    public void logout(ActionEvent e) throws IOException {
        logout();
    }

    /**
     * 
     * @throws IOException
     */
    public void logout() throws IOException {
        final HttpServletRequest request = getRequest();
        request.getSession(false).removeAttribute(HttpSessionContextIntegrationFilter.SPRING_SECURITY_CONTEXT_KEY);

        // simulate the SecurityContextLogoutHandler
        SecurityContextHolder.clearContext();
        request.getSession(false).invalidate();
        ExternalContext exctx = FacesContext.getCurrentInstance().getExternalContext();

        UbikeUser u = (UbikeUser) BaseBean.getSessionAttribute("user");
        if (u != null) {
            getUml().logout(u.getAccount().getId());
        }
        BaseBean.removeSessionAttribute("user");
        BaseBean.removeSessionAttribute("client");

        HttpServletResponse response = (HttpServletResponse) exctx.getResponse();
        response.sendRedirect(exctx.getRequestContextPath());
    }

    /**
     * @return
     */
    private HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    /**
     * @return The authentication manager
     */
    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    /**
     * @param authenticationManager The authentication manager to set
     */
    @Required
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * @return The password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Assert the given String value to the password field
     * @param password The new password value
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Assert the given String value to the username field
     * @param userName
     */
    public void setUsername(String userName) {
        this.username = userName;
    }

    /**
     * @return the User Manager <tt>uml</tt>
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
     * @return the remember
     */
    public boolean isRemember() {
        return remember;
    }

    /**
     * @param remember the remember to set
     */
    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    /**
     * @return the tml
     */
    public TripManagerLocal getTml() {
        return tml;
    }

    /**
     * @param tml the tml to set
     */
    public void setTml(TripManagerLocal tml) {
        this.tml = tml;
    }
}
