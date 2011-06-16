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
package com.ubike.faces.validator;

import com.ubike.faces.component.captcha.Captcha;
import com.ubike.faces.component.captcha.Verification;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;

/**
 * {@code CaptchaValidator}
 * <p/>
 *
 * Created on Jun 10, 2011 at 11:11:54 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public class CaptchaValidator implements Validator {

    private static final Logger logger = Logger.getLogger(CaptchaValidator.class.getName());

    /**
     * Create a new instance of {@code CaptchaValidator}
     */
    public CaptchaValidator() {
        super();
    }

    @Override
    public void validate(FacesContext fc, UIComponent component, Object value)
            throws ValidatorException {

        String result = null;
        Verification verification = (Verification) value;

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
            throw new RuntimeException(exception);
        }

        boolean isValid = Boolean.valueOf(result);

        if (!isValid) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "The security question answer is not correct",
                    "The security question answer is not correct");
            logger.log(Level.WARNING, "The answer {0} is not correct", verification.getAnswer());
            throw new ValidatorException(message);
        }
        logger.log(Level.INFO, "The answer {0} is correct", verification.getAnswer());
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
        String privateKey = facesContext.getExternalContext().getInitParameter(Captcha.PRIVATE_KEY);

        StringBuilder postParams = new StringBuilder();
        postParams.append("privatekey=").append(URLEncoder.encode(privateKey, "UTF-8"));
        postParams.append("&remoteip=").append(URLEncoder.encode(remoteAddress, "UTF-8"));
        postParams.append("&challenge=").append(URLEncoder.encode(challenge, "UTF-8"));
        postParams.append("&response=").append(URLEncoder.encode(answer, "UTF-8"));

        return postParams.toString();
    }
}
