import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class SenderQueue extends Thread {
    ConnectionFactory connection = null;
    JMSContext ctx = null;
    JMSProducer producer = null;
    Queue queue = null;
    String subscriberName = null;

    public SenderQueue(String subscriberName,
                         String connectionFactoryAddress,
                         String queueAddress,
                         String username,
                         String password) throws NamingException, JMSException {
        this.subscriberName = subscriberName;

        InitialContext initialContext = new InitialContext();

        this.connection = InitialContext.doLookup(connectionFactoryAddress);
        this.queue = (Queue) initialContext.lookup(queueAddress);

        this.ctx = this.connection.createContext(username, password);
    }

    @Override
    public void run() {
        try {
            this.startSenderQueue();

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

    public void startSenderQueue() throws JMSException, IOException {
        System.out.println("Starting " + this.subscriberName);

        //QueueReceiver subscriber = this.session.createReceiver(this.queue);
        //subscriber.setMessageListener(this);

        this.producer = this.ctx.createProducer();
        this.sendMessage();
    }

    public void sendMessage() throws JMSException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            TextMessage tm = this.ctx.createTextMessage();
            tm.setText(sc.next());
            this.producer.send(this.queue, tm);
        }
    }

    public void stopSenderQueue() throws JMSException {
        System.out.println("Exiting " + this.subscriberName);
        this.ctx.stop();
        this.ctx.close();
    }

    public static void main(String[] args) {
        SenderQueue rt = null;
        try {
            rt = new SenderQueue("Sender Topic", "jms/RemoteConnectionFactory", "jms/queue/PlayQueue", "topic", "topic");

            final SenderQueue finalRt = rt;
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("Shutting down " + finalRt.subscriberName + "...");
                    try {
                        finalRt.stopSenderQueue();
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
