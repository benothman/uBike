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

import com.ubike.model.MemberShip;
import java.net.URI;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benothman
 */
@XmlRootElement(name = "memberShips")
public class MemberShipsConverter {

    private List<MemberShip> entities;
    private List<MemberShipConverter> items;
    private URI uri;
    private int expandLevel;

    /** Creates a new instance of MemberShipsConverter */
    public MemberShipsConverter() {
    }

    /**
     * Creates a new instance of MemberShipsConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public MemberShipsConverter(List<MemberShip> entities, URI uri,
            int expandLevel) {
        this.entities = entities;
        this.uri = uri;
        this.expandLevel = expandLevel;
        getMemberShip();
    }

    /**
     * Returns a collection of MemberShipConverter.
     *
     * @return a collection of MemberShipConverter
     */
    @XmlElement
    public List<MemberShipConverter> getMemberShip() {
        if (items == null) {
            items = new ArrayList<MemberShipConverter>();
        }
        if (entities != null) {
            items.clear();
            for (MemberShip entity : entities) {
                items.add(new MemberShipConverter(entity, uri, expandLevel, true));
            }
        }
        return items;
    }

    /**
     * Sets a collection of MemberShipConverter.
     *
     * @param a collection of MemberShipConverter to set
     */
    public void setMemberShip(List<MemberShipConverter> items) {
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
     * Returns a collection MemberShip entities.
     *
     * @return a collection of MemberShip entities
     */
    @XmlTransient
    public List<MemberShip> getEntities() {
        entities = new ArrayList<MemberShip>();
        if (items != null) {
            for (MemberShipConverter item : items) {
                entities.add(item.getEntity());
            }
        }
        return entities;
    }
}
