import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReceiverTopic extends Thread implements MessageListener {
    TopicConnection connection = null;
    TopicSession session = null;
    Topic topic = null;
    String subscriberName = null;

    public ReceiverTopic(String subscriberName,
                         String connectionFactoryAddress,
                         String topicAddress,
                         String username,
                         String password) throws NamingException, JMSException {
        this.subscriberName = subscriberName;

        InitialContext ctx = new InitialContext();
        Object tmp = ctx.lookup(connectionFactoryAddress);

        TopicConnectionFactory tcf = (TopicConnectionFactory) tmp;
        this.connection = tcf.createTopicConnection(username, password);
        this.connection.setClientID(this.subscriberName);
        this.topic = (Topic) ctx.lookup(topicAddress);

        this.session = this.connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);

        this.connection.start();
    }

    @Override
    public void run() {
        try {
            this.startReceiverTopic();

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
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        TopicSubscriber subscriber = session.createDurableSubscriber(this.topic, this.subscriberName);
        subscriber.setMessageListener(this);
    }

    public void stopReceiverTopic() throws JMSException {
        System.out.println("Exiting " + this.subscriberName);
        this.connection.stop();
        this.session.close();
        this.connection.close();
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
            rt.start();
            rt.join();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
