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

import com.ubike.faces.validator.Equals;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * {@code EqualsValidator}
 * <p/>
 *
 * Created on Jun 9, 2011 at 5:35:26 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
public class EqualsValidator implements ConstraintValidator<Equals, String> {

    private String pattern;
    private boolean ignoreCase;

    /**
     * Create a new instance of {@code EqualsValidator}
     */
    public EqualsValidator() {
        super();
    }

    @Override
    public void initialize(Equals constraintAnnotation) {
        this.pattern = constraintAnnotation.value();
        this.ignoreCase = constraintAnnotation.ignoreCase();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return this.ignoreCase ? this.pattern.equalsIgnoreCase(value) : this.pattern.equals(value);
    }
}
