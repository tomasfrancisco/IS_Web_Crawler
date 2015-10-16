import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Scanner;

public class PriceRequester {

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            String reference = sc.next();

            SenderQueue sq = new SenderQueue("Price Requester Queue Sender", "jms/RemoteConnectionFactory", "jms/queue/PlayQueue", "topic", "topic");
            sq.startSenderQueue();
            sq.sendMessage(reference);

            System.in.read();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
