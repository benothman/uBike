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
import com.ubike.services.MemberShipServiceLocal;
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

    /**
     * Create a new instance of {@code MemberShipServiceImpl}
     */
    public MemberShipServiceImpl() {
        super(MemberShip.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
