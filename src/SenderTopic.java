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

    public void startSenderTopic() {
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

    public void stopSenderTopic() {
        System.out.println("Exiting " + this.publisherName + "...");
        this.ctx.stop();
        this.ctx.close();
        System.out.println("Terminated.");
    }
}
