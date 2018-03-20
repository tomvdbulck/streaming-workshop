//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.03.20 at 10:31:36 PM CET 
//


package generated.config;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for T_meetpunt complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="T_meetpunt">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}beschrijvende_id" minOccurs="0"/>
 *         &lt;element ref="{}volledige_naam" minOccurs="0"/>
 *         &lt;element ref="{}Ident_8" minOccurs="0"/>
 *         &lt;element ref="{}lve_nr" minOccurs="0"/>
 *         &lt;element ref="{}Kmp_Rsys" minOccurs="0"/>
 *         &lt;element ref="{}Rijstrook" minOccurs="0"/>
 *         &lt;element ref="{}X_coord_EPSG_31370" minOccurs="0"/>
 *         &lt;element ref="{}Y_coord_EPSG_31370" minOccurs="0"/>
 *         &lt;element ref="{}lengtegraad_EPSG_4326" minOccurs="0"/>
 *         &lt;element ref="{}breedtegraad_EPSG_4326" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="unieke_id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "T_meetpunt", propOrder = {
    "beschrijvendeId",
    "volledigeNaam",
    "ident8",
    "lveNr",
    "kmpRsys",
    "rijstrook",
    "xCoordEPSG31370",
    "yCoordEPSG31370",
    "lengtegraadEPSG4326",
    "breedtegraadEPSG4326"
})
public class TMeetpunt {

    @XmlElement(name = "beschrijvende_id")
    protected String beschrijvendeId;
    @XmlElement(name = "volledige_naam")
    protected String volledigeNaam;
    @XmlElement(name = "Ident_8")
    protected String ident8;
    @XmlElement(name = "lve_nr")
    protected BigInteger lveNr;
    @XmlElement(name = "Kmp_Rsys")
    protected String kmpRsys;
    @XmlElement(name = "Rijstrook")
    protected String rijstrook;
    @XmlElement(name = "X_coord_EPSG_31370")
    protected String xCoordEPSG31370;
    @XmlElement(name = "Y_coord_EPSG_31370")
    protected String yCoordEPSG31370;
    @XmlElement(name = "lengtegraad_EPSG_4326")
    protected String lengtegraadEPSG4326;
    @XmlElement(name = "breedtegraad_EPSG_4326")
    protected String breedtegraadEPSG4326;
    @XmlAttribute(name = "unieke_id", required = true)
    protected String uniekeId;

    /**
     * Gets the value of the beschrijvendeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBeschrijvendeId() {
        return beschrijvendeId;
    }

    /**
     * Sets the value of the beschrijvendeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBeschrijvendeId(String value) {
        this.beschrijvendeId = value;
    }

    /**
     * Gets the value of the volledigeNaam property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVolledigeNaam() {
        return volledigeNaam;
    }

    /**
     * Sets the value of the volledigeNaam property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVolledigeNaam(String value) {
        this.volledigeNaam = value;
    }

    /**
     * Gets the value of the ident8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdent8() {
        return ident8;
    }

    /**
     * Sets the value of the ident8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdent8(String value) {
        this.ident8 = value;
    }

    /**
     * Gets the value of the lveNr property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLveNr() {
        return lveNr;
    }

    /**
     * Sets the value of the lveNr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLveNr(BigInteger value) {
        this.lveNr = value;
    }

    /**
     * Gets the value of the kmpRsys property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKmpRsys() {
        return kmpRsys;
    }

    /**
     * Sets the value of the kmpRsys property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKmpRsys(String value) {
        this.kmpRsys = value;
    }

    /**
     * Gets the value of the rijstrook property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRijstrook() {
        return rijstrook;
    }

    /**
     * Sets the value of the rijstrook property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRijstrook(String value) {
        this.rijstrook = value;
    }

    /**
     * Gets the value of the xCoordEPSG31370 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXCoordEPSG31370() {
        return xCoordEPSG31370;
    }

    /**
     * Sets the value of the xCoordEPSG31370 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXCoordEPSG31370(String value) {
        this.xCoordEPSG31370 = value;
    }

    /**
     * Gets the value of the yCoordEPSG31370 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYCoordEPSG31370() {
        return yCoordEPSG31370;
    }

    /**
     * Sets the value of the yCoordEPSG31370 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYCoordEPSG31370(String value) {
        this.yCoordEPSG31370 = value;
    }

    /**
     * Gets the value of the lengtegraadEPSG4326 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLengtegraadEPSG4326() {
        return lengtegraadEPSG4326;
    }

    /**
     * Sets the value of the lengtegraadEPSG4326 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLengtegraadEPSG4326(String value) {
        this.lengtegraadEPSG4326 = value;
    }

    /**
     * Gets the value of the breedtegraadEPSG4326 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBreedtegraadEPSG4326() {
        return breedtegraadEPSG4326;
    }

    /**
     * Sets the value of the breedtegraadEPSG4326 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBreedtegraadEPSG4326(String value) {
        this.breedtegraadEPSG4326 = value;
    }

    /**
     * Gets the value of the uniekeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniekeId() {
        return uniekeId;
    }

    /**
     * Sets the value of the uniekeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniekeId(String value) {
        this.uniekeId = value;
    }

}
