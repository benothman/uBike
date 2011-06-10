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

import com.ubike.model.Account;
import com.ubike.model.UbikeUser;
import com.ubike.rest.service.AccountsResource;
import java.lang.reflect.Method;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;

/**
 * To implement the security policies, we choose to use the voter mecanism
 * This class is used as a voter for access to the <code>AccountsResource</code>
 * If the requested resource is <code>AccountsResource</code> the vote method
 * check if the user has permission to access and it returns <tt>ACCESS_GRANTED</tt>
 * if it is the case else returns <tt>ACCESS_DENIED</tt>
 * If the requested resource is not <code>AccountsResource</code> so it returns
 * <tt>ACCESS_ABSTAIN</tt>
 *
 * @author Benothman
 */
public class AccountsVoter extends UbikeResourceAccessVoter {

    public int vote(Authentication auth, Object secureObj, ConfigAttributeDefinition cad) {
        if (supports(secureObj.getClass())) {
            try {
                MethodInvocation mi = (MethodInvocation) secureObj;

                if (mi.getThis().getClass() == AccountsResource.class) {

                    AccountsResource resource = (AccountsResource) mi.getThis();
                    Method method = mi.getMethod();

                    if (method.getName().equals("getAccountResource")) {
                        Object arguments[] = mi.getArguments();
                        Long id = (Long) arguments[0];
                        Account tmp = resource.getById(id);
                        UbikeUser current = (UbikeUser) getSessionAttribute("user");
                        if (tmp.getOwner().getId().equals(current.getId())) {
                            return ACCESS_GRANTED;
                        }
                    }

                    return ACCESS_DENIED;
                }
            } catch (Exception exp) {
                System.out.println(exp);
            }
        }

        return ACCESS_ABSTAIN;
    }
}
