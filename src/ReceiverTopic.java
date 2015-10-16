import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class ReceiverTopic implements MessageListener {
    ConnectionFactory connection = null;
    JMSContext ctx = null;
    JMSConsumer consumer = null;
    Topic topic = null;
    String subscriberName = null;
    PriceKeeper priceKeeper = null;

    public ReceiverTopic(String subscriberName,
                         String connectionFactoryAddress,
                         String topicAddress,
                         String username,
                         String password) throws NamingException, JMSException {
        this.subscriberName = subscriberName;

        InitialContext initialContext = new InitialContext();

        this.connection = (ConnectionFactory) initialContext.lookup(connectionFactoryAddress);
        this.topic = (Topic) initialContext.lookup(topicAddress);

        this.ctx = connection.createContext(username, password);
    }

    @Override
    public void onMessage(Message message) {
        BytesMessage byteFile = (BytesMessage) message;
        try {
            byte[] data = new byte[(int) byteFile.getBodyLength()];
            byteFile.readBytes(data, (int) byteFile.getBodyLength());
            File file = new File("received_" + this.subscriberName + ".xml");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);

            this.priceKeeper.saveSmartphone(file);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startReceiverTopic(PriceKeeper priceKeeper) throws JMSException, IOException {
        System.out.println("Starting " + this.subscriberName + "...");

        this.priceKeeper = priceKeeper;

        this.ctx.setClientID(this.subscriberName);
        this.consumer = this.ctx.createDurableConsumer(this.topic, this.subscriberName);

        this.consumer.setMessageListener(this);
    }

    public void stopReceiverTopic() throws JMSException {
        System.out.println("Exiting " + this.subscriberName + "...");
        this.ctx.stop();
        this.ctx.close();
        System.out.println("Terminated.");
    }
}
