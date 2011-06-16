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

import com.ubike.util.Util;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * {@code Account}
 * <p/>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Entity
@Table(name = "ACCOUNTS", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"USERNAME"})
})
@NamedQueries({
    @NamedQuery(name = "Account.getAll", query = "SELECT o FROM Account o"),
    @NamedQuery(name = "Account.getByUsername",
    query = "SELECT o FROM Account o WHERE o.username=:param"),
    @NamedQuery(name = "Account.getByUser",
    query = "SELECT o FROM Account o WHERE o.owner.id=:ownerId")
})
public class Account implements Serializable {

    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "USERNAME", length = 20, unique = true, nullable = false, updatable=false)
    private String username;
    @Column(name = "PASSWORD", nullable = false)
    private String keyPass;
    @Column(name = "LOGGEDIN")
    private boolean loggedIn;
    @Column(name = "ENABLED")
    private boolean enabled;
    @Column(name = "SALT", nullable = false)
    private String salt;
    @Column(name = "ADHESION_DATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date adhesionDate;
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_OWNER", referencedColumnName = "ID")
    private UbikeUser owner;
    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Authority> authorities;

    /**
     *
     */
    public Account() {
        this.adhesionDate = new Date(System.currentTimeMillis());
    }

    /**
     * @param userName
     * @param owner
     */
    public Account(String userName, UbikeUser owner) {
        this();
        this.username = userName;
        this.owner = owner;
        this.enabled = true;
        this.owner.setAccount(this);
    }

    /**
     * @return The id of the <code>UbikeEntity</code>
     */
    public Long getId() {
        return this.id;
    }

    /**
     * @param id The id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @param username the userName to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getKeyPass() {
        return this.keyPass;
    }

    /**
     * @param keyPass
     */
    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    /**
     * @return the salt
     */
    public String getSalt() {
        return this.salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * @return the loggedIn
     */
    public boolean isLoggedIn() {
        return this.loggedIn;
    }

    /**
     * @param loggedIn the loggedIn to set
     */
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    /**
     * @return the owner
     */
    public UbikeUser getOwner() {
        return this.owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(UbikeUser owner) {
        this.owner = owner;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the authorities
     */
    public List<Authority> getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    /**
     * @return the adhesionDate
     */
    public Date getAdhesionDate() {
        return this.adhesionDate;
    }

    /**
     * @param adhesionDate the adhesionDate to set
     */
    public void setAdhesionDate(Date adhesionDate) {
        this.adhesionDate = adhesionDate;
    }

    @Transient
    public String getFormatDate() {
        return Util.formatDate(getAdhesionDate());
    }
}
