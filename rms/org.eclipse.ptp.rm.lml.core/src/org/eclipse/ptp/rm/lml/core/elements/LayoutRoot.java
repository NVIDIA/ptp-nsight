//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.21 at 09:26:24 AM EDT 
//


package org.eclipse.ptp.rm.lml.core.elements;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for layout_root complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="layout_root">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="request" type="{http://eclipse.org/ptp/lml}RequestType" minOccurs="0"/>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;element name="textlayout" type="{http://eclipse.org/ptp/lml}infoboxlayout_type" minOccurs="0"/>
 *           &lt;element name="infoboxlayout" type="{http://eclipse.org/ptp/lml}infoboxlayout_type" minOccurs="0"/>
 *           &lt;element name="tablelayout" type="{http://eclipse.org/ptp/lml}tablelayout_type" minOccurs="0"/>
 *           &lt;element name="chartlayout" type="{http://eclipse.org/ptp/lml}chartlayout_type" minOccurs="0"/>
 *           &lt;element name="usagebarlayout" type="{http://eclipse.org/ptp/lml}usagebarlayout_type" minOccurs="0"/>
 *           &lt;element name="nodedisplaylayout" type="{http://eclipse.org/ptp/lml}nodedisplaylayout_type" minOccurs="0"/>
 *           &lt;element name="abslayout" type="{http://eclipse.org/ptp/lml}abslayout_type" minOccurs="0"/>
 *           &lt;element name="splitlayout" type="{http://eclipse.org/ptp/lml}splitlayout_type" minOccurs="0"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "layout_root", propOrder = {
    "request",
    "textlayoutAndInfoboxlayoutAndTablelayout"
})
public class LayoutRoot {

    protected RequestType request;
    @XmlElementRefs({
        @XmlElementRef(name = "splitlayout", type = JAXBElement.class),
        @XmlElementRef(name = "abslayout", type = JAXBElement.class),
        @XmlElementRef(name = "usagebarlayout", type = JAXBElement.class),
        @XmlElementRef(name = "nodedisplaylayout", type = JAXBElement.class),
        @XmlElementRef(name = "chartlayout", type = JAXBElement.class),
        @XmlElementRef(name = "textlayout", type = JAXBElement.class),
        @XmlElementRef(name = "infoboxlayout", type = JAXBElement.class),
        @XmlElementRef(name = "tablelayout", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> textlayoutAndInfoboxlayoutAndTablelayout;
    @XmlAttribute
    protected String version;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link RequestType }
     *     
     */
    public RequestType getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestType }
     *     
     */
    public void setRequest(RequestType value) {
        this.request = value;
    }

    /**
     * Gets the value of the textlayoutAndInfoboxlayoutAndTablelayout property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the textlayoutAndInfoboxlayoutAndTablelayout property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTextlayoutAndInfoboxlayoutAndTablelayout().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link SplitlayoutType }{@code >}
     * {@link JAXBElement }{@code <}{@link AbslayoutType }{@code >}
     * {@link JAXBElement }{@code <}{@link UsagebarlayoutType }{@code >}
     * {@link JAXBElement }{@code <}{@link NodedisplaylayoutType }{@code >}
     * {@link JAXBElement }{@code <}{@link ChartlayoutType }{@code >}
     * {@link JAXBElement }{@code <}{@link InfoboxlayoutType }{@code >}
     * {@link JAXBElement }{@code <}{@link InfoboxlayoutType }{@code >}
     * {@link JAXBElement }{@code <}{@link TablelayoutType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getTextlayoutAndInfoboxlayoutAndTablelayout() {
        if (textlayoutAndInfoboxlayoutAndTablelayout == null) {
            textlayoutAndInfoboxlayoutAndTablelayout = new ArrayList<JAXBElement<?>>();
        }
        return this.textlayoutAndInfoboxlayoutAndTablelayout;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
