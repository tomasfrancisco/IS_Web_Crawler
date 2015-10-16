import javax.jms.JMSException;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class PriceRequester {

    public static void waitFor(ReceiverQueue rq, ReceiverTopic rt) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    rt.stopReceiverTopic();
                    rq.stopReceiverQueue();
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
    }

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
