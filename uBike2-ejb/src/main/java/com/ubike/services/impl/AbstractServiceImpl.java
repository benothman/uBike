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

import com.ubike.services.AbstractService;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * {@code AbstractServiceImpl}
 * <p/>
 *
 * Created on May 22, 2011 at 10:08:17 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public abstract class AbstractServiceImpl<T> implements AbstractService<T> {

    private Class<T> entityClass;

    /**
     * Create a new instance of {@code AbstractServiceImpl}
     */
    public AbstractServiceImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * 
     * @return 
     */
    public Class<T> getEntityClass() {
        return this.entityClass;
    }

    /*
     * 
     */
    @Override
    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    /*
     * 
     */
    @Override
    public T find(Long id) {
        return getEntityManager().find(entityClass, id);
    }

    /*
     * 
     */
    @Override
    public List<T> findAll() {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        return getEntityManager().createQuery(cq).getResultList();
    }

    /*
     * 
     */
    @Override
    public T update(T entity) {
        return getEntityManager().merge(entity);
    }

    /*
     * 
     */
    @Override
    public void remove(Long id) {
        getEntityManager().remove(find(id));
    }

    /*
     * 
     */
    @Override
    public List<T> findRange(int[] range) {
        return findRange(range[0], range[1]);
    }

    /**
     * 
     * @param start
     * @param max
     * @return 
     */
    @Override
    public List<T> findRange(int start, int max) {
        CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(max);
        q.setFirstResult(start);
        return q.getResultList();
    }

    /*
     * 
     */
    @Override
    public int count() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery().where();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
