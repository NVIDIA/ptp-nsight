//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.01 at 05:21:43 PM CDT 
//


package org.eclipse.ptp.rm.jaxb.core.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for form-data-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="form-data-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="top" type="{http://org.eclipse.ptp/rm}form-attachment-type" minOccurs="0"/>
 *         &lt;element name="bottom" type="{http://org.eclipse.ptp/rm}form-attachment-type" minOccurs="0"/>
 *         &lt;element name="left" type="{http://org.eclipse.ptp/rm}form-attachment-type" minOccurs="0"/>
 *         &lt;element name="right" type="{http://org.eclipse.ptp/rm}form-attachment-type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="height" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}int" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "form-data-type", propOrder = {
    "top",
    "bottom",
    "left",
    "right"
})
public class FormDataType {

    protected FormAttachmentType top;
    protected FormAttachmentType bottom;
    protected FormAttachmentType left;
    protected FormAttachmentType right;
    @XmlAttribute
    protected Integer height;
    @XmlAttribute
    protected Integer width;

    /**
     * Gets the value of the top property.
     * 
     * @return
     *     possible object is
     *     {@link FormAttachmentType }
     *     
     */
    public FormAttachmentType getTop() {
        return top;
    }

    /**
     * Sets the value of the top property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormAttachmentType }
     *     
     */
    public void setTop(FormAttachmentType value) {
        this.top = value;
    }

    /**
     * Gets the value of the bottom property.
     * 
     * @return
     *     possible object is
     *     {@link FormAttachmentType }
     *     
     */
    public FormAttachmentType getBottom() {
        return bottom;
    }

    /**
     * Sets the value of the bottom property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormAttachmentType }
     *     
     */
    public void setBottom(FormAttachmentType value) {
        this.bottom = value;
    }

    /**
     * Gets the value of the left property.
     * 
     * @return
     *     possible object is
     *     {@link FormAttachmentType }
     *     
     */
    public FormAttachmentType getLeft() {
        return left;
    }

    /**
     * Sets the value of the left property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormAttachmentType }
     *     
     */
    public void setLeft(FormAttachmentType value) {
        this.left = value;
    }

    /**
     * Gets the value of the right property.
     * 
     * @return
     *     possible object is
     *     {@link FormAttachmentType }
     *     
     */
    public FormAttachmentType getRight() {
        return right;
    }

    /**
     * Sets the value of the right property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormAttachmentType }
     *     
     */
    public void setRight(FormAttachmentType value) {
        this.right = value;
    }

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHeight(Integer value) {
        this.height = value;
    }

    /**
     * Gets the value of the width property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Sets the value of the width property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setWidth(Integer value) {
        this.width = value;
    }

}
