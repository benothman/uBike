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

import com.ubike.model.Account;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.ws.rs.core.UriBuilder;
import javax.persistence.EntityManager;
import com.ubike.model.UbikeUser;

/**
 * {@code AccountConverter}
 * <p/>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@XmlRootElement(name = "account")
public class AccountConverter {
    private Account entity;
    private URI uri;
    private int expandLevel;
  
    /**
     * Creates a new instance of AccountConverter
     */
    public AccountConverter() {
        entity = new Account();
    }

    /**
     * Creates a new instance of AccountConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded@param isUriExtendable indicates whether the uri can be extended
     */
    public AccountConverter(Account entity, URI uri, int expandLevel,
            boolean isUriExtendable) {
        this.entity = entity;
        this.uri = (isUriExtendable) ? UriBuilder.fromUri(uri).path(entity.getId() + "/").build() : uri;
        this.expandLevel = expandLevel;
        getOwner();
    }

    /**
     * Creates a new instance of AccountConverter.
     *
     * @param entity associated entity
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public AccountConverter(Account entity, URI uri, int expandLevel) {
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
     * Getter for userName.
     *
     * @return value for userName
     */
    @XmlElement
    public String getUserName() {
        return (expandLevel > 0) ? entity.getUsername() : null;
    }

    /**
     * Setter for userName.
     *
     * @param value the value to set
     */
    public void setUserName(String value) {
        entity.setUsername(value);
    }

    /**
     * Getter for keyPass.
     *
     * @return value for keyPass
     */
    @XmlElement
    public String getPassword() {
        return (expandLevel > 0) ? entity.getPassword() : null;
    }

    /**
     * Setter for keyPass.
     *
     * @param value the value to set
     */
    public void setPassword(String value) {
        entity.setPassword(value);
    }

    /**
     * Getter for loggedIn.
     *
     * @return value for loggedIn
     */
    @XmlElement
    public Boolean getLoggedIn() {
        return (expandLevel > 0) ? entity.isLoggedIn() : null;
    }

    /**
     * Setter for loggedIn.
     *
     * @param value the value to set
     */
    public void setLoggedIn(Boolean value) {
        entity.setLoggedIn(value);
    }

    /**
     * Getter for salt.
     *
     * @return value for salt
     */
    @XmlElement
    public String getSalt() {
        return (expandLevel > 0) ? entity.getSalt() : null;
    }

    /**
     * Setter for salt.
     *
     * @param value the value to set
     */
    public void setSalt(String value) {
        entity.setSalt(value);
    }

    /**
     * Getter for owner.
     *
     * @return value for owner
     */
    @XmlElement
    public UbikeUserConverter getOwner() {
        if (expandLevel > 0) {
            if (entity.getOwner() != null) {
                return new UbikeUserConverter(entity.getOwner(),
                        uri.resolve("owner/"), expandLevel - 1, false);
            }
        }
        return null;
    }

    /**
     * Setter for owner.
     *
     * @param value the value to set
     */
    public void setOwner(UbikeUserConverter value) {
        entity.setOwner((value != null) ? value.getEntity() : null);
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
     * Returns the Account entity.
     *
     * @return an entity
     */
    @XmlTransient
    public Account getEntity() {
        if (entity.getId() == null) {
            AccountConverter converter = UriResolver.getInstance().resolve(AccountConverter.class,
                    uri);
            if (converter != null) {
                entity = converter.getEntity();
            }
        }
        return entity;
    }

    /**
     * Returns the resolved Account entity.
     *
     * @return an resolved entity
     */
    public Account resolveEntity(EntityManager em) {
        UbikeUser owner = entity.getOwner();
        if (owner != null) {
            entity.setOwner(em.getReference(UbikeUser.class, owner.getId()));
        }
        return entity;
    }
}
