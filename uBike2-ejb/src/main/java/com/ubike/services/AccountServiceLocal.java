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
package com.ubike.services;

import com.ubike.model.Account;
import com.ubike.model.UbikeUser;
import javax.ejb.Local;

/**
 * {@code AccountServiceLocal}
 * <p/>
 *
 * Created on May 22, 2011 at 1:32:36 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Local
public interface AccountServiceLocal extends AbstractService<Account> {

    /**
     * Retrieve the account having the specified username
     * 
     * @param username the username
     * @return an instance of Account
     */
    public Account getByUsername(String username);

    /**
     * Create an account for the specified user with the specified username, 
     * password and salt
     * 
     * @param ubikeUser the user
     * @param username the username of the account
     * @param password the password of the account
     * @return a new instance of {@link com.ubike.model.Account}
     */
    public Account createAccount(UbikeUser ubikeUser, String username, String password);
}
