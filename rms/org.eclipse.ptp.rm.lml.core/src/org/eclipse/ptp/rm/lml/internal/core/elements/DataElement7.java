//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.30 at 02:35:58 PM CEST 
//


package org.eclipse.ptp.rm.lml.internal.core.elements;

import java.io.Serializable;


import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for data_element7 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="data_element7">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.llview.de}data_element">
 *       &lt;sequence>
 *         &lt;element name="el8" type="{http://www.llview.de}data_element8" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "data_element7", propOrder = {
    "el8"
})
public class DataElement7
    extends DataElement
 implements Serializable {

    protected List<DataElement8> el8;

    /**
     * Gets the value of the el8 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the el8 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEl8().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataElement8 }
     * 
     * 
     */
    public List<DataElement8> getEl8() {
        if (el8 == null) {
            el8 = new ArrayList<DataElement8>();
        }
        return this.el8;
    }

}
