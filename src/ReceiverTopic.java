import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReceiverTopic implements MessageListener {
    ConnectionFactory connection = null;
    JMSContext ctx = null;
    JMSConsumer consumer = null;
    Topic topic = null;
    String subscriberName = null;

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
        TextMessage msg = (TextMessage) message;
        try {
            System.out.println("Got message: " + msg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void startReceiverTopic() throws JMSException, IOException {
        System.out.println("Starting " + this.subscriberName);

        this.consumer = this.ctx.createConsumer(this.topic);
        this.consumer.setMessageListener(this);
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
