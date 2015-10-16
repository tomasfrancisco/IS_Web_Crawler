import org.xml.sax.SAXException;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class PriceKeeper {

    List<Smartphone> smartphones = null;

    public PriceKeeper() {
        this.smartphones = new Smartphonelist().getSmartphone();
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

    public void saveSmartphone(File file) {

        //Validate XML File
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
    }

    public Smartphone getSmartphone(String reference) {
        System.out.println("Searching for reference: " + reference + " ...");
        for(Smartphone phone : this.smartphones)
            if(phone.getBrand().contains(reference) ||
                    phone.getProcessor().contains(reference) ||
                    phone.getResolution().contains(reference) ||
                    phone.getScreenSizeInches().contains(reference) ||
                    phone.getScreenSizePx().contains(reference) ||
                    phone.getScreenTechnology().contains(reference))
                return phone;
        System.out.println("Reference " + reference + " not found.");
        return null;
    }

    public static void waitFor(ReceiverQueue rq, ReceiverTopic rt) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    rt.stopReceiverTopic();
                    rq.stopReceiverQueue();
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

    public boolean validateXML(File f){
        //Sources
        Source xsdDoc = new StreamSource("src/xmlSchema.xsd");
        Source xmlDoc = new StreamSource(f);
        //Validacao
        SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema;

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

    public static void main(String[] args) {
        try {
            PriceKeeper pk = new PriceKeeper();

            ReceiverTopic rt = new ReceiverTopic("price_keeper_topic_receiver", "jms/RemoteConnectionFactory", "jms/topic/SmartTopic", "topic", "topic");
            ReceiverQueue rq = new ReceiverQueue("price_keeper_queue_receiver", "jms/RemoteConnectionFactory", "jms/queue/PlayQueue", "topic", "topic");

            rt.startReceiverTopic(pk);
            rq.startReceiverQueue(pk);

            waitFor(rq, rt);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
