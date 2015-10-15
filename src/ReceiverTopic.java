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
            File file = new File("smartphonelist.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data);

            this.priceKeeper.saveSmartphone(file);

            System.out.println("Got new file.");
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void startReceiverTopic(PriceKeeper priceKeeper) throws JMSException, IOException {
        System.out.println("Starting " + this.subscriberName);

        this.priceKeeper = priceKeeper;

        this.consumer = this.ctx.createConsumer(this.topic);
        this.consumer.setMessageListener(this);
        System.in.read();
    }

    public void stopReceiverTopic() throws JMSException {
        System.out.println("Exiting " + this.subscriberName);
        this.ctx.stop();
        this.ctx.close();
    }

    public static void main(String[] args) {
        ReceiverTopic rt = null;
        try {
            rt = new ReceiverTopic("Receiver Topic", "jms/RemoteConnectionFactory", "jms/topic/SmartTopic", "topic", "topic");


            final ReceiverTopic finalRt = rt;
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("Shutting down " + finalRt.subscriberName + "...");
                    try {
                        finalRt.stopReceiverTopic();
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
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
