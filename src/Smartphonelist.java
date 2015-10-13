//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.13 at 11:06:12 PM WEST 
//




import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="smartphone" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="processor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="screenTechnology" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="screenSizeInches" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="screenSizePx" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="resolution" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}byte" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "smartphone"
})
@XmlRootElement(name = "smartphonelist")
public class Smartphonelist {

    protected List<Smartphonelist.Smartphone> smartphone;

    /**
     * Gets the value of the smartphone property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the smartphone property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSmartphone().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Smartphonelist.Smartphone }
     * 
     * 
     */
    public List<Smartphonelist.Smartphone> getSmartphone() {
        if (smartphone == null) {
            smartphone = new ArrayList<Smartphonelist.Smartphone>();
        }
        return this.smartphone;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="processor" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="screenTechnology" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="screenSizeInches" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="screenSizePx" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="resolution" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="price" type="{http://www.w3.org/2001/XMLSchema}double"/>
     *       &lt;/sequence>
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}byte" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "name",
        "processor",
        "screenTechnology",
        "screenSizeInches",
        "screenSizePx",
        "resolution",
        "price"
    })
    public static class Smartphone {

        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected String processor;
        @XmlElement(required = true)
        protected String screenTechnology;
        @XmlElement(required = true)
        protected String screenSizeInches;
        @XmlElement(required = true)
        protected String screenSizePx;
        @XmlElement(required = true)
        protected String resolution;
        protected double price;
        @XmlAttribute(name = "id")
        protected Byte id;

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the processor property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getProcessor() {
            return processor;
        }

        /**
         * Sets the value of the processor property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setProcessor(String value) {
            this.processor = value;
        }

        /**
         * Gets the value of the screenTechnology property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getScreenTechnology() {
            return screenTechnology;
        }

        /**
         * Sets the value of the screenTechnology property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setScreenTechnology(String value) {
            this.screenTechnology = value;
        }

        /**
         * Gets the value of the screenSizeInches property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getScreenSizeInches() {
            return screenSizeInches;
        }

        /**
         * Sets the value of the screenSizeInches property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setScreenSizeInches(String value) {
            this.screenSizeInches = value;
        }

        /**
         * Gets the value of the screenSizePx property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getScreenSizePx() {
            return screenSizePx;
        }

        /**
         * Sets the value of the screenSizePx property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setScreenSizePx(String value) {
            this.screenSizePx = value;
        }

        /**
         * Gets the value of the resolution property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResolution() {
            return resolution;
        }

        /**
         * Sets the value of the resolution property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResolution(String value) {
            this.resolution = value;
        }

        /**
         * Gets the value of the price property.
         * 
         */
        public double getPrice() {
            return price;
        }

        /**
         * Sets the value of the price property.
         * 
         */
        public void setPrice(double value) {
            this.price = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link Byte }
         *     
         */
        public Byte getId() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link Byte }
         *     
         */
        public void setId(Byte value) {
            this.id = value;
        }

        public String toString(){
            return "Nome: " + name
                    + "\nProcessador:" + processor
                    + "\nTecnologia de ecrã: " + screenTechnology
                    + "\nTamanho do ecrã: " + screenSizeInches + " " + screenSizePx
                    + "\nResolução Máxima: " + resolution
                    + "\nPrice: " + price + "€"
                    + "\n--------------------";
        }

    }

}
