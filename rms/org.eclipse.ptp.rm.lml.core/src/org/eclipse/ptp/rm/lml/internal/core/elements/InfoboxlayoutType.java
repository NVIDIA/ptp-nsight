//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.10 at 02:31:08 PM CET 
//


package org.eclipse.ptp.rm.lml.internal.core.elements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;



/**
 * <p>Java class for infoboxlayout_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="infoboxlayout_type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.llview.de}componentlayout_type">
 *       &lt;sequence>
 *         &lt;element name="typecol" type="{http://www.llview.de}infoboxlayout_coltype" minOccurs="0"/>
 *         &lt;element name="infocol" type="{http://www.llview.de}infoboxlayout_coltype" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "infoboxlayout_type", propOrder = {
    "typecol",
    "infocol"
})
public class InfoboxlayoutType
    extends ComponentlayoutType
{

    protected InfoboxlayoutColtype typecol;
    protected InfoboxlayoutColtype infocol;

    /**
     * Gets the value of the typecol property.
     * 
     * @return
     *     possible object is
     *     {@link InfoboxlayoutColtype }
     *     
     */
    public InfoboxlayoutColtype getTypecol() {
        return typecol;
    }

    /**
     * Sets the value of the typecol property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoboxlayoutColtype }
     *     
     */
    public void setTypecol(InfoboxlayoutColtype value) {
        this.typecol = value;
    }

    /**
     * Gets the value of the infocol property.
     * 
     * @return
     *     possible object is
     *     {@link InfoboxlayoutColtype }
     *     
     */
    public InfoboxlayoutColtype getInfocol() {
        return infocol;
    }

    /**
     * Sets the value of the infocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoboxlayoutColtype }
     *     
     */
    public void setInfocol(InfoboxlayoutColtype value) {
        this.infocol = value;
    }

}
