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
package com.ubike.faces.component.captcha;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

/**
 * {@code CaptchaRenderer}
 * <p/>
 *
 * Created on Jun 9, 2011 at 7:38:45 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@FacesRenderer(componentFamily = "com.ubike.faces.component",
rendererType = "com.ubike.faces.component.captcha")
public class CaptchaRenderer extends Renderer {

    private static final Logger logger = Logger.getLogger(CaptchaRenderer.class.getName());
    public static final String RENDERER_TYPE = "com.ubike.faces.component.captcha";

    /**
     * Create a new instance of {@code CaptchaRenderer}
     */
    public CaptchaRenderer() {
        super();
        logger.log(Level.INFO, "Create a new instance of {0}", getClass().getName());
    }

    @Override
    public void decode(FacesContext fc, UIComponent component) {
        Captcha captcha = (Captcha) component;
        Map<String, String> params = fc.getExternalContext().getRequestParameterMap();

        String challenge = params.get(Captcha.CHALLENGE_FIELD);
        String answer = params.get(Captcha.RESPONSE_FIELD);

        if (answer != null) {
            if (answer.equals("")) {
                captcha.setSubmittedValue(answer);
            } else {
                captcha.setSubmittedValue(new Verification(challenge, answer));
            }
        }
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Captcha captcha = (Captcha) component;
        captcha.setRequired(true);
        String protocol = captcha.isSecure() ? "https" : "http";

        String publicKey = getPublicKey(context, captcha);

        if (publicKey == null) {
            throw new FacesException("Cannot find public key for catpcha, "
                    + "use com.ubike.PUBLIC_CAPTCHA_KEY context-param to define one");
        }

        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);

        writer.write("var RecaptchaOptions = {");
        writer.write("theme:\"" + captcha.getTheme() + "\"");
        writer.write(",lang:\"" + captcha.getLanguage() + "\"");
        if (captcha.getTabindex() != 0) {
            writer.write(",tabIndex:" + captcha.getTabindex());
        }
        writer.write("};");
        writer.endElement("script");

        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.writeAttribute("src", protocol + "://www.google.com/recaptcha/api/challenge?k=" + publicKey, null);
        writer.endElement("script");

        writer.startElement("noscript", null);
        writer.startElement("iframe", null);
        writer.writeAttribute("src", protocol + "://www.google.com/recaptcha/api/noscript?k=" + publicKey, null);
        writer.endElement("iframe");

        writer.startElement("textarea", null);
        writer.writeAttribute("id", Captcha.CHALLENGE_FIELD, null);
        writer.writeAttribute("name", Captcha.CHALLENGE_FIELD, null);
        writer.writeAttribute("rows", "3", null);
        writer.writeAttribute("columns", "40", null);
        writer.endElement("textarea");

        writer.startElement("input", null);
        writer.writeAttribute("id", Captcha.RESPONSE_FIELD, null);
        writer.writeAttribute("name", Captcha.RESPONSE_FIELD, null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", "manual_challenge", null);
        writer.endElement("input");

        writer.endElement("noscript");
    }

    /**
     * 
     * @param context
     * @param captcha
     * @return 
     */
    protected String getPublicKey(FacesContext context, Captcha captcha) {
        String key = captcha.getPublicKey();

        if (key != null) {
            logger.warning("PublicKey definition on captcha is deprecated, "
                    + "use com.ubike.PUBLIC_CAPTCHA_KEY context-param instead");

            return key;
        } else {
            return context.getExternalContext().getInitParameter(Captcha.PUBLIC_KEY);
        }
    }
}
