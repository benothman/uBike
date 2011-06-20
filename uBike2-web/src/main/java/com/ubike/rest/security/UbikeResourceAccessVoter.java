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

import com.ubike.model.MemberShip;
import com.ubike.model.UbikeUser;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.vote.AccessDecisionVoter;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 *
 * @author Benothman
 */
public abstract class UbikeResourceAccessVoter implements AccessDecisionVoter {

    /* (non-Javadoc)
     * @see org.springframework.security.vote.AccessDecisionVoter#supports(org.springframework.security.ConfigAttribute)
     */
    @Override
    public boolean supports(ConfigAttribute configAttr) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.springframework.security.vote.AccessDecisionVoter#supports(java.lang.Class)
     */
    @Override
    public boolean supports(Class clazz) {
        return true;
    }

    /**
     * Check the user groups members access
     *
     * @param user The user
     * @param other the id of the user who try to access user data
     * @return <tt>ACCESS_GRANTED</tt> if the user having the given id and the
     * given user are members of a same group else <tt>ACCESS_DENIED</tt>
     */
    protected int checkGroupAccess(UbikeUser first, UbikeUser second) {

        for (MemberShip o : first.getMemberShips()) {
            for (MemberShip m : o.getGroup().getMemberShips()) {
                if (m.getMember().getId().equals(second.getId())) {
                    return ACCESS_GRANTED;
                }
            }
        }

        return ACCESS_DENIED;
    }

    /**
     * Check if the current user has access to the resource of the owner user.
     *
     * @param resourceOwner The owner of the resource
     * @param current The current user i.e the logged user
     * @return <tt>ACCESS_GRANTED</tt> if the owner give access to the current
     * user else <tt>ACCESS_DENIED</tt>
     */
    protected int checkAccess(UbikeUser resourceOwner, UbikeUser current) {

        if (resourceOwner.getId().equals(current.getId()) || resourceOwner.getPreferences().isOthersAccess()) {
            return ACCESS_GRANTED;
        }

        return resourceOwner.getPreferences().isGroupAccess() ? checkGroupAccess(resourceOwner, current) : ACCESS_DENIED;
    }

    /**
     * Look for the attribute having the given name in the session scope attributes
     *
     * @return The attribute having the given name.
     */
    public Object getSessionAttribute(String name) {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        return attrs.getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }
}
