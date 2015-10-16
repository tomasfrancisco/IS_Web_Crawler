import org.xml.sax.SAXException;

import javax.jms.JMSException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.List;


public class HTMLSummaryCreator {
    TransformerFactory tFactory = null;
    Source xslStyleSource = null;
    Smartphonelist smartphoneList = null;
    List<Smartphone> smartphones = null;

    public HTMLSummaryCreator(String xslStyleAddress) {
        this.tFactory = TransformerFactory.newInstance();
        this.xslStyleSource = new StreamSource(xslStyleAddress);
        this.smartphoneList = new Smartphonelist();
        this.smartphones = smartphoneList.getSmartphone();
    }

    private static Smartphonelist jaxbXMLtoObject(File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Smartphonelist.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Smartphonelist list = (Smartphonelist) unmarshaller.unmarshal(file);

            return list;
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static void jaxbObjectToXML(Smartphonelist smphonelist, String fileName) {
        try {
            XMLObject xmlObject;
            JAXBContext jaxbContext = JAXBContext.newInstance(Smartphonelist.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StreamResult file = new StreamResult(fileName);
            marshaller.marshal(smphonelist, file);

        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
    }

    public boolean validateXML(File file) {
        Source xsdDoc = new StreamSource("src/xmlSchema.xsd");
        Source xmlDoc = new StreamSource(file);

        SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema = null;
        try {
            schema = sFactory.newSchema(xsdDoc);
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        }
        Validator validator = schema.newValidator();

        try {
            validator.validate(xmlDoc);
            System.out.println(xmlDoc.getSystemId() + " is valid");
            return true;
        } catch (SAXException e) {
            System.out.println(xmlDoc.getSystemId() + " is NOT valid");
            System.out.println("Reason: " + e.getLocalizedMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void transformIntoHTML() {
        try {
            System.out.println("Transforming...");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(this.xslStyleSource);
            jaxbObjectToXML(this.smartphoneList, "html_summary.xml");
            Source xml = new StreamSource("html_summary.xml");
            transformer.transform(xml, new StreamResult(new FileOutputStream("html_summary.html")));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveSmartphone(File file) {

        if(!validateXML(file)) return;

        List<Smartphone> newSmartphones = this.jaxbXMLtoObject(file).getSmartphone();

        for(Smartphone newPhone : newSmartphones)
            if(!this.smartphones.isEmpty()) {
                for (Smartphone phone : this.smartphones)
                    if (!smartphones.contains(newPhone)) {
                        smartphones.add(newPhone);
                        System.out.println("Saved new smartphone.");
                        break;
                    } else {
                        System.out.println("Smartphone already exists.");
                        break;
                    }

            } else {
                smartphones.add(newPhone);
                System.out.println("Saved new smartphone.");
            }
        transformIntoHTML();
    }

    public static void waitFor(ReceiverTopic rt) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    rt.stopReceiverTopic();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println("To end program, type Q or q, " + "then <return>");
        char answer = 0;
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        while (!((answer == 'q') || (answer == 'Q'))) {
            try {
                answer = (char) inputStreamReader.read();
            } catch (IOException e) {
                System.out.println("I/O exception: " + e.toString());
            }
        }
    }

    public static void main(String[] args)
    {
        try
        {
            ReceiverTopic rt = new ReceiverTopic("html_summary_creator_topic_receiver", "jms/RemoteConnectionFactory", "jms/topic/SmartTopic", "topic", "topic");

            HTMLSummaryCreator htmlSummaryCreator = new HTMLSummaryCreator("src/htmlSummaryCreator.xsl");

            rt.startReceiverTopic(htmlSummaryCreator);

            waitFor(rt);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
