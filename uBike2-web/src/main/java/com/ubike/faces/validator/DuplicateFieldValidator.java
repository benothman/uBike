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

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * {@code DuplicateFieldValidator}
 * <p/>
 *
 * Created on Jun 9, 2011 at 6:29:54 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@FacesValidator(value = "duplicateFieldValidator")
public class DuplicateFieldValidator implements Validator {

    /**
     * Create a new instance of {@code DuplicateFieldValidator}
     */
    public DuplicateFieldValidator() {
        super();
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        // Obtain the client ID of the first field from f:attribute.
        System.out.println(component.getFamily());
        String field1Id = (String) component.getAttributes().get("field1Id");

        // Find the actual JSF component for the client ID.
        UIInput textInput = (UIInput) context.getViewRoot().findComponent(field1Id);
        if (textInput == null) {
            throw new IllegalArgumentException(String.format("Unable to find component with id %s", field1Id));
        }
        // Get its value, the entered text of the first field.
        String field1 = (String) textInput.getValue();

        // Cast the value of the entered text of the second field back to String.
        String confirm = (String) value;

        // Check if the first text is actually entered and compare it with second text.
        if (field1 != null && field1.length() != 0 && !field1.equals(confirm)) {
            throw new ValidatorException(new FacesMessage("E-mail addresses are not equal."));
        }
    }
}
