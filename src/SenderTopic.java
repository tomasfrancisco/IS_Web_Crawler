import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.soap.Text;
import java.io.IOException;

/**
 * Created by tomasfrancisco on 12/10/15.
 */
public class SenderTopic {
    TopicConnection connection = null;
    TopicSession session = null;
    Topic topic = null;
    String publisherName = null;

    public SenderTopic(String publisherName) throws NamingException, JMSException {
        this.publisherName = publisherName;

        InitialContext ctx = new InitialContext();
        Object tmp = ctx.lookup("jms/RemoteConnectionFactory");

        TopicConnectionFactory tcf = (TopicConnectionFactory) tmp;
        this.connection = tcf.createTopicConnection("topic", "topic");
        this.connection.setClientID(this.publisherName);
        this.topic = (Topic) ctx.lookup("jms/topic/SmartTopic");

        this.session = this.connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);

        this.connection.start();
    }

    public void start() throws JMSException, IOException {
        System.out.println("Begin SenderTopic");

        TopicPublisher publisher = session.createPublisher(this.topic);
        TextMessage msg = session.createTextMessage();
        msg.setText("Olá boa tarde!!!");
        publisher.send(msg);
        System.in.read();
        this.stop();
    }

    public void stop() throws JMSException {
        this.connection.stop();
        this.session.close();
        this.connection.close();
    }

    public static void main(String[] args) {
        SenderTopic st = null;
        try {
            st = new SenderTopic("SenderTopic");
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        try {
            st.start();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
