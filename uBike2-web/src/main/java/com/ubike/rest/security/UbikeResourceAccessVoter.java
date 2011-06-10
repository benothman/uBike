/*
 *  Copyright 2009 Nabil BENOTHMAN <nabil.benothman@gmail.com>.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 *
 *  This class is a part of uBike projet (HEIG-VD)
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
