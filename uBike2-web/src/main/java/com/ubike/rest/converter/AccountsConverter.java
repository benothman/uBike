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

import com.ubike.model.Account;
import java.net.URI;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;

/**
 * {@code AccountsConverter}
 * <p></p>
 *
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@XmlRootElement(name = "accounts")
public class AccountsConverter {
    private Collection<Account> entities;
    private Collection<AccountConverter> items;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of AccountsConverter */
    public AccountsConverter() {
    }

    /**
     * Creates a new instance of AccountsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public AccountsConverter(Collection<Account> entities, URI uri,
            int expandLevel) {
        this.entities = entities;
        this.uri = uri;
        this.expandLevel = expandLevel;
        getAccount();
    }

    /**
     * Returns a collection of AccountConverter.
     *
     * @return a collection of AccountConverter
     */
    @XmlElement
    public Collection<AccountConverter> getAccount() {
        if (items == null) {
            items = new ArrayList<AccountConverter>();
        }
        if (entities != null) {
            items.clear();
            for (Account entity : entities) {
                items.add(new AccountConverter(entity, uri, expandLevel, true));
            }
        }
        return items;
    }

    /**
     * Sets a collection of AccountConverter.
     *
     * @param a collection of AccountConverter to set
     */
    public void setAccount(Collection<AccountConverter> items) {
        this.items = items;
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
     * Returns a collection Account entities.
     *
     * @return a collection of Account entities
     */
    @XmlTransient
    public Collection<Account> getEntities() {
        entities = new ArrayList<Account>();
        if (items != null) {
            for (AccountConverter item : items) {
                entities.add(item.getEntity());
            }
        }
        return entities;
    }
}
