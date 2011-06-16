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

import com.ubike.faces.validator.CaptchaValidator;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 * {@code Captcha}
 * <p/>
 *
 * Created on Jun 9, 2011 at 7:14:49 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@FacesComponent(value = "com.ubike.faces.component.Captcha")
public class Captcha extends UIInput {

    public static final String COMPONENT_FAMILY = "com.ubike.faces.component";
    public static final String COMPONENT_TYPE = "com.ubike.faces.component.Captcha";
    public static final String PUBLIC_KEY = "com.ubike.PUBLIC_CAPTCHA_KEY";
    public static final String PRIVATE_KEY = "com.ubike.PRIVATE_CAPTCHA_KEY";
    public final static String CHALLENGE_FIELD = "recaptcha_challenge_field";
    public final static String RESPONSE_FIELD = "recaptcha_response_field";
    private static final Logger logger = Logger.getLogger(Captcha.class.getName());
    private String language;
    private String publicKey;
    private String privateKey;
    private String theme;
    private boolean secure;
    private int tabindex;

    /**
     * Create a new instance of {@code Captcha}
     */
    public Captcha() {
        super();
        logger.log(Level.INFO, "Create a new instance of {0}", getClass().getName());
        this.addValidator(new CaptchaValidator());
    }

    @Override
    public void encodeBegin(FacesContext fc) throws IOException {
        super.encodeBegin(fc);
    }

    @Override
    public void encodeEnd(FacesContext fc) throws IOException {
        super.encodeEnd(fc);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public String getRendererType() {
        return CaptchaRenderer.RENDERER_TYPE;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object values[] = new Object[7];
        values[0] = super.saveState(context);
        values[1] = language;
        values[2] = publicKey;
        values[3] = privateKey;
        values[4] = theme;
        values[5] = secure;
        values[6] = getTabindex();
        return values;
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        language = (String) values[1];
        publicKey = (String) values[2];
        privateKey = (String) values[3];
        theme = (String) values[4];
        secure = (Boolean) values[5];
        setTabindex((int) (Integer) values[6]);
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the publicKey
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @return the privateKey
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey the privateKey to set
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * @param theme the theme to set
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

    /**
     * @return the secure
     */
    public boolean isSecure() {
        return secure;
    }

    /**
     * @param secure the secure to set
     */
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    /**
     * @return the tabindex
     */
    public int getTabindex() {
        return tabindex;
    }

    /**
     * @param tabindex the tabindex to set
     */
    public void setTabindex(int tabindex) {
        this.tabindex = tabindex;
    }
}
