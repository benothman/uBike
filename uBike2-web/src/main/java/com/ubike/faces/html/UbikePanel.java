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
package com.ubike.faces.html;

import java.io.IOException;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * {@code UbikePanel}
 * <p/>
 *
 * Created on Jun 10, 2011 at 7:20:18 PM
 *
 * @author <a href="mailto:nabil.benothman@gmail.com">Nabil Benothman</a>
 */
@FacesComponent(value = "com.ubike.faces.component.Panel")
public class UbikePanel extends UIComponentBase {

    public static final String COMPONENT_FAMILY = "com.ubike.faces.component";
    public static final String COMPONENT_TYPE = "com.ubike.faces.component.Panel";
    private String header;
    private String styleClass;
    private String style;
    private String headerClass;
    private String headerStyle;
    private String bodyClass;
    private String bodyStyle;

    /**
     * Create a new instance of {@code UbikePanel}
     */
    public UbikePanel() {
        super();
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext fc) throws IOException {
        ResponseWriter writer = fc.getResponseWriter();

        // Start of the panel
        writer.startElement("div", null);
        writer.writeAttribute("id", getId(), "id");
        String pClass = "ub-p";
        if (getStyleClass() != null && !getStyleClass().matches("\\s*")) {
            pClass += " " + getStyleClass().replaceAll(",", " ");
        }

        writer.writeAttribute("class", pClass, "class");
        if (getStyle() != null && !getStyle().matches("\\s*")) {
            writer.writeAttribute("style", getStyle(), "style");
        }

        // start of the panel header
        writer.startElement("div", null);
        writer.writeAttribute("id", getId() + "_header", "id");
        pClass = "ub-p-h";
        if (getHeaderClass() != null && !getHeaderClass().matches("\\s*")) {
            pClass += " " + getHeaderClass().replaceAll(",", " ");
        }
        writer.writeAttribute("class", pClass, "class");
        if (getHeaderStyle() != null && !getHeaderStyle().matches("\\s*")) {
            writer.writeAttribute("style", getHeaderStyle(), "style");
        }

        // Write the content of the header
        writer.write(this.getHeader());

        // End of the panel header
        writer.endElement("div");

        // start of the panel body 
        writer.startElement("div", null);
        writer.writeAttribute("id", getId() + "_body", "id");

        pClass = "ub-p-b";
        if (getBodyClass() != null && !getBodyClass().matches("\\s*")) {
            pClass += " " + getBodyClass().replaceAll(",", " ");
        }
        writer.writeAttribute("class", pClass, "class");
        if (getBodyStyle() != null && !getBodyStyle().matches("\\s*")) {
            writer.writeAttribute("style", getBodyStyle(), "style");
        }

        super.encodeBegin(fc);
    }

    @Override
    public void encodeEnd(FacesContext fc) throws IOException {
        ResponseWriter writer = fc.getResponseWriter();
        writer.endElement("div"); // End of the panel body
        writer.endElement("div"); // end of the panel
        super.encodeEnd(fc);
    }

    /**
     * @return the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * @param header the header to set
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * @return the styleClass
     */
    public String getStyleClass() {
        return styleClass;
    }

    /**
     * @param styleClass the styleClass to set
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * @param style the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * @return the headerClass
     */
    public String getHeaderClass() {
        return headerClass;
    }

    /**
     * @param headerClass the headerClass to set
     */
    public void setHeaderClass(String headerClass) {
        this.headerClass = headerClass;
    }

    /**
     * @return the headerStyle
     */
    public String getHeaderStyle() {
        return headerStyle;
    }

    /**
     * @param headerStyle the headerStyle to set
     */
    public void setHeaderStyle(String headerStyle) {
        this.headerStyle = headerStyle;
    }

    /**
     * @return the bodyClass
     */
    public String getBodyClass() {
        return bodyClass;
    }

    /**
     * @param bodyClass the bodyClass to set
     */
    public void setBodyClass(String bodyClass) {
        this.bodyClass = bodyClass;
    }

    /**
     * @return the bodyStyle
     */
    public String getBodyStyle() {
        return bodyStyle;
    }

    /**
     * @param bodyStyle the bodyStyle to set
     */
    public void setBodyStyle(String bodyStyle) {
        this.bodyStyle = bodyStyle;
    }
}
