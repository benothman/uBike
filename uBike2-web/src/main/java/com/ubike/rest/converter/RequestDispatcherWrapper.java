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

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.spi.template.ResolvedViewable;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.core.Context;

/**
 *
 * @author Benothman
 */
public final class RequestDispatcherWrapper implements RequestDispatcher {

    @Context
    private final HttpContext hc;
    private final RequestDispatcher d;
    private final Object it;

    public RequestDispatcherWrapper(RequestDispatcher d, HttpContext hc,
            Object it) {
        this.d = d;
        this.hc = hc;
        this.it = it;
    }

    public void forward(ServletRequest req, ServletResponse rsp) throws
            ServletException, IOException {
        ResolvedViewable rv = (ResolvedViewable) hc.getProperties().get(
                "com.sun.jersey.spi.template.ResolvedViewable");

        req.setAttribute("httpContext", hc);
        req.setAttribute("resolvingClass", rv.getResolvingClass());
        req.setAttribute("it", it);
        req.setAttribute("_request", req);
        req.setAttribute("_response", rsp);
        d.forward(req, rsp);
    }

    public void include(ServletRequest req, ServletResponse rsp) throws
            ServletException, IOException {
        throw new UnsupportedOperationException();
    }
}