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

import com.ubike.model.UbikeUser;
import com.ubike.services.UserServiceLocal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * {@code UserServiceImpl}
 * <p/>
 *
 * Created on Jun 7, 2011 at 6:18:58 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
public class UserServiceImpl extends AbstractServiceImpl<UbikeUser> implements UserServiceLocal {

    @PersistenceContext(unitName = "ubikeEJB")
    private EntityManager entityManager;
    private static final Logger logger = Logger.getLogger(UserServiceLocal.class.getName());

    /**
     * Create a new instance of {@code UserServiceImpl}
     */
    public UserServiceImpl() {
        super(UbikeUser.class);
    }

    @Override
    public UbikeUser findWithTrips(Long id) {
        UbikeUser user = find(id);
        user.getTrips().size();
        return user;
    }

    @Override
    public UbikeUser findWithMemberShips(Long id) {
        UbikeUser user = find(id);
        user.getMemberShips().size();
        return user;
    }

    @Override
    public UbikeUser findWithTripsAndMemberShips(Long id) {
        UbikeUser user = findWithTrips(id);
        user.getMemberShips().size();
        return user;
    }

    @Override
    public List<UbikeUser> getFriends(Long id) {
        logger.log(Level.WARNING, "Not yet implemented");
        try {
            String query = "SELECT o FROM UbikeUser o WHERE o.id IN "
                    + "(SELECT m.member.id FROM MemberShip m WHERE )";
            return (List<UbikeUser>) getEntityManager().createQuery(query).getResultList();
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "An error occurs while retrieving friends", exp);
        }

        return new ArrayList<UbikeUser>();
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
