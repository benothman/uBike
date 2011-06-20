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

import com.ubike.model.MemberShip;
import java.util.List;
import javax.ejb.Local;

/**
 * {@code MemberShipServiceLocal}
 * <p/>
 *
 * Created on Jun 7, 2011 at 5:28:16 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Local
public interface MemberShipServiceLocal extends AbstractService<MemberShip> {

    /**
     * 
     * @param userId
     * @return 
     */
    public List<MemberShip> getUserMemberShips(Long userId);

    /**
     * 
     * @param groupId
     * @return 
     */
    public List<MemberShip> getGroupMemberShips(Long groupId);

    /**
     * 
     * @param userId
     * @param groupId
     * @return 
     */
    public MemberShip getMemberShip(Long userId, Long groupId);

    /**
     * Count the number of active members of a given group
     * 
     * @param groupId the group id
     * @return the number of the active members of a group
     */
    public int countActiveMembers(Long groupId);

    /**
     * Count the number of non active members of a given group
     * 
     * @param groupId the group id
     * @return the number of the non active members of a group
     */
    public int countNonActiveMembers(Long groupId);

    /**
     * 
     * @param userId
     * @return 
     */
    public List<MemberShip> getFriends(Long userId);
}
