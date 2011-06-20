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
package com.ubike.faces.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * {@code TripMapBean}
 * <p></p>
 * Created on Jun 6, 2011 at 7:17:22 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@ManagedBean(name = "gmBean")
@RequestScoped
public class TripMapBean extends AbstractBean {

    private int zoom = 11;
    // this key is used for url : "http://193.134.218.17"
    //private String gmapkey = "ABQIAAAAo3M6TqLV705jU8tXZMdnZhSojq1cE0wCLib52N96vdjJwohopRTmOM-8UiZJp8-MDLSaJ_Vapw7puQ";
    // this key is used for url : "http://193.134.218.14"
    private String gmapkey = "ABQIAAAAo3M6TqLV705jU8tXZMdnZhTXNRRhQ9T92wCb7M39oPc_GNMMQBRGy8pCEYAS2vAbPkX6MmHx7NCrNA";
    private String currentPlace;
    private int currentId;

    /**
     * 
     */
    public TripMapBean() {
    }

    /**
     * @return the zoom
     */
    public int getZoom() {
        return zoom;
    }

    /**
     * @param zoom the zoom to set
     */
    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    /**
     * @return the gmapkey
     */
    public String getGmapkey() {
        return gmapkey;
    }

    /**
     * @param gmapkey the gmapkey to set
     */
    public void setGmapkey(String gmapkey) {
        this.gmapkey = gmapkey;
    }

    /**
     * @return the currentPlace
     */
    public String getCurrentPlace() {
        return currentPlace;
    }

    /**
     * @param currentPlace the currentPlace to set
     */
    public void setCurrentPlace(String currentPlace) {
        this.currentPlace = currentPlace;
    }

    /**
     * @return the currentId
     */
    public int getCurrentId() {
        return currentId;
    }

    /**
     * @param currentId the currentId to set
     */
    public void setCurrentId(int currentId) {
        this.currentId = currentId;
    }
}
