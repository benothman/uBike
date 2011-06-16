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
package com.ubike.faces.component.password;

import com.ubike.faces.component.captcha.CaptchaRenderer;
import java.util.logging.Logger;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

/**
 * {@code PasswordRenderer}
 * <p/>
 *
 * Created on Jun 12, 2011 at 1:17:03 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@FacesRenderer(componentFamily = "com.ubike.faces.component",
rendererType = "com.ubike.faces.component.password")
public class PasswordRenderer extends Renderer {

    private static final Logger logger = Logger.getLogger(PasswordRenderer.class.getName());
    public static final String RENDERER_TYPE = "com.ubike.faces.component.password";

    /**
     * Create a new instance of {@code PasswordRenderer}
     */
    public PasswordRenderer() {
        super();
    }
}
