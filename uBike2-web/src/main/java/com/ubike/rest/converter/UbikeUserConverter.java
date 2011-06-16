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
package com.ubike.rest.converter;

import com.ubike.model.UbikeUser;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import com.ubike.model.Account;
import com.ubike.model.MemberShip;
import com.ubike.model.PrivacyPreferences;
import com.ubike.model.Trip;
import java.util.List;

/**
 * {@code UbikeUserConverter}
 * <p/>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@XmlRootElement(name = "ubike-user")
public class UbikeUserConverter {

    private UbikeUser entity;
    private URI uri;
    private int expandLevel;

    /**
     * Creates a new instance of UbikeUserConverter
     */
    public UbikeUserConverter() {
        entity = new UbikeUser();
    }

    /**
     * Creates a new instance of UbikeUserConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public UbikeUserConverter(UbikeUser entity, URI uri, int expandLevel,
            boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getTrips();
        getAccount();
        getMemberShips();
        getPreferences();
    }

    /**
     * Creates a new instance of UbikeUserConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public UbikeUserConverter(UbikeUser entity, URI uri, int expandLevel) {
        this(entity, uri, expandLevel, false);
    }

    /**
     * Getter for id.
     *
     * @return value for id
     */
    @XmlElement
    public Long getId() {
        return (expandLevel > 0) ? entity.getId() : null;
    }

    /**
     * Setter for id.
     *
     * @param value the value to set
     */
    public void setId(Long value) {
        entity.setId(value);
    }

    /**
     * Getter for firstName.
     *
     * @return value for firstName
     */
    @XmlElement
    public String getFirstname() {
        return (expandLevel > 0) ? entity.getFirstname() : null;
    }

    /**
     * Setter for firstName.
     *
     * @param value the value to set
     */
    public void setFirstname(String value) {
        entity.setFirstname(value);
    }

    /**
     * Getter for lastName.
     *
     * @return value for lastName
     */
    @XmlElement
    public String getLastname() {
        return (expandLevel > 0) ? entity.getLastname() : null;
    }

    /**
     * Setter for lastName.
     *
     * @param value the value to set
     */
    public void setLastname(String value) {
        entity.setLastname(value);
    }

    /**
     * Getter for address.
     *
     * @return value for address
     */
    @XmlElement
    public String getAddress() {
        return (expandLevel > 0) ? entity.getAddress() : null;
    }

    /**
     * Setter for address.
     *
     * @param value the value to set
     */
    public void setAddress(String value) {
        entity.setAddress(value);
    }

    /**
     * Getter for phone.
     *
     * @return value for phone
     */
    @XmlElement
    public String getPhone() {
        return (expandLevel > 0) ? entity.getPhone() : null;
    }

    /**
     * Setter for phone.
     *
     * @param value the value to set
     */
    public void setPhone(String value) {
        entity.setPhone(value);
    }

    /**
     * Getter for email.
     *
     * @return value for email
     */
    @XmlElement
    public String getEmail() {
        return (expandLevel > 0) ? entity.getEmail() : null;
    }

    /**
     * Setter for email.
     *
     * @param value the value to set
     */
    public void setEmail(String value) {
        entity.setEmail(value);
    }

    /**
     * Getter for trips.
     *
     * @return value for trips
     */
    @XmlElement
    public TripsConverter getTrips() {
        if (expandLevel > 0) {
            if (entity.getTrips() != null) {
                return new TripsConverter(entity.getTrips(),
                        uri.resolve("trips/"), expandLevel - 1);
            }
        }
        return null;
    }

    /**
     * Setter for trips.
     *
     * @param value the value to set
     */
    public void setTrips(TripsConverter value) {
        entity.setTrips((value != null) ? value.getEntities() : null);
    }

    /**
     * Getter for account.
     *
     * @return value for account
     */
    @XmlElement
    public AccountConverter getAccount() {
        if (expandLevel > 0) {
            if (entity.getAccount() != null) {
                return new AccountConverter(entity.getAccount(),
                        uri.resolve("account/"), expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for account.
     *
     * @param value the value to set
     */
    public void setAccount(AccountConverter value) {
        entity.setAccount((value != null) ? value.getEntity() : null);
    }

    /**
     * Getter for memberShips.
     *
     * @return value for memberShips
     */
    @XmlElement
    public MemberShipsConverter getMemberShips() {
        if (expandLevel > 0) {
            if (entity.getMemberShips() != null) {
                return new MemberShipsConverter(entity.getMemberShips(),
                        uri.resolve("memberShips/"), expandLevel - 1);
            }
        }
        return null;
    }

    /**
     * Setter for memberShips.
     *
     * @param value the value to set
     */
    public void setMemberShips(MemberShipsConverter value) {
        entity.setMemberShips((value != null) ? value.getEntities() : null);
    }

    /**
     * Getter for preferences.
     *
     * @return value for preferences
     */
    @XmlElement
    public PrivacyPreferencesConverter getPreferences() {
        if (expandLevel > 0) {
            if (entity.getPreferences() != null) {
                return new PrivacyPreferencesConverter(entity.getPreferences(),
                        uri.resolve("preferences/"), expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for preferences.
     *
     * @param value the value to set
     */
    public void setPreferences(PrivacyPreferencesConverter value) {
        entity.setPreferences((value != null) ? value.getEntity() : null);
    }

    /**
     * Returns the URI associated with this converter.
     *
     * @return the uri
     */
    @XmlAttribute
    public URI getUri() {
        return uri;
    }

    /**
     * Sets the URI for this reference converter.
     *
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Returns the UbikeUser entity.
     *
     * @return an entity
     */
    @XmlTransient
    public UbikeUser getEntity() {
        if (entity.getId() == null) {
            UbikeUserConverter converter = UriResolver.getInstance().resolve(UbikeUserConverter.class,
                    uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved UbikeUser entity.
     *
     * @return an resolved entity
     */
    public UbikeUser resolveEntity(EntityManager em) {
        List<Trip> trips = entity.getTrips();
        List<Trip> newtrips = new java.util.ArrayList<Trip>();
        for (Trip item : trips) {
            newtrips.add(em.getReference(Trip.class, item.getId()));
        }
        entity.setTrips(newtrips);
        Account account = entity.getAccount();
        if (account != null) {
            entity.setAccount(em.getReference(Account.class, account.getId()));
        }
        List<MemberShip> memberShips = entity.getMemberShips();
        List<MemberShip> newmemberShips = new java.util.ArrayList<MemberShip>();
        for (MemberShip item : memberShips) {
            newmemberShips.add(em.getReference(MemberShip.class, item.getId()));
        }
        entity.setMemberShips(newmemberShips);
        PrivacyPreferences preferences = entity.getPreferences();
        if (preferences != null) {
            entity.setPreferences(em.getReference(PrivacyPreferences.class,
                    preferences.getId()));
        }
        return entity;
    }
}
