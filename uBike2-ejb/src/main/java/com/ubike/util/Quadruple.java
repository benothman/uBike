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
 * {@code Quadruple}
 * <p/>
 *
 * Created on Jun 15, 2011 at 8:31:13 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public class Quadruple<F, S, T, Q> extends Triple<F, S, T> {

    private Q fourth;

    /**
     * Create a new instance of {@code Quadruple}
     */
    public Quadruple() {
        super();
    }

    /**
     * Create a new instance of {@code Quadruple}
     * 
     * @param first the first element
     * @param second the second element
     * @param third the last element
     * @param fourth the fourth element
     */
    public Quadruple(F first, S second, T third, Q fourth) {
        super(first, second, third);
        this.fourth = fourth;
    }

    /**
     * @return the fourth
     */
    public Q getFourth() {
        return fourth;
    }

    /**
     * @param fourth the fourth to set
     */
    public void setFourth(Q fourth) {
        this.fourth = fourth;
    }
}
