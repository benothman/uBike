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
import java.util.ResourceBundle;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

/**
 * {@code BubbleHtmlInputSecret}
 *
 * <p>Custom component, rendering a an input password field with a bubble info message.
 * The info message is a simple {@link HtmlRichMessage} (customized) showing the
 * label of the {@link UIComponent} and appears when the input text field gain the
 * focus. If the password field is required and/or supports conversion/validation, the
 * label message will be replaced by the conversion/validation message. The bubble
 * info message disappears one second after the input password field loose the focus
 * </p>
 * 
 * Created on Dec 7, 2010, 10:13:32 PM
 *
 * @author Nabil Benothman
 * @version 1.0
 */
public class BubbleHtmlInputSecret extends HtmlInputSecret {

    public static final String UI_BUBBLE_INFO_FAMILY = "BIFAMILY";
    public static final String COMPONENT_TYPE = "org.jboss.gatein.jsf.html.GateInBubbleHtmlInputSecret";
    private HtmlRichMessage htmlMessgae;
    private String labelStyle;
    private String labelClass;

    /**
     * Create a new instance of {@code GateInHtmlInputSecret}
     */
    public BubbleHtmlInputSecret() {
        super();
        this.setValue(this.getLabel());
        this.htmlMessgae = new HtmlRichMessage();
    }

    @Override
    public void encodeBegin(FacesContext fc) throws IOException {
        ResponseWriter writer = fc.getResponseWriter();
        String reqCtxPath = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();

        writer.startElement("br", null);
        writer.endElement("br");
        writer.startElement("div", null);
        writer.writeAttribute("class", "bubbleInfo", "class");

        writer.startElement("table", null);

        writer.writeAttribute("id", getId() + ":" + "dpopd", "id");
        writer.writeAttribute("class", "popup", "class");
        writer.writeAttribute("border", "0", "border");
        writer.writeAttribute("cellpadding", "0", "cellpadding");
        writer.writeAttribute("cellspacing", "0", "cellspacing");
        writer.startElement("tbody", null);
        writer.startElement("tr", null);

        writer.startElement("td", null);
        writer.writeAttribute("id", "topleft", "id");
        writer.writeAttribute("class", "corner", "class");
        writer.endElement("td");

        writer.startElement("td", null);
        writer.writeAttribute("class", "top", "class");
        writer.endElement("td");

        writer.startElement("td", null);
        writer.writeAttribute("id", "topright", "id");
        writer.writeAttribute("class", "corner", "class");
        writer.endElement("td");

        writer.endElement("tr");

        writer.startElement("tr", null);
        writer.startElement("td", null);
        writer.writeAttribute("class", "left", "class");
        writer.endElement("td");
        writer.startElement("td", null);

        writer.startElement("table", null);
        writer.writeAttribute("class", "popup-contents", "class");
        writer.writeAttribute("border", "0", "border");
        writer.writeAttribute("bgcolor", "#FFFFFF", "bgcolor");
        writer.writeAttribute("cellpadding", "0", "cellpadding");
        writer.writeAttribute("cellspacing", "0", "cellspacing");

        writer.startElement("tbody", null);

        writer.startElement("tr", null);
        writer.writeAttribute("id", "release-notes", "id");

        writer.startElement("th", null);
        writer.write("&#160;");
        writer.endElement("th");

        writer.startElement("td", null);
        this.initMessage(fc);
        this.htmlMessgae.encodeAll(fc);
        writer.endElement("td");

        writer.endElement("tr");
        writer.endElement("tbody");
        writer.endElement("table");
        writer.endElement("td");

        writer.startElement("td", null);
        writer.writeAttribute("class", "right", "class");
        writer.endElement("td");
        writer.endElement("tr");

        writer.startElement("tr", null);
        writer.startElement("td", null);
        writer.writeAttribute("class", "corner", "class");
        writer.writeAttribute("id", "bottomleft", "id");
        writer.endElement("td");

        writer.startElement("td", null);
        writer.writeAttribute("class", "bottom", "class");

        writer.startElement("img", null);
        writer.writeAttribute("width", "30", "width");
        writer.writeAttribute("height", "29", "height");
        writer.writeAttribute("alt", "popup tail", "alt");
        writer.writeAttribute("src", reqCtxPath + "/images/bubble/bubble-bottom-middle.png", "src");
        writer.endElement("img");

        writer.endElement("td");

        writer.startElement("td", null);
        writer.writeAttribute("class", "corner", "class");
        writer.writeAttribute("id", "bottomright", "id");
        writer.endElement("td");
        writer.endElement("tr");
        writer.endElement("tbody");
        writer.endElement("table");

        writer.startElement("br", null);
        writer.endElement("br");

        if (this.getLabel() != null) {
            writer.startElement("span", null);

            if (this.labelClass != null) {
                writer.writeAttribute("class", this.labelClass, "class");
            }
            if (this.labelStyle != null && this.labelStyle.length() > 0) {
                writer.writeAttribute("style", this.labelStyle, "style");
            }

            writer.write(this.getLabel() + " : ");
            writer.endElement("span");
            writer.startElement("br", null);
            writer.endElement("br");
        }

        writer.startElement("span", null);
        super.encodeBegin(fc);
    }

    @Override
    public void encodeEnd(FacesContext fc) throws IOException {
        ResponseWriter writer = fc.getResponseWriter();
        super.encodeEnd(fc);
        writer.endElement("span");
        writer.endElement("div");
    }

    @Override
    public Object getSubmittedValue() {
        String value = (String) super.getSubmittedValue();
        String label = getLabel();
        if (value != null && value.trim().equalsIgnoreCase(label)) {
            return "";
        }

        return value;
    }

    @Override
    public Object getValue() {
        Object val = super.getValue();
        if (val == null) {
            return this.getLabel();
        }
        return val;
    }

    /**
     * Initialize the HTML message parameters before encoding it
     */
    private void initMessage(FacesContext fc) {
        UIComponent tmp = null;
        for (UIComponent uic : this.getChildren()) {
            if (uic instanceof HtmlRichMessage) {
                tmp = uic;
                break;
            }
        }

        if (tmp != null) {
            this.htmlMessgae = (HtmlRichMessage) tmp;
        } else {
            this.getChildren().add(this.htmlMessgae);
            this.htmlMessgae.setFor(this.getId());
        }

        String passedLabel = this.getLabel() != null ? this.getLabel() : " ";
        if (this.isRequired()) {
            ResourceBundle resourceBundle = FacesContext.getCurrentInstance().getApplication().getResourceBundle(fc, "msg");
            String text = null;
            if (resourceBundle != null) {
                text = resourceBundle.getString("gatein.input.field");
            }
            if (text == null) {
                text = "required field";
            }

            passedLabel += " : " + text;
        }
    }

    /**
     * @return the htmlMessgae
     */
    public HtmlRichMessage getHtmlMessgae() {
        return htmlMessgae;
    }

    /**
     * @return the labelStyle
     */
    public String getLabelStyle() {
        return labelStyle;
    }

    /**
     * @param labelStyle the labelStyle to set
     */
    public void setLabelStyle(String labelStyle) {
        this.labelStyle = labelStyle;
    }

    /**
     * Getter for the label class
     *
     * @return The label class
     */
    public String getLabelClass() {
        return labelClass;
    }

    /**
     * Setter for the label class
     *
     * @param labelClass the label class to set
     */
    public void setLabelClass(String labelClass) {
        this.labelClass = labelClass;
    }
}
