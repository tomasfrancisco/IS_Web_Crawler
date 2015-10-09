
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author danielamaral
 */
public class WebCrawler {

    private static final String url = "http://www.pixmania.pt/telefones/telemovel/smartphone-19883-s.html";
    private static final String FILE_NAME = "SavedSmartphones.xml";
    Document doc;
    Elements currentPhones;
    String name;
    String processor;
    String screenTechnology;
    String screenSize;
    String resolution;
    double finalPrice;
    Smartphone phone;
    ArrayList<Smartphone> smartphoneList = new ArrayList<>();
    ArrayList<Double> priceList = new ArrayList<>();

    public WebCrawler() throws IOException {
        doc = Jsoup.connect(url).get();

        currentPhones = doc.select("div.in");

        for (Element tempPhone : currentPhones) {
//            sSystem.out.println(tempPhone);
            Elements header = tempPhone.getElementsByClass("productTitle");

            //Smartphone name
            if (header.size() > 0) {
                Element headerTag = header.get(0);
                name = headerTag.getElementsByTag("a").attr("title");
                name = name.substring(0, name.indexOf("-"));
            } else {
                continue;
            }

            Elements list = tempPhone.getElementsByClass("customList");

            if (list.size() > 0) {

                Elements caracteristics = list
                        .get(0)
                        .getElementsByTag("li");

                //Processador
                processor = caracteristics
                        .get(0)
                        .toString();
                processor = processor.substring(processor.indexOf(": ") + 2, processor.length() - 5); //PERGUNTAR AO PROF O QUE FAZEMOS COM A MEMORIA RAM

                //Screen Technology
                screenTechnology = caracteristics
                        .get(1)
                        .toString();
                screenTechnology = screenTechnology.substring(screenTechnology.indexOf(": ") + 2, screenTechnology.length() - 5);
                //Screen Size
                String tempScreenSize
                        = caracteristics
                        .get(2)
                        .toString();

                screenSize = tempScreenSize.substring(tempScreenSize.indexOf(": ") + 2, tempScreenSize.indexOf(": ") + 6);

                //Resolution
                resolution = caracteristics
                        .get(3)
                        .toString();
                resolution = resolution
                        .substring(resolution.indexOf(": "), resolution.length() - 5);

                phone = new Smartphone(name, processor, screenTechnology, screenSize, resolution, 0);
                smartphoneList.add(phone);
            }

        }

        Elements priceElement = doc.select("span.currentPrice");

        for (Element priceByPhone : priceElement) {

            String finalPriceByPhone = priceByPhone.getElementsByTag("ins").attr("content");
            finalPrice = round(Double.parseDouble(finalPriceByPhone), 2);
            priceList.add(finalPrice);

//             //System.out.println(price);
//            System.out.println("Nome: " + name
//                    + "\nProcessador:" + processor
//                    + "\nTecnologia de ecrã: " + screenTechnology
//                    + "\nTamanho do ecrã: " + screenSize
//                    + "\nResolução Máxima: " + resolution
//                    + "\nPrice: " + finalPrice + "€"
//                    + "\n--------------------");
        }
        int i = 0;
        for (Smartphone sPhone : smartphoneList) {
            sPhone.setPrice(priceList.get(i));
            i++;
        }

    }

    public static void main(String[] args) throws IOException {
        WebCrawler crawler = new WebCrawler();
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    private static void jaxbObjectToXML(Smartphone smartphone) {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(Smartphone.class);
            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(smartphone, System.out);

            marshaller.marshal(smartphone, new File(FILE_NAME));

        } catch (JAXBException ex) {
            ex.printStackTrace();
        }

    }
}
