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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * {@code UbikeUser}
 * <p/>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@Entity
@Table(name = "UBIKE_USERS")
@NamedQueries({
    @NamedQuery(name = "UbikeUser.getAll", query = "SELECT o FROM UbikeUser o ORDER BY o.lastname, o.firstname"),
    @NamedQuery(name = "UbikeUser.getByName", query = "SELECT o FROM UbikeUser o WHERE o.firstname=:param1 AND o.lastname=:param2"),
    @NamedQuery(name = "UbikeUser.getByAccount", query = "SELECT o FROM UbikeUser o WHERE o.account.id=:accountId"),
    @NamedQuery(name = "UbikeUser.getWithMemberShips", query = "SELECT o FROM UbikeUser o LEFT JOIN FETCH o.memberShips WHERE o.id=:userId")
})
public class UbikeUser implements com.ubike.util.UbikeEntity {

    /**
     * 
     */
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "FIRSTNAME", length = 50, nullable = false)
    private String firstname;
    @Column(name = "LASTNAME", length = 50, nullable = false)
    private String lastname;
    @Column(name = "ADDRESS", length = 255)
    private String address;
    @Column(name = "PHONE", length = 20, nullable = false)
    private String phone;
    @Column(name = "EMAIL", length = 50, nullable = false)
    private String email;
    @OneToOne(mappedBy = "owner", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Account account;
    @OneToOne(mappedBy = "owner", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private PrivacyPreferences preferences;
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private UserProfile userProfile;
    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<MemberShip> memberShips = new ArrayList<MemberShip>();
    @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Trip> trips = new ArrayList<Trip>();
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Statistic> statistics;

    /**
     * Create a new <code>UbikeUser</code> instance
     */
    public UbikeUser() {
        if (this.preferences == null) {
            this.preferences = new PrivacyPreferences(this, false, false);
        }
        if (this.userProfile == null) {
            this.userProfile = new UserProfile();
        }
    }

    /**
     * Create a new <code>UbikeUser</code> instance with the given first and last
     * names.
     *
     * @param firstName
     * @param lastName
     */
    public UbikeUser(String firstName, String lastName) {
        this();
        this.firstname = firstName;
        this.lastname = lastName;
    }

    /**
     * @param firstname
     * @param lastname
     * @param mail
     * @param username
     * @param address
     * @param phone
     */
    public UbikeUser(String firstname, String lastname, String address,
            String phone, String email) {
        this(firstname, lastname);
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return this.firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return this.lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the mail
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @param mail the mail to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return this.phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the trips
     */
    public List<Trip> getTrips() {
        return this.trips;
    }

    /**
     * @param trips the trips to set
     */
    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    /**
     * @return the accounts of this user
     */
    public Account getAccount() {
        return this.account;
    }

    /**
     * @param account the accounts to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @return the private preferences
     */
    public PrivacyPreferences getPreferences() {
        return this.preferences;
    }

    /**
     * @param preferences the private preferences to set
     */
    public void setPreferences(PrivacyPreferences preferences) {
        this.preferences = preferences;
    }

    /**
     * @return The memberships of the <code>UbikeUser</code>
     */
    @Override
    public List<MemberShip> getMemberShips() {
        return this.memberShips;
    }

    /**
     * @param memberShips
     */
    @Override
    public void setMemberShips(List<MemberShip> memberShips) {
        this.memberShips = memberShips;
    }

    @Override
    public String toString() {
        return this.firstname + " " + this.lastname + " : " + this.address + ", " + this.phone + ", " + this.email;
    }

    @Transient
    public int getTripsCount() {
        return this.trips.size();
    }

    /**
     * @return the userProfile
     */
    public UserProfile getUserProfile() {
        return userProfile;
    }

    /**
     * @param userProfile the userProfile to set
     */
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    /**
     * @return the id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the statistics
     */
    @Override
    public List<Statistic> getStatistics() {
        return statistics;
    }

    /**
     * @param statistics the statistics to set
     */
    @Override
    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
    }
}
