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

import com.sun.jersey.api.container.ContainerException;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.spi.template.TemplateProcessor;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 * This classe can be used with the implicit MVCJ
 *
 * @author Benothman
 */
@Provider
public class XHTMLFacletTemplateProcessor implements TemplateProcessor {

    @Context
    private HttpContext hc;
    @Context
    private ServletContext servletContext;
    @Context
    private ThreadLocal<HttpServletRequest> requestInvoker;
    @Context
    private ThreadLocal<HttpServletResponse> responseInvoker;
    private final String basePath = "";

    /**
     *
     * @param path
     * @return
     */
    public String resolve(String path) {
        try {
            if (!path.endsWith(".xhtml")) {
                path = path + ".xhtml";
                if (servletContext.getResource(path) != null) {
                    return path.replace(".xhtml", ".jsf");
                }
            }
        } catch (MalformedURLException ex) {
            // TODO log
        }

        return null;
    }

    /**
     *
     * @param resolvedPath
     * @param model
     * @param out
     * @throws java.io.IOException
     */
    public void writeTo(String resolvedPath, Object model, OutputStream out)
            throws IOException {
        // Commit the status and headers to the HttpServletResponse
        out.flush();

        RequestDispatcher d = servletContext.getRequestDispatcher(resolvedPath);
        if (d == null) {
            throw new ContainerException(
                    "No request dispatcher for: " + resolvedPath);
        }

        d = new RequestDispatcherWrapper(d, hc, model);

        try {
            d.forward(requestInvoker.get(), responseInvoker.get());
        } catch (Exception e) {
            throw new ContainerException(e);
        }
    }
}
