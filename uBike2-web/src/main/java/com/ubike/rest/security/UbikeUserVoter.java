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

import com.ubike.model.UbikeUser;
import com.ubike.rest.service.UbikeUserResource;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;

/**
 * To implement the security policies, we choose to use the voter mecanism
 * This class is used as a voter for access to the <code>UbikeUserResource</code>
 * If the requested resource is <code>UbikeUserResource</code> the vote method
 * check if the user has permission to access and it returns <tt>ACCESS_GRANTED</tt>
 * if it is the case else returns <tt>ACCESS_DENIED</tt>
 * If the requested resource is not <code>UbikeUserResource</code> so it returns
 * <tt>ACCESS_ABSTAIN</tt>
 *
 * @author Benothman
 */
public class UbikeUserVoter extends UbikeResourceAccessVoter {


    /* (non-Javadoc)
     * @see org.springframework.security.vote.AccessDecisionVoter#vote(org.springframework.security.Authentication,
     * java.lang.Object, org.springframework.security.ConfigAttributeDefinition)
     */
    @Override
    public int vote(Authentication auth, Object secureObj, ConfigAttributeDefinition config) {
        if (supports(secureObj.getClass())) {
            try {
                MethodInvocation mi = (MethodInvocation) secureObj;

                if (mi.getThis().getClass() == UbikeUserResource.class) {
                    
                    UbikeUserResource resource = (UbikeUserResource) mi.getThis();
                    UbikeUser tmp = resource.getEntity();
                    UbikeUser current = (UbikeUser) getSessionAttribute("user");
                    String name = mi.getMethod().getName();

                    if (name.equals("getPreferencesResource") || name.equals("getAccountResource") || name.equals("put")) {
                        return tmp.getId().equals(current.getId()) ? ACCESS_GRANTED : ACCESS_DENIED;
                    }

                    return checkAccess(tmp, current);
                }
            } catch (Exception exp) {
                System.out.println(exp);
            }
        }

        return ACCESS_ABSTAIN;
    }
}
