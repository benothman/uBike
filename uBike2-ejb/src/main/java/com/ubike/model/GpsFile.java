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

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * {@code GpsFile}
 * <p/>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Entity
@Table(name = "GPS_FILES")
@NamedQueries({
    @NamedQuery(name = "GPSFILE.getAll", query = "SELECT o FROM GpsFile o"),
    @NamedQuery(name = "GPSFILE.getByName",
    query = "SELECT o FROM GpsFile o WHERE o.fileName=:param")
})
@Inheritance(strategy = InheritanceType.JOINED)
public class GpsFile implements Serializable {

    /**
     *
     */
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FILENAME", nullable = false)
    private String fileName;
    @Column(name = "FILE_LENGTH", nullable = false)
    private long length;
    @Column(name = "CREATION_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @OneToMany(mappedBy = "gpsFile", cascade = {CascadeType.MERGE,
        CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private Collection<TripSegment> segments = new LinkedList<TripSegment>();

    /**
     *
     */
    public GpsFile() {
        super();
    }

    /**
     *
     * @param fileName the file name
     * @param length the length of the file
     * @param d the creation date of the file
     */
    public GpsFile(String fileName, long length, Date d) {
        this.fileName = fileName;
        this.length = length;
        this.creationDate = d;
    }

    /**
     * 
     * @param fileName the file name
     * @param length the length of the file
     * @param d the creation date of the file
     * @param segments the list of segments of the file
     */
    public GpsFile(String fileName, long length, Date d,
            Collection<TripSegment> segments) {
        this(fileName, length, d);
        this.segments = segments;
    }

    /**
     * @return The id of the <code>GpsFile</code>
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * @param filename the name to set
     */
    public void setFileName(String filename) {
        this.fileName = filename;
    }

    /**
     * @return the creation date
     */
    public Date getCreationDate() {
        return this.creationDate;
    }

    /**
     * @param creationDate the creation date to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @return the file length
     */
    public long getLength() {
        return this.length;
    }

    /**
     * @param length the file length to set
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     * @return the segments
     */
    public Collection<TripSegment> getSegments() {
        return this.segments;
    }

    /**
     * @param segments the segments to set
     */
    public void setSegments(Collection<TripSegment> segments) {
        this.segments = segments;
    }
}
