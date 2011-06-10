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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

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
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {
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

    @Override
    public void validate(FacesContext fc) {
        Object submittedValue = getSubmittedValue();
        if (submittedValue == null) { // the value was not submitted at all
            return;
        }

        super.validate(fc);

        if (isValid()) {

            String result = null;
            Verification verification = (Verification) submittedValue;

            try {
                URL url = new URL("http://api-verify.recaptcha.net/verify");
                URLConnection conn = url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                String postBody = createPostParameters(fc, verification);

                OutputStream out = conn.getOutputStream();
                out.write(postBody.getBytes());
                out.flush();
                out.close();
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = rd.readLine();
                rd.close();
            } catch (Exception exception) {
                throw new FacesException(exception);
            }

            boolean isValid = Boolean.valueOf(result);

            if (!isValid) {
                setValid(false);

                String validatorMessage = getValidatorMessage();
                FacesMessage msg = null;

                if (validatorMessage == null) {
                    validatorMessage = "The security answer is not correct";
                }

                msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, validatorMessage, validatorMessage);
                fc.addMessage(getClientId(fc), msg);
            }
        }
    }

    /**
     * 
     * @param facesContext
     * @param verification
     * @return
     * @throws UnsupportedEncodingException 
     */
    private String createPostParameters(FacesContext facesContext, Verification verification) throws UnsupportedEncodingException {
        String challenge = verification.getChallenge();
        String answer = verification.getAnswer();
        String remoteAddress = ((HttpServletRequest) facesContext.getExternalContext().getRequest()).getRemoteAddr();
        String privateKeyParam = facesContext.getExternalContext().getInitParameter(Captcha.PRIVATE_KEY);

        if (privateKeyParam == null) {
            throw new FacesException("Cannot find private key for catpcha, use "
                    + PRIVATE_KEY + " context-param to define one");
        }

        StringBuilder postParams = new StringBuilder();
        postParams.append("privatekey=").append(URLEncoder.encode(privateKeyParam, "UTF-8"));
        postParams.append("&remoteip=").append(URLEncoder.encode(remoteAddress, "UTF-8"));
        postParams.append("&challenge=").append(URLEncoder.encode(challenge, "UTF-8"));
        postParams.append("&response=").append(URLEncoder.encode(answer, "UTF-8"));

        return postParams.toString();
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
