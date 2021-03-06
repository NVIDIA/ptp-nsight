
package org.eclipse.ptp.internal.etfw.jaxb.data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AnalysisToolType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AnalysisToolType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="global" type="{http://eclipse.org/ptp/etfw}ToolAppType" minOccurs="0"/>
 *         &lt;element name="analysis-commands" type="{http://eclipse.org/ptp/etfw}ToolAppType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="for-all-like" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tool-state" type="{http://eclipse.org/ptp/etfw}ToolStateType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="tool-id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tool-name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="tool-type" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="require-true" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="depth" type="{http://www.w3.org/2001/XMLSchema}int" default="-1" />
 *       &lt;attribute name="useDefaultLocation" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="useLatestFileOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" />
 *       &lt;attribute name="set-success-attribute" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnalysisToolType", propOrder = {
    "global",
    "analysisCommands",
    "forAllLike",
    "toolState"
})
public class AnalysisToolType {

    protected ToolAppType global;
    @XmlElement(name = "analysis-commands")
    protected List<ToolAppType> analysisCommands;
    @XmlElement(name = "for-all-like")
    protected String forAllLike;
    @XmlElement(name = "tool-state")
    protected ToolStateType toolState;
    @XmlAttribute(name = "tool-id")
    protected String toolId;
    @XmlAttribute(name = "tool-name")
    protected String toolName;
    @XmlAttribute(name = "tool-type")
    protected String toolType;
    @XmlAttribute(name = "require-true")
    protected String requireTrue;
    @XmlAttribute
    protected Integer depth;
    @XmlAttribute
    protected Boolean useDefaultLocation;
    @XmlAttribute
    protected Boolean useLatestFileOnly;
    @XmlAttribute(name = "set-success-attribute")
    protected String setSuccessAttribute;

    /**
     * Gets the value of the global property.
     * 
     * @return
     *     possible object is
     *     {@link ToolAppType }
     *     
     */
    public ToolAppType getGlobal() {
        return global;
    }

    /**
     * Sets the value of the global property.
     * 
     * @param value
     *     allowed object is
     *     {@link ToolAppType }
     *     
     */
    public void setGlobal(ToolAppType value) {
        this.global = value;
    }

    /**
     * Gets the value of the analysisCommands property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the analysisCommands property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnalysisCommands().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ToolAppType }
     * 
     * 
     */
    public List<ToolAppType> getAnalysisCommands() {
        if (analysisCommands == null) {
            analysisCommands = new ArrayList<ToolAppType>();
        }
        return this.analysisCommands;
    }

    /**
     * Gets the value of the forAllLike property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForAllLike() {
        return forAllLike;
    }

    /**
     * Sets the value of the forAllLike property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForAllLike(String value) {
        this.forAllLike = value;
    }

    /**
     * Gets the value of the toolState property.
     * 
     * @return
     *     possible object is
     *     {@link ToolStateType }
     *     
     */
    public ToolStateType getToolState() {
        return toolState;
    }

    /**
     * Sets the value of the toolState property.
     * 
     * @param value
     *     allowed object is
     *     {@link ToolStateType }
     *     
     */
    public void setToolState(ToolStateType value) {
        this.toolState = value;
    }

    /**
     * Gets the value of the toolId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToolId() {
        return toolId;
    }

    /**
     * Sets the value of the toolId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToolId(String value) {
        this.toolId = value;
    }

    /**
     * Gets the value of the toolName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToolName() {
        return toolName;
    }

    /**
     * Sets the value of the toolName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToolName(String value) {
        this.toolName = value;
    }

    /**
     * Gets the value of the toolType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToolType() {
        return toolType;
    }

    /**
     * Sets the value of the toolType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToolType(String value) {
        this.toolType = value;
    }

    /**
     * Gets the value of the requireTrue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequireTrue() {
        return requireTrue;
    }

    /**
     * Sets the value of the requireTrue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequireTrue(String value) {
        this.requireTrue = value;
    }

    /**
     * Gets the value of the depth property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getDepth() {
        if (depth == null) {
            return -1;
        } else {
            return depth;
        }
    }

    /**
     * Sets the value of the depth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDepth(Integer value) {
        this.depth = value;
    }

    /**
     * Gets the value of the useDefaultLocation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUseDefaultLocation() {
        if (useDefaultLocation == null) {
            return false;
        } else {
            return useDefaultLocation;
        }
    }

    /**
     * Sets the value of the useDefaultLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUseDefaultLocation(Boolean value) {
        this.useDefaultLocation = value;
    }

    /**
     * Gets the value of the useLatestFileOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUseLatestFileOnly() {
        if (useLatestFileOnly == null) {
            return true;
        } else {
            return useLatestFileOnly;
        }
    }

    /**
     * Sets the value of the useLatestFileOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUseLatestFileOnly(Boolean value) {
        this.useLatestFileOnly = value;
    }

    /**
     * Gets the value of the setSuccessAttribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetSuccessAttribute() {
        return setSuccessAttribute;
    }

    /**
     * Sets the value of the setSuccessAttribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetSuccessAttribute(String value) {
        this.setSuccessAttribute = value;
    }

}
