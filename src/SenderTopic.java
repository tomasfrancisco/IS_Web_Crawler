import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class SenderTopic extends Thread {
    TopicConnection connection = null;
    TopicSession session = null;
    Topic topic = null;
    String publisherName = null;
    TopicPublisher publisher = null;

    public SenderTopic(String publisherName,
                       String connectionFactoryAddress,
                       String topicAddress,
                       String username,
                       String password)
            throws NamingException, JMSException {

        this.publisherName = publisherName;

        InitialContext ctx = new InitialContext();
        Object tmp = ctx.lookup(connectionFactoryAddress);

        TopicConnectionFactory tcf = (TopicConnectionFactory) tmp;
        this.connection = tcf.createTopicConnection(username, password);
        this.connection.setClientID(this.publisherName);
        this.topic = (Topic) ctx.lookup(topicAddress);

        this.session = this.connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);

        this.connection.start();
    }

    @Override
    public void run() {
        this.startSenderTopic();
    }

    private void startSenderTopic() {
        System.out.println("Starting " + this.publisherName);
        try {
            publisher = session.createPublisher(this.topic);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() throws JMSException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            TextMessage tm = this.session.createTextMessage();
            tm.setText(sc.next());
            this.publisher.send(tm);
        }
    }

    private void stopSenderTopic() {
        System.out.println("Exiting " + this.publisherName);
        try {
            this.connection.stop();
            this.session.close();
            this.connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SenderTopic st = null;
        try {
            st = new SenderTopic("SenderTopic", "jms/RemoteConnectionFactory", "jms/topic/SmartTopic", "topic", "topic");

            final SenderTopic finalSt = st;
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("W: interrupt received, killing server…");
                    finalSt.stopSenderTopic();
                }
            });
            st.start();
            st.sendMessage();
            st.join();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
