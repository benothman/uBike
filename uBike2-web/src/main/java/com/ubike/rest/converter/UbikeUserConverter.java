/*
 *  Copyright 2009 Nabil BENOTHMAN <nabil.benothman@gmail.com>.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 *
 *  This class is a part of uBike projet (HEIG-VD)
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
 *
 * @author Benothman
 */
@XmlRootElement(name = "uBikeUser")
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
    public String getFirstName() {
        return (expandLevel > 0) ? entity.getFirstName() : null;
    }

    /**
     * Setter for firstName.
     *
     * @param value the value to set
     */
    public void setFirstName(String value) {
        entity.setFirstName(value);
    }

    /**
     * Getter for lastName.
     *
     * @return value for lastName
     */
    @XmlElement
    public String getLastName() {
        return (expandLevel > 0) ? entity.getLastName() : null;
    }

    /**
     * Setter for lastName.
     *
     * @param value the value to set
     */
    public void setLastName(String value) {
        entity.setLastName(value);
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
