import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

public class ReceiverTopic implements MessageListener {
    TopicConnection connection = null;
    TopicSession session = null;
    Topic topic = null;
    String subscriberName = null;

    public ReceiverTopic(String subscriberName) throws NamingException, JMSException {
        this.subscriberName = subscriberName;

        InitialContext ctx = new InitialContext();
        Object tmp = ctx.lookup("jms/RemoteConnectionFactory");

        TopicConnectionFactory tcf = (TopicConnectionFactory) tmp;
        this.connection = tcf.createTopicConnection("topic", "topic");
        this.connection.setClientID(this.subscriberName);
        this.topic = (Topic) ctx.lookup("jms/topic/SmartTopic");

        this.session = this.connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);

        this.connection.start();
    }

    public void start() throws JMSException, IOException {
        System.out.println("Begin ReceiverTopic");

        TopicSubscriber subscriber = session.createDurableSubscriber(this.topic, this.subscriberName);
        subscriber.setMessageListener(this);
        System.in.read();
        this.stop();
    }

    public void stop() throws JMSException {
        this.connection.stop();
        this.session.close();
        this.connection.close();
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
}
