import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

public class SenderQueue implements MessageListener {
    ConnectionFactory connection = null;
    JMSContext ctx = null;
    JMSProducer producer = null;
    JMSConsumer consumer = null;
    Queue queue = null;
    Queue tQueue = null;
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

    @Override
    public void onMessage(Message message) {
        try {
            if(message instanceof TextMessage) {
                TextMessage msg = (TextMessage) message;
                System.out.println("Message: " + msg.getText());
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void startSenderQueue() throws JMSException, IOException {
        System.out.println("Starting " + this.subscriberName + "...");
        this.producer = this.ctx.createProducer();
        this.tQueue = this.ctx.createTemporaryQueue();
        this.consumer = this.ctx.createConsumer(tQueue);
        this.consumer.setMessageListener(this);
    }

    public void sendMessage(String msg) throws JMSException {
        TextMessage tm = this.ctx.createTextMessage();
        tm.setText(msg);
        tm.setJMSReplyTo(this.tQueue);
        this.producer.send(this.queue, tm);
    }

    public void stopSenderQueue() throws JMSException {
        System.out.println("Exiting " + this.subscriberName);
        this.ctx.stop();
        this.ctx.close();
        System.out.println("Terminated.");
    }
}
