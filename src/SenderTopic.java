import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.crypto.dsig.XMLObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class SenderTopic {
    ConnectionFactory connection = null;
    JMSContext ctx = null;
    JMSProducer producer = null;
    Topic topic = null;
    String publisherName = null;

    public SenderTopic(String publisherName,
                       String connectionFactoryAddress,
                       String topicAddress,
                       String username,
                       String password)
            throws NamingException, JMSException {
        this.publisherName = publisherName;

        InitialContext initialContext = new InitialContext();

        this.connection = (ConnectionFactory) initialContext.lookup(connectionFactoryAddress);
        this.topic = (Topic) initialContext.lookup(topicAddress);

        this.ctx = this.connection.createContext(username, password);
    }

    private void startSenderTopic() {
        System.out.println("Starting " + this.publisherName + "...");
        this.producer = this.ctx.createProducer();
    }

    public void sendMessage(String msg) throws JMSException {
        TextMessage tm = this.ctx.createTextMessage();
        tm.setText(msg);
        this.producer.send(this.topic, tm);
    }

    public void sendSmartphoneList(File smartphoneList) {
        FileInputStream fileInputStream = null;

        byte[] byteFile = new byte[(int) smartphoneList.length()];

        try {
            fileInputStream = new FileInputStream(smartphoneList);
            fileInputStream.read(byteFile);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.producer.send(this.topic, byteFile);
    }

    private void stopSenderTopic() {
        System.out.println("Exiting " + this.publisherName);
        this.ctx.stop();
        this.ctx.close();
    }

    private static File jaxbObjectToXML(Smartphonelist smphonelist) {
        try {
            XMLObject xmlObject;
            JAXBContext jaxbContext = JAXBContext.newInstance(Smartphonelist.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

//            marshaller.marshal(smphonelist, xmlObject );

            File file = new File("saved.xml");
            marshaller.marshal(smphonelist, file);

            return file;

        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        SenderTopic st = null;
        try {
            st = new SenderTopic("SenderTopic", "jms/RemoteConnectionFactory", "jms/topic/SmartTopic", "topic", "topic");

            st.startSenderTopic();

            ObjectFactory factory = new ObjectFactory();
            Smartphonelist list = factory.createSmartphonelist();
            List<Smartphone> smartphonelist = list.getSmartphone();
            Smartphone phone = new Smartphone();
            phone.setBrand("OLAAA CRL!!!");
            smartphonelist.add(phone);

            File file = st.jaxbObjectToXML(list);

            st.sendSmartphoneList(file);

            final SenderTopic finalSt = st;
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("W: interrupt received, killing server…");
                    finalSt.stopSenderTopic();
                }
            });
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
