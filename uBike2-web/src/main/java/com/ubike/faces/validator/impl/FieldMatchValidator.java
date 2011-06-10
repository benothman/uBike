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
package com.ubike.faces.validator.impl;

import com.ubike.faces.validator.FieldMatch;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

/**
 * {@code FieldMatchValidator}
 * <p/>
 *
 * Created on Jun 9, 2011 at 6:03:16 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;
    private Class beanClass;
    private String beanName;

    /**
     * Create a new instance of {@code FieldMatchValidator}
     */
    public FieldMatchValidator() {
        super();
    }

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
        beanClass = constraintAnnotation.type();
        beanName = constraintAnnotation.name();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ApplicationContext ctx = FacesContextUtils.getWebApplicationContext(fc);
            Object bean = ctx.getBean(beanName, beanClass);
            
            
            //final Object firstObj = BeanUtils.getProperty(value, firstFieldName);
            //final Object secondObj = BeanUtils.getProperty(value, secondFieldName);

            //return firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
            return true;
        } catch (final Exception ignore) {
            // ignore
        }
        return true;
    }
}
