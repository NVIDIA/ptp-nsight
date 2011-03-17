//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.10 at 02:31:08 PM CET 
//


package org.eclipse.ptp.rm.lml.internal.core.elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



/**
 * 
 *     			Representation of the overview about cpus and which jobs
 *     			are running on which cpu/node. Designed as tree.
 *     			Scheme-tag describes an empty batch-system, data-tag the
 *     			actual situation in this system. Data-tag connects
 *     			elements in the nodedisplay with jobs, which are running
 *     			in the system.
 *     		
 * 
 * <p>Java class for nodedisplay complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="nodedisplay">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.llview.de}gobject_type">
 *       &lt;sequence>
 *         &lt;element name="scheme" type="{http://www.llview.de}scheme_type"/>
 *         &lt;element name="data" type="{http://www.llview.de}data_type"/>
 *       &lt;/sequence>
 *       &lt;attribute name="refto" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nodedisplay", propOrder = {
    "scheme",
    "data"
})
public class Nodedisplay
    extends GobjectType
{

    @XmlElement(required = true)
    protected SchemeType scheme;
    @XmlElement(required = true)
    protected DataType data;
    @XmlAttribute
    protected String refto;

    /**
     * Gets the value of the scheme property.
     * 
     * @return
     *     possible object is
     *     {@link SchemeType }
     *     
     */
    public SchemeType getScheme() {
        return scheme;
    }

    /**
     * Sets the value of the scheme property.
     * 
     * @param value
     *     allowed object is
     *     {@link SchemeType }
     *     
     */
    public void setScheme(SchemeType value) {
        this.scheme = value;
    }

    /**
     * Gets the value of the data property.
     * 
     * @return
     *     possible object is
     *     {@link DataType }
     *     
     */
    public DataType getData() {
        return data;
    }

    /**
     * Sets the value of the data property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataType }
     *     
     */
    public void setData(DataType value) {
        this.data = value;
    }

    /**
     * Gets the value of the refto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefto() {
        return refto;
    }

    /**
     * Sets the value of the refto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefto(String value) {
        this.refto = value;
    }

}
