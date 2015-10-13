import javax.jms.JMSException;
import javax.naming.NamingException;

public class PriceKeeper {

    public static void main(String[] args) {
        try {
            ReceiverTopic rt = new ReceiverTopic("Receiver Topic Msg PriceKeeper", "jms/RemoteConnectionFactory", "jms/topic/SmartTopic", "topic", "topic");
            rt.start();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}
