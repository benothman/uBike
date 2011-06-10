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

import javax.ws.rs.WebApplicationException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import javax.xml.bind.JAXBContext;

/**
 * Utility class for resolving an uri into an entity.
 *
 * @author Benothman
 */
public class UriResolver {
    
    private static ThreadLocal<UriResolver> instance = new ThreadLocal<UriResolver>() {
        protected UriResolver initialValue() {
            return new UriResolver();
        }
    };
    
    private boolean inProgress = false;
    
    private UriResolver() {
    }
    
    /**
     * Returns an instance of UriResolver.
     *
     * @return an instance of UriResolver.
     */
    public static UriResolver getInstance() {
        return instance.get();
    }
    
    private static void removeInstance() {
        instance.remove();
    }
    
    /**
     * Returns the entity associated with the given uri.
     *
     * @param type the converter class used to unmarshal the entity from XML
     * @param uri the uri identifying the entity
     * @return the entity associated with the given uri
     */
    public <T> T resolve(Class<T> type, URI uri) {
        if (inProgress) return null;
        
        inProgress = true;
        
        try {
            if (uri == null) {
                throw new RuntimeException("No uri specified in a reference.");
            }
            
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            
            if (conn.getResponseCode() == 200) {
                JAXBContext context = JAXBContext.newInstance(type);

                return (T) context.createUnmarshaller().unmarshal(conn.getInputStream());
            } else {
                throw new WebApplicationException(new Throwable("Resource for " + uri + " does not exist."), 404);
            }
        } catch (WebApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new WebApplicationException(ex);
        } finally {
            removeInstance();
        }
    }
}