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

import com.ubike.model.Trip;
import com.ubike.model.UbikeUser;
import com.ubike.rest.service.TripResource;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;

/**
 * To implement the security policies, we choose to use the voter mecanism
 * This class is used as a voter for access to the <code>TripResource</code>
 * If the requested resource is <code>TripResource</code> the vote method
 * check if the user has permission to access and it returns <tt>ACCESS_GRANTED</tt>
 * if it is the case else returns <tt>ACCESS_DENIED</tt>
 * If the requested resource is not <code>TripResource</code> so it returns
 * <tt>ACCESS_ABSTAIN</tt>
 *
 * @author Benothman
 */
public class TripVoter extends UbikeResourceAccessVoter {

    /* (non-Javadoc)
     * @see org.springframework.security.vote.AccessDecisionVoter#vote(org.springframework.security.Authentication,
     * java.lang.Object, org.springframework.security.ConfigAttributeDefinition)
     */
    public int vote(Authentication auth, Object secureObj, ConfigAttributeDefinition config) {
        if (supports(secureObj.getClass())) {
            try {
                MethodInvocation mi = (MethodInvocation) secureObj;

                if (mi.getThis().getClass() == TripResource.class) {
                    TripResource resource = (TripResource) mi.getThis();
                    Trip tmp = resource.getEntity();
                    UbikeUser current = (UbikeUser) getSessionAttribute("user");

                    return checkAccess(tmp.getOwner(), current);
                }
            } catch (Exception exp) {
                System.out.println(exp);
            }
        }

        return ACCESS_ABSTAIN;
    }
}
