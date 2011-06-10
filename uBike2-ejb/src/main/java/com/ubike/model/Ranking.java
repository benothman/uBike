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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Benothman
 */
@Entity
@Table(name = "RANKINGS")
public class Ranking implements Serializable {

    /**
     * 
     */
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "RANK")
    private int rank;
    @Column(name = "RANK_VALUE")
    private double value;
    @ManyToOne(cascade={}, fetch=FetchType.EAGER)
    @JoinColumn(name = "ID_STAT", referencedColumnName = "ID")
    private Statistic statistic;
    @ManyToOne(cascade={}, fetch=FetchType.EAGER)
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID")
    private UbikeUser user;

    /**
     * 
     */
    public Ranking() {
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the statistic
     */
    public Statistic getStatistic() {
        return statistic;
    }

    /**
     * @param statistic the statistic to set
     */
    public void setStatistic(Statistic statistic) {
        this.statistic = statistic;
    }

    /**
     * @return the user
     */
    public UbikeUser getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(UbikeUser user) {
        this.user = user;
    }
}
