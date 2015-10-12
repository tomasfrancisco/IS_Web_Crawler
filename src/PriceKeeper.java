import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.soap.Text;
import java.io.IOException;

public class PriceKeeper implements MessageListener {
    private JMSContext ctx;

    public PriceKeeper() {
        ctx = getInitialContext("jms/RemoteConnectionFactory", "joao", "br1o+sa*");
    }

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            System.out.println(msg.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void onClose() {

    }

    public void start() {

    }

    private static JMSContext getInitialContext(String url, String username, String password) {
        try {
            ConnectionFactory connection = InitialContext.doLookup(url);
            return connection.createContext(username, password);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        ReceiverTopic topic = null;
        try {
            topic = new ReceiverTopic("PriceKeeper");
            topic.start();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
