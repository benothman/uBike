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
package com.ubike.model;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * {@code NormalizedGpsFile}
 * <p/>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Entity
@Table(name = "NORM_GPS_FILES")
@PrimaryKeyJoinColumn(name = "ID_FILE")
public class NormalizedGpsFile extends GpsFile {

    /**
     * Creates a new instance of {@code NormalizedGpsFile}
     */
    public NormalizedGpsFile() {
        super();
    }

    /**
     * Creates a new instance of {@code NormalizedGpsFile}
     * 
     * @param filename the file name
     * @param length the length of the file
     * @param d the creation date of the file
     */
    public NormalizedGpsFile(String filename, long length, Date d) {
        super(filename, length, d);
    }

    /**
     * Creates a new instance of {@code NormalizedGpsFile}
     * 
     * @param filename the file name
     * @param length the length of the file
     * @param d the creation date of the file
     * @param segments the list of segments of the file
     */
    public NormalizedGpsFile(String filename, long length, Date d, List<TripSegment> segments) {
        super(filename, length, d, segments);
    }
}
