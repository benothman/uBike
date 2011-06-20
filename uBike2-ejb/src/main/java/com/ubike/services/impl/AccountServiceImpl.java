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
package com.ubike.services.impl;

import com.ubike.model.Account;
import com.ubike.model.Authority;
import com.ubike.model.UbikeUser;
import com.ubike.services.AccountServiceLocal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * {@code AccountServiceImpl}
 * <p/>
 *
 * Created on Jun 7, 2011 at 6:13:50 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
public class AccountServiceImpl extends AbstractServiceImpl<Account> implements AccountServiceLocal {

    @PersistenceContext(unitName = "ubikeEJB")
    private EntityManager entityManager;

    /**
     * Create a new instance of {@code AccountServiceImpl}
     */
    public AccountServiceImpl() {
        super(Account.class);
    }

    /*
     * 
     */
    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /*
     * 
     */
    @Override
    public Account getByUsername(String username) {
        try {
            return getEntityManager().createNamedQuery("Account.getByUsername", Account.class).
                    setParameter("param", username).getSingleResult();
        } catch (Exception exp) {
            return null;
        }
    }

    /*
     * 
     */
    @Override
    public Account createAccount(UbikeUser user, String username, String password) {

        Account account = getByUsername(username);
        if (account == null) {
            try {
                return addNewAccount(user, username, password);
            } catch (Exception exp) {
                System.err.println("create account error " + exp.getMessage());
            }
        }

        return null;
    }

    /**
     * 
     * @param user
     * @param username
     * @param password
     * @param salt
     * @return
     * @throws Exception 
     */
    private Account addNewAccount(UbikeUser user, String username, String password) throws Exception {
        Account account = new Account(username, user);
        List<Authority> authorities = new ArrayList<Authority>();
        authorities.add(new Authority(account, username, "ROLE_USER"));
        authorities.add(new Authority(account, username, "USER_ACCESS"));
        account.setAuthorities(authorities);
        account.setSalt(username);
        account.setPassword(password);
        create(account);
        return account;
    }
}
