//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.21 at 09:26:24 AM EDT 
//


package org.eclipse.ptp.rm.lml.core.elements;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for scheme_element2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="scheme_element2">
 *   &lt;complexContent>
 *     &lt;extension base="{http://eclipse.org/ptp/lml}scheme_element">
 *       &lt;sequence>
 *         &lt;element name="el3" type="{http://eclipse.org/ptp/lml}scheme_element3" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scheme_element2", propOrder = {
    "el3"
})
public class SchemeElement2
    extends SchemeElement
{

    protected List<SchemeElement3> el3;

    /**
     * Gets the value of the el3 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the el3 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEl3().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SchemeElement3 }
     * 
     * 
     */
    public List<SchemeElement3> getEl3() {
        if (el3 == null) {
            el3 = new ArrayList<SchemeElement3>();
        }
        return this.el3;
    }

}
