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
package com.ubike.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * {@code CustomArrayList}
 * <p/>
 *
 * Created on Jun 12, 2011 at 8:50:06 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public class CustomArrayList<T> extends ArrayList<T> implements CustomList<T> {

    /**
     * Create a new instance of {@code CustomArrayList}
     */
    public CustomArrayList() {
        super();
    }

    /**
     * Create a new instance of {@code CustomArrayList}
     * @param i 
     */
    public CustomArrayList(int i) {
        super(i);
    }

    /**
     * Create a new instance of {@code CustomArrayList}
     * 
     * @param clctn 
     */
    public CustomArrayList(Collection<? extends T> clctn) {
        super(clctn);
    }

    /**
     * @return the first element, if any, in the list 
     */
    @Override
    public T getFirst() {
        return get(0);
    }

    /**
     * @return the last element, if any, in the list
     */
    @Override
    public T getLast() {
        return get(size() - 1);
    }

    @Override
    public int getSize() {
        return this.size();
    }
}
