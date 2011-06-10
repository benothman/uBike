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

import com.ubike.model.Route;
import java.net.URI;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;

/**
 *
 * @author Benothman
 */

@XmlRootElement(name = "routes")
public class RoutesConverter {
    private Collection<Route> entities;
    private Collection<RouteConverter> items;
    private URI uri;
    private int expandLevel;
  
    /** Creates a new instance of RoutesConverter */
    public RoutesConverter() {
    }

    /**
     * Creates a new instance of RoutesConverter.
     *
     * @param entities associated entities
     * @param uri associated uri
     * @param expandLevel indicates the number of levels the entity graph should be expanded
     */
    public RoutesConverter(Collection<Route> entities, URI uri, int expandLevel) {
        this.entities = entities;
        this.uri = uri;
        this.expandLevel = expandLevel;
        getRoute();
    }

    /**
     * Returns a collection of RouteConverter.
     *
     * @return a collection of RouteConverter
     */
    @XmlElement
    public Collection<RouteConverter> getRoute() {
        if (items == null) {
            items = new ArrayList<RouteConverter>();
        }
        if (entities != null) {
            items.clear();
            for (Route entity : entities) {
                items.add(new RouteConverter(entity, uri, expandLevel, true));
            }
        }
        return items;
    }

    /**
     * Sets a collection of RouteConverter.
     *
     * @param a collection of RouteConverter to set
     */
    public void setRoute(Collection<RouteConverter> items) {
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
     * Returns a collection Route entities.
     *
     * @return a collection of Route entities
     */
    @XmlTransient
    public Collection<Route> getEntities() {
        entities = new ArrayList<Route>();
        if (items != null) {
            for (RouteConverter item : items) {
                entities.add(item.getEntity());
            }
        }
        return entities;
    }
}
