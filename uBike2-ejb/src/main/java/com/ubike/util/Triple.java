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

/**
 * {@code Triple}
 * <p>Utility class to represent a triple of objects</p>
 *
 * Created on Jun 15, 2011 at 11:54:49 AM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public class Triple<F, S, T> extends Couple<F, S> {
    
    private T third;

    /**
     * Create a new instance of {@code Triple}
     */
    public Triple() {
        super();
    }

    /**
     * Create a new instance of {@code Triple}
     * 
     * @param first the first element
     * @param second the second element
     * @param third the third element
     */
    public Triple(F first, S second, T third) {
        super(first, second);
        this.third = third;
    }

    /**
     * @return the third element
     */
    public T getThird() {
        return third;
    }

    /**
     * @param third the third element to set
     */
    public void setThird(T third) {
        this.third = third;
    }
}
