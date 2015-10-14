import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReceiverQueue implements MessageListener {
    ConnectionFactory connection = null;
    JMSContext ctx = null;
    JMSConsumer consumer = null;
    Queue queue = null;
    String subscriberName = null;

    public ReceiverQueue(String subscriberName,
                         String connectionFactoryAddress,
                         String queueAddress,
                         String username,
                         String password) throws NamingException, JMSException {
        this.subscriberName = subscriberName;

        InitialContext initialContext = new InitialContext();

        this.connection = (ConnectionFactory) initialContext.lookup(connectionFactoryAddress);
        this.queue = (Queue) initialContext.lookup(queueAddress);

        this.ctx = this.connection.createContext(username, password);
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

    public void startReceiverQueue() throws JMSException, IOException {
        System.out.println("Starting " + this.subscriberName + "...");

        this.consumer = this.ctx.createConsumer(this.queue);
        this.consumer.setMessageListener(this);
    }

    public void stopReceiverQueue() throws JMSException {
        System.out.println("Exiting " + this.subscriberName + "...");
        this.ctx.stop();
        this.ctx.close();
    }

    public static void main(String[] args) {
        ReceiverQueue rt = null;
        try {
            rt = new ReceiverQueue("Receiver Queue", "jms/RemoteConnectionFactory", "jms/queue/PlayQueue", "topic", "topic");
            rt.startReceiverQueue();

            final ReceiverQueue finalRt = rt;
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("Shutting down " + finalRt.subscriberName + "...");
                    try {
                        finalRt.stopReceiverQueue();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
