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

import com.ubike.model.MemberShip;
import com.ubike.model.Statistic;
import java.io.Serializable;
import java.util.List;

/**
 * {@code UbikeEntity}
 * <p/>
 *
 * Created on Jun 7, 2011 at 7:52:33 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public interface UbikeEntity extends Serializable {

    
    /**
     * 
     * @return 
     */
    public Long getId();
    
    /**
     * 
     * @param id 
     */
    public void setId(Long id);
    
    /**
     * 
     * @return 
     */
    public List<MemberShip> getMemberShips();
    
    /**
     * 
     * @param memberShips 
     */
    public void setMemberShips(List<MemberShip> memberShips);
    
    /**
     * 
     * @return 
     */
    public List<Statistic> getStatistics();

    /**
     * 
     * @param statistics 
     */
    public void setStatistics(List<Statistic> statistics);
}
