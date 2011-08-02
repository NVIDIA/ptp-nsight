//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.01 at 05:21:43 PM CDT 
//


package org.eclipse.ptp.rm.jaxb.core.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for monitor-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="monitor-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="property" type="{http://org.eclipse.ptp/rm}property-type" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="driver" type="{http://org.eclipse.ptp/rm}monitor-driver-type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="refreshFrequencyInSeconds" type="{http://www.w3.org/2001/XMLSchema}int" default="60" />
 *       &lt;attribute name="schedulerType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "monitor-type", propOrder = {
    "property",
    "driver"
})
public class MonitorType {

    protected List<PropertyType> property;
    protected MonitorDriverType driver;
    @XmlAttribute
    protected Integer refreshFrequencyInSeconds;
    @XmlAttribute
    protected String schedulerType;

    /**
     * Gets the value of the property property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the property property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyType }
     * 
     * 
     */
    public List<PropertyType> getProperty() {
        if (property == null) {
            property = new ArrayList<PropertyType>();
        }
        return this.property;
    }

    /**
     * Gets the value of the driver property.
     * 
     * @return
     *     possible object is
     *     {@link MonitorDriverType }
     *     
     */
    public MonitorDriverType getDriver() {
        return driver;
    }

    /**
     * Sets the value of the driver property.
     * 
     * @param value
     *     allowed object is
     *     {@link MonitorDriverType }
     *     
     */
    public void setDriver(MonitorDriverType value) {
        this.driver = value;
    }

    /**
     * Gets the value of the refreshFrequencyInSeconds property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getRefreshFrequencyInSeconds() {
        if (refreshFrequencyInSeconds == null) {
            return  60;
        } else {
            return refreshFrequencyInSeconds;
        }
    }

    /**
     * Sets the value of the refreshFrequencyInSeconds property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRefreshFrequencyInSeconds(Integer value) {
        this.refreshFrequencyInSeconds = value;
    }

    /**
     * Gets the value of the schedulerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSchedulerType() {
        return schedulerType;
    }

    /**
     * Sets the value of the schedulerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSchedulerType(String value) {
        this.schedulerType = value;
    }

}
