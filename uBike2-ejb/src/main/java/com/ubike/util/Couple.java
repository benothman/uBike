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

import java.io.Serializable;

/**
 * {@code Couple}
 * <p>Utility class to represent a couple of objects</p>
 *
 * Created on Jun 15, 2011 at 11:51:51 AM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public class Couple<F, S> implements Serializable {

    private String name;
    private F first;
    private S second;

    /**
     * Create a new instance of {@code Couple}
     */
    public Couple() {
        super();
    }

    /**
     * Create a new instance of {@code Couple}
     * @param first
     * @param second 
     */
    public Couple(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * @return the first
     */
    public F getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(F first) {
        this.first = first;
    }

    /**
     * @return the second
     */
    public S getSecond() {
        return second;
    }

    /**
     * @param second the second to set
     */
    public void setSecond(S second) {
        this.second = second;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
