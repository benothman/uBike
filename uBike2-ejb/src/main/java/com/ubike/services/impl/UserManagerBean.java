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
import com.ubike.model.UbikeGroup;
import com.ubike.model.MemberShip;
import com.ubike.model.UbikeUser;
import com.ubike.services.UserManagerLocal;
import com.ubike.util.Role;
import com.ubike.util.Util;
import java.util.LinkedList;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * {@code UserManagerBean}
 * <p />
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
public class UserManagerBean implements UserManagerLocal {

    @PersistenceContext(unitName = "ubikeEJB")
    private EntityManager em;

    /**
     * Persist the given object in the database
     * @param o The Object to be persisted
     */
    private void persist(Object o) {
        em.persist(o);
        em.flush();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#addUser(com.ubike.model.UbikeUser)
     */
    @Override
    public void addUser(UbikeUser user) {
        persist(user);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#addGroup(com.ubike.model.UbikeGroup)
     */
    @Override
    public boolean addGroup(UbikeGroup group) {
        try {
            persist(group);
            return true;
        } catch (Exception exp) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#leaveGroup(com.ubike.model.MemberShip)
     */
    @Override
    public boolean leaveGroup(MemberShip m) {
        try {

            MemberShip tmp = em.find(MemberShip.class, m.getId());
            if (tmp != null) {
                UbikeUser user = tmp.getMember();
                user.getMemberShips().remove(tmp);
                UbikeGroup group = tmp.getGroup();
                group.getMemberShips().remove(tmp);
                em.remove(tmp);
                user = (UbikeUser) updateEntity(user);
                group = (UbikeGroup) updateEntity(group);

                return true;
            }
            return false;
        } catch (Exception exp) {
            System.err.println("UserManagerBean error-> " + exp);
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#createAccount(com.ubike.model.UbikeUser,
     *                    java.lang.String, java.lang.String)
     */
    @Override
    public boolean createAccount(UbikeUser ubikeUser, String username, String password) {
        Account account = this.getByUserName(username);
        if (account == null) {
            try {
                account = new Account(username, ubikeUser);
                List<Authority> authorities = new LinkedList<Authority>();
                authorities.add(new Authority(account, username, "ROLE_USER"));
                authorities.add(new Authority(account, username, "USER_ACCESS"));
                account.setAuthorities(authorities);
                account.setSalt(username);
                account.setKeyPass(password);
                persist(account);
                return true;
            } catch (Exception exp) {
                System.err.println("create account error " + exp.getMessage());
                return false;
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#login( java.lang.String, java.lang.String)
     */
    @Override
    public UbikeUser login(String username, String password) {
        try {
            Account account = getByUserName(username);
            if (account != null && account.getKeyPass().equals(Util.encrypt(password + "{" + username + "}", Util.SHA_512))) {
                account.setLoggedIn(true);
                return account.getOwner();
            }
        } catch (Exception exp) {
            System.err.println("No Login found for ( " + username + ", " + password + ")");
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#logout(java.lang.Long)
     */
    @Override
    public boolean logout(long id) {
        Account account = (Account) this.getAccountById(id);
        if (account != null) {
            account.setLoggedIn(false);
            em.flush();
            return true;
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getByUserName(java.lang.String)
     */
    @Override
    public Account getByUserName(String username) {
        try {
            return (Account) em.createNamedQuery("Account.getByUsername").
                    setParameter("param", username).getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#changePassword(java.lang.Long,
     *                     java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public boolean changePassword(long id, String oldPass, String newPass, String confirm) {
        Account account = (Account) this.getAccountById(id);
        if (account != null) {
            if (account.getKeyPass().equals(Util.getEncryption(
                    account.getSalt() + oldPass)) && newPass.equals(confirm)) {

                account.setKeyPass(newPass);
                em.flush();
                return true;
            }
            return false;
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#removeUser(com.ubike.model.UbikeUser)
     */
    @Override
    public boolean removeUser(UbikeUser user) {
        try {
            UbikeUser o = getUserById(user.getId());
            if (o != null) {
                em.remove(o);
                return true;
            }
        } catch (Exception exp) {
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#removeGroup(com.ubike.model.UbikeGroup)
     */
    @Override
    public boolean removeGroup(UbikeGroup group) {
        try {
            UbikeGroup o = getGroupById(group.getId());
            if (o != null) {
                em.remove(o);
                return true;
            }
        } catch (Exception exp) {
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#updateEntity(java.lang.Object)
     */
    @Override
    public Object updateEntity(Object entity) {
        return em.merge(entity);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getUserById(java.lang.Long)
     */
    @Override
    public UbikeUser getUserById(long id) {
        return (UbikeUser) em.find(UbikeUser.class, id);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getAllUsers()
     */
    @Override
    public Collection<UbikeUser> getAllUsers() {
        try {
            return (Collection<UbikeUser>) em.createNamedQuery("user.getAll").getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<UbikeUser>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getAllGroups()
     */
    @Override
    public Collection<UbikeGroup> getAllGroups() {
        try {
            return (Collection<UbikeGroup>) em.createNamedQuery("Group.getAll").getResultList();
        } catch (Exception exp) {
        }
        return new LinkedList<UbikeGroup>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getAccountById(java.lang.Long)
     */
    @Override
    public Account getAccountById(long id) {
        return (Account) em.find(Account.class, id);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getGroupById(java.lang.Long)
     */
    @Override
    public UbikeGroup getGroupById(long id) {
        return (UbikeGroup) em.find(UbikeGroup.class, id);
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getMemberShip(java.lang.Long, java.lang.Long)
     */
    @Override
    public MemberShip getMemberShip(long userId, long groupId) {
        try {
            return (MemberShip) em.createNamedQuery("MemberShip.getByGroupUser").
                    setParameter("userId", userId).setParameter("groupId", groupId).getSingleResult();
        } catch (Exception exp) {
            System.out.println("[UserManagerBean#getMemberShip(long, long)] error -> " + exp.getMessage());
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#addMember(com.ubike.model.UbikeGroup, com.ubike.model.UbikeUser)
     */
    @Override
    public boolean addMember(UbikeGroup group, UbikeUser member) {

        MemberShip m = getMemberShip(member.getId(), group.getId());
        if (m == null) {
            try {
                m = new MemberShip(member, group, Role.Member);
                persist(m);
                return true;
            } catch (Exception exp) {
                System.out.println("[UserManagerBean#addMember(UbikeGroup, UbikeUser)] error -> " + exp.getMessage());
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getGroupByName(java.lang.String)
     */
    @Override
    public UbikeGroup getGroupByName(String name) {
        try {
            return (UbikeGroup) em.createNamedQuery("Group.getByName").setParameter(
                    "param", name).getSingleResult();
        } catch (Exception exp) {
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getGroupMemberShips(java.lang.Long)
     */
    @Override
    public Collection<MemberShip> getGroupMemberShips(long groupId) {
        try {
            return (Collection<MemberShip>) em.createNamedQuery("MemberShip.getByGroup").
                    setParameter("groupId", groupId).getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<MemberShip>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getUserMemberShip(java.lang.Long)
     */
    @Override
    public Collection<MemberShip> getUserMemberShips(long userId) {
        try {
            return (Collection<MemberShip>) em.createNamedQuery("MemberShip.getByUser").
                    setParameter("userId", userId).getResultList();
        } catch (Exception exp) {
        }

        return new LinkedList<MemberShip>();
    }

    /* (non-Javadoc)
     * @see com.ubike.services.UserManagerLocal#getUserWithMemberShip(java.lang.Long)
     */
    public UbikeUser getUserWithMemberShip(long userId) {
        try {
            // TODO test query -> does not works yet
            return (UbikeUser) em.createNamedQuery("user.getWithMemberShips").setParameter(
                    "userId", userId).getSingleResult();
        } catch (Exception exp) {
            System.err.println(exp);
        }

        return null;
    }
}
