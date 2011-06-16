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

import com.ubike.model.Statistic;
import com.ubike.services.StatisticServiceLocal;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * {@code StatisticServiceImpl}
 * <p/>
 *
 * Created on Jun 12, 2011 at 8:14:37 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Stateless
public class StatisticServiceImpl extends AbstractServiceImpl<Statistic> implements StatisticServiceLocal {

    @PersistenceContext(unitName = "ubikeEJB")
    private EntityManager entityManager;

    /**
     * Create a new instance of {@code StatisticServiceImpl}
     */
    public StatisticServiceImpl() {
        super(Statistic.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}
