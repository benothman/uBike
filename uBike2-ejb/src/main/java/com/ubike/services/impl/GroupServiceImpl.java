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

import com.ubike.model.UbikeGroup;
import com.ubike.services.GroupServiceLocal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * {@code GroupServiceImpl}
 * <p/>
 *
 * Created on Jun 7, 2011 at 6:16:02 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
public class GroupServiceImpl extends AbstractServiceImpl<UbikeGroup> implements GroupServiceLocal {

    @PersistenceContext(unitName = "ubikeEJB")
    private EntityManager entityManager;

    /**
     * Create a new instance of {@code GroupServiceImpl}
     */
    public GroupServiceImpl() {
        super(UbikeGroup.class);
    }

    @Override
    public UbikeGroup findWithMemberShips(Long id) {
        UbikeGroup group = find(id);
        group.getMemberShips().size();
        return group;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
