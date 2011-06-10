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
import com.ubike.model.UbikeGroup;
import com.ubike.model.MemberShip;
import com.ubike.model.UbikeUser;
import java.util.Collection;
import javax.ejb.Local;

/**
 * {@code UserManagerLocal}
 * <p />
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Local
public interface UserManagerLocal {

    /**
     * Add the given user to the data base
     *
     * @param user The user to be added
     */
    public void addUser(UbikeUser user);

    /**
     * Try to add the given to the data base
     * @param group The group to be added
     * @return True if the operation finish with success else False
     */
    public boolean addGroup(UbikeGroup group);

    /**
     * Remove the membership from the database
     * @param m The membership to be deleted
     * @return true if the operation finish successfully else false
     */
    public boolean leaveGroup(MemberShip m);

    /**
     * Create an account for the given user
     * @param user The user
     * @param username The username
     * @param password The password
     * @return True if the opeation finish with success else False
     */
    public boolean createAccount(UbikeUser user, String username, String password);

    /**
     * @param username
     * @param password
     * @return
     */
    public UbikeUser login(String username, String password);

    /**
     * @param id
     * @return
     */
    public boolean logout(long id);

    /**
     * @param username
     * @return
     */
    public Account getByUserName(String username);

    /**
     * @param id
     * @param oldPass
     * @param newPass
     * @param confirm
     * @return
     */
    public boolean changePassword(long id, String oldPass, String newPass, String confirm);

    /**
     * @param user
     * @return
     */
    public boolean removeUser(UbikeUser user);

    /**
     * @param group
     * @return
     */
    public boolean removeGroup(UbikeGroup group);

    /**
     * @param entity
     */
    public Object updateEntity(Object entity);

    /**
     * @param id
     * @return
     */
    public UbikeUser getUserById(long id);

    /**
     * @return a list of all users
     */
    public Collection<UbikeUser> getAllUsers();

    /**
     * @return a list of all groups
     */
    public Collection<UbikeGroup> getAllGroups();

    /**
     * @param id The account id
     * @return The account having the given id
     */
    public Account getAccountById(long id);

    /**
     * @param id The group id
     * @return The group having the given id
     */
    public UbikeGroup getGroupById(long id);

    /**
     * @param name The group name
     * @return The group having the given name
     */
    public UbikeGroup getGroupByName(String name);

    /**
     * Try to add the given member to the group memberships
     * @param group The group
     * @param member The menber to be added
     * @return True if the  operation finish with success else False
     */
    public boolean addMember(UbikeGroup group, UbikeUser member);

    /**
     * Fetch the MemberShips of the user given by his id
     *
     * @param userId The id of the user
     * @return The list of the memberships of the user given by his id
     */
    public Collection<MemberShip> getUserMemberShips(long userId);

    /**
     * Fetch the MemberShips of the group given by it's id
     *
     * @param groupId The id of the group
     * @return The list of the memberships of the group given by it's id
     */
    public Collection<MemberShip> getGroupMemberShips(long groupId);

    /**
     * Fetch the membership relation between the given group and user
     * @param userId The user id
     * @param groupId The group id
     * @return The membership if exists else null
     */
    public MemberShip getMemberShip(long userId, long groupId);
}
