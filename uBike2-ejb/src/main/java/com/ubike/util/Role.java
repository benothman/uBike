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
 * This class represent the role of a member in the group
 * The creator of a group is the administrator by default so he have all
 * privileges. A member who join an existing group is an ordinary member and he
 * have a restricted privileges
 *
 * @author BENOTHMAN Nabil.
 */
public enum Role implements Serializable {

    // asserted to the creator of the group.
    Creator,
    // asserted to the administrator of the group
    Admin,
    // asserted to an ordinary member who join the group
    Member;
}
