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

import com.ubike.model.MemberShip;
import com.ubike.model.Ranking;
import com.ubike.services.MemberShipServiceLocal;
import com.ubike.util.Metric;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * {@code MemberShipServiceImpl}
 * <p/>
 *
 * Created on Jun 7, 2011 at 6:17:32 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
public class MemberShipServiceImpl extends AbstractServiceImpl<MemberShip> implements MemberShipServiceLocal {

    @PersistenceContext(unitName = "ubikeEJB")
    private EntityManager entityManager;
    private static final Logger logger = Logger.getLogger(MemberShipServiceLocal.class.getName());

    /**
     * Create a new instance of {@code MemberShipServiceImpl}
     */
    public MemberShipServiceImpl() {
        super(MemberShip.class);
    }

    @Override
    public void create(MemberShip entity) {
        if (entity.getRankings() == null) {
            entity.setRankings(new ArrayList<Ranking>());
        }

        int rank = getGroupCount(entity.getGroup().getId()) + 1;

        if (entity.getRankings().isEmpty()) {
            for (Metric m : Metric.values()) {
                Ranking ranking = new Ranking(rank, 0, m);
                entity.getRankings().add(ranking);
            }
        }

        super.create(entity);
    }

    /**
     * 
     * @param groupId
     * @return 
     */
    private int getGroupCount(Long groupId) {
        try {
            return (Integer) getEntityManager().createQuery("SELECT COUNT(o.id) FROM MemberShip o WHERE o.group.id="
                    + groupId).getSingleResult();
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "Error while retreiving the number of members", exp);
            return 0;
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public int countActiveMembers(Long groupId) {
        return countMembers(groupId, true);
    }

    @Override
    public int countNonActiveMembers(Long groupId) {
        return countMembers(groupId, false);
    }

    @Override
    public List<MemberShip> getFriends(Long userId) {
        logger.log(Level.INFO, "Retrieving friends list for user [id = {0}]", userId);
        try {
            /*
            String query = "SELECT o FROM MemberShip o WHERE m.group.id IN (SELECT m.group.id FROM "
            + "MemberShip m WHERE m.member.id = " + userId + ") AND NOT (o.member.id = "
            + userId + ")";
            return (List<MemberShip>) getEntityManager().createQuery(query).getResultList();
             */
            return (List<MemberShip>) getEntityManager().createNamedQuery("MemberShip.getUserFriends").
                    setParameter("userId", userId).getResultList();
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "An error occurs while retreiving friends list for user with id = " + userId, exp);
            return new ArrayList<MemberShip>();
        }
    }

    /**
     * 
     * @param groupId
     * @param active
     * @return 
     */
    private int countMembers(Long groupId, boolean active) {
        logger.log(Level.INFO, "Counting members for group [id = {0}]", groupId);
        try {
            String query = "SELECT COUNT(o.id) FROM MemberShip o WHERE o.group.id=" + groupId + "AND o.active=" + active;
            return (Integer) getEntityManager().createQuery(query).getSingleResult();
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "Error occurs while counting the number of members", exp);
        }

        return 0;
    }

    @Override
    public List<MemberShip> getUserMemberShips(Long userId) {
        try {
            return getEntityManager().createNamedQuery("MemberShip.getByUser").
                    setParameter("userId", userId).getResultList();
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "Error while retreiving the user memberships", exp);
        }

        return new ArrayList<MemberShip>();
    }

    @Override
    public List<MemberShip> getGroupMemberShips(Long groupId) {
        try {
            return getEntityManager().createNamedQuery("MemberShip.getByGroup").
                    setParameter("groupId", groupId).getResultList();
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "Error while retreiving the group memberships", exp);
        }

        return new ArrayList<MemberShip>();
    }

    @Override
    public MemberShip getMemberShip(Long userId, Long groupId) {
        try {
            return getEntityManager().createNamedQuery("MemberShip.getByGroupUser", MemberShip.class).
                    setParameter("groupId", groupId).setParameter("userId", userId).getSingleResult();
        } catch (Exception exp) {
            logger.log(Level.SEVERE, "Error while retreiving the group memberships", exp);
        }
        return null;
    }
}
