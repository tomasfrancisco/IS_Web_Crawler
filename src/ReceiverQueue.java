import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

public class ReceiverQueue implements MessageListener {
    private ConnectionFactory connection = null;
    private JMSContext ctx = null;
    private JMSProducer producer = null;
    private JMSConsumer consumer = null;
    private Queue queue = null;
    protected String subscriberName = null;
    protected PriceKeeper priceKeeper = null;
    protected HTMLSummaryCreator htmlSummaryCreator = null;

    public ReceiverQueue(String subscriberName,
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
        TextMessage msg = (TextMessage) message;
        try {
            TextMessage response = this.ctx.createTextMessage();
            Smartphone phone = this.priceKeeper.getSmartphone(msg.getText());
            if(phone != null)
                response.setText(Double.toString(phone.getPrice()));
            else
                response.setText("Reference not found.");
            producer.send(msg.getJMSReplyTo(), response);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void startReceiverQueue(PriceKeeper priceKeeper) throws JMSException, IOException {
        System.out.println("Starting " + this.subscriberName + "...");
        this.priceKeeper = priceKeeper;
        this.consumer = this.ctx.createConsumer(this.queue);
        this.consumer.setMessageListener(this);
        this.producer = this.ctx.createProducer();
    }

    public void startReceiverQueue(HTMLSummaryCreator htmlSummaryCreator) throws JMSException, IOException {
        System.out.println("Starting " + this.subscriberName + "...");
        htmlSummaryCreator = htmlSummaryCreator;
        this.consumer = this.ctx.createConsumer(this.queue);
        this.consumer.setMessageListener(this);
        this.producer = this.ctx.createProducer();
    }

    public void stopReceiverQueue() throws JMSException {
        System.out.println("Exiting from " + this.subscriberName + "...");
        this.ctx.stop();
        this.ctx.close();
        System.out.println("Terminated.");
    }
}
