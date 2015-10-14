import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.*;
import java.util.Scanner;

public class SenderQueue {
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

        this.connection = (ConnectionFactory) initialContext.lookup(connectionFactoryAddress);
        this.queue = (Queue) initialContext.lookup(queueAddress);

        this.ctx = this.connection.createContext(username, password);
    }

    public void startSenderQueue() throws JMSException, IOException {
        System.out.println("Starting " + this.subscriberName + "...");
        this.producer = this.ctx.createProducer();
    }

    public void sendMessage(String msg) throws JMSException {
        TextMessage tm = this.ctx.createTextMessage();
        tm.setText(msg);
        this.producer.send(this.queue, tm);
    }

    public void stopSenderQueue() throws JMSException {
        System.out.println("Exiting " + this.subscriberName);
        this.ctx.stop();
        this.ctx.close();
    }

    public static void main(String[] args) {
        SenderQueue rt = null;
        try {
            rt = new SenderQueue("SenderQueue", "jms/RemoteConnectionFactory", "jms/queue/PlayQueue", "topic", "topic");
            rt.startSenderQueue();

            rt.sendMessage("teste");
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
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
