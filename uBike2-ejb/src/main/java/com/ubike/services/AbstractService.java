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

import java.util.List;
import javax.persistence.EntityManager;

/**
 * {@code AbstractService}
 * <p/>
 *
 * Created on May 22, 2011 at 5:06:42 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public interface AbstractService<T> {

    /**
     * Persist the specified entity to the database 
     * 
     * @param entity the entity to be persisted
     */
    public void create(T entity);

    /**
     * Retrieve the entity having the specified <tt>id</tt> from the data-store
     * 
     * @param id the entity <tt>id</tt>
     * @return entity having the specified <tt>id</tt> from the data-store
     */
    public T find(Long id);

    /**
     * Retrieve all entities from the data-store
     * 
     * @return a list of all entities from the data-store
     */
    public List<T> findAll();

    /**
     * Update the specified entity state
     * 
     * @param entity entity to be updated
     */
    public T update(T entity);

    /**
     * Remove the entity having the specified <tt>id</tt> from the data-store
     * 
     * @param id the entity <tt>id</tt>
     */
    public void remove(Long id);

    /**
     * Retrieve a range of entities from the data-store
     * 
     * @param range an array containing the first and last positions
     * @return a list of entities
     */
    public List<T> findRange(int[] range);

    /**
     * Retrieve a range of entities from the data-store
     * 
     * @param start the first entity to retrieve
     * @param max the maximum of entities to retrieve
     * @return a list en entities
     */
    public List<T> findRange(int start, int max);
    
    /**
     * Count the number of entities in the data-store
     * 
     * @return the number of entities in the data-store
     */
    public int count();

    /**
     * @return the entity manager
     */
    public EntityManager getEntityManager();
}
