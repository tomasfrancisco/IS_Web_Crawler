import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PriceKeeper {

    List<Smartphone> smartphones = null;

    public PriceKeeper() {
        this.smartphones = new Smartphonelist().getSmartphone();
    }

    private static Smartphonelist jaxbXMLtoObject(File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Smartphonelist.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            Smartphonelist list = (Smartphonelist) unmarshaller.unmarshal(file);

            return list;
        } catch (JAXBException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void saveSmartphone(File file) {
        List<Smartphone> newSmartphones = this.jaxbXMLtoObject(file).getSmartphone();

        for(Smartphone newPhone : newSmartphones)
            for(Smartphone phone : this.smartphones)
                if (!smartphones.contains(newPhone))
                    smartphones.add(newPhone);

        System.out.println(this.smartphones);
    }

    public Smartphone getSmartphone(String reference) {
        for(Smartphone phone : this.smartphones)
            if(phone.getName().contains(reference) ||
                    phone.getProcessor().contains(reference) ||
                    phone.getResolution().contains(reference) ||
                    phone.getScreenSizeInches().contains(reference) ||
                    phone.getScreenSizePx().contains(reference) ||
                    phone.getScreenTechnology().contains(reference))
                return phone;
        return null;
    }

    public static void main(String[] args) {
        try {
            PriceKeeper pk = new PriceKeeper();

            ReceiverTopic rt = new ReceiverTopic("Receiver Topic Msg PriceKeeper", "jms/RemoteConnectionFactory", "jms/topic/SmartTopic", "topic", "topic");
            rt.startReceiverTopic(pk);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
