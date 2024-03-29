import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author danielamaral
 */

public class WebCrawler {

    private static String url;
    private static final String FILE_NAME = "src/SavedSmartphones.xml";

    //User input


    //The doc that's gonna have all the info
    Document doc;
    Elements currentPhones;

    //Phone's attributes
    String brand;
    String model;
    String processor;
    String screenTechnology;
    String screenSizeInches;
    String screenSizePx;
    String resolution;
    double finalPrice;

    //Create a factory
    ObjectFactory factory = new ObjectFactory();
    //Create a smartphone
    Smartphone phone;
    //Create a Smartphonelist
    Smartphonelist listSmartphones;
    //Create a price list
    ArrayList<Double> priceList = new ArrayList<>();
    List<Smartphone> smartphones;

    public WebCrawler() throws IOException {

        listSmartphones = factory.createSmartphonelist();
        smartphones = listSmartphones.getSmartphone();

        int i=0;
        try {
            doc = Jsoup.connect(url).get();




            currentPhones = doc.select("div.in");


            for (Element tempPhone : currentPhones) {
//            sSystem.out.println(tempPhone);
                Elements header = tempPhone.getElementsByClass("productTitle");

                phone = factory.createSmartphone();

                //Smartphone name
                if (header.size() > 0) {
                    Element headerTag = header.get(0);
                    String tempTitle = headerTag.getElementsByTag("a").attr("title");
                    if (tempTitle.indexOf("-") == -1)
                        continue;
                    tempTitle = tempTitle.substring(0, tempTitle.indexOf("-"));
                    brand = tempTitle.substring(0, tempTitle.indexOf(" "));
                    model = tempTitle.substring(tempTitle.indexOf(" ") + 1, tempTitle.length());
               /* System.out.println(brand + " " + model);*/

                } else {
                    continue;
                }

                Elements list = tempPhone.getElementsByClass("customList");

                if (list.size() > 0) {

                    Elements caracteristics = list
                            .get(0)
                            .getElementsByTag("li");

                    //Processador
                    if (caracteristics.size() > 0) {
                        processor = caracteristics
                                .get(0)
                                .toString();

                        processor = processor.substring(processor.indexOf(": ") + 2, processor.length() - 5);

                        //Screen Technology
                        screenTechnology = caracteristics
                                .get(1)
                                .toString();
                        screenTechnology = screenTechnology.substring(screenTechnology.indexOf(": ") + 2, screenTechnology.length() - 5);
                    }

                    if (caracteristics.size() > 1) {
                        //Screen Size      #Problem: some phones don't have all the characteristics specified. Gotta check the size.
                        String tempScreenSize
                                = caracteristics
                                .get(2)
                                .toString();

                        String screenSize = tempScreenSize.substring(tempScreenSize.indexOf(": ") + 1, tempScreenSize.length() - 5).trim();
                        //System.out.println(screenSize);
                        if (screenSize.indexOf(" ") == -1)
                            continue;
                        screenSizeInches = screenSize.substring(0, screenSize.indexOf(" "));
                        //System.out.println(screenSizeInches);
                        screenSizePx = screenSize.substring(screenSize.indexOf(" "), screenSize.length());
                        screenSizePx = screenSizePx.replaceAll("[()-]", "");
                        //System.out.println(screenSizePx);
                    }

                    if (caracteristics.size() > 2) {
                        //Resolution
                        resolution = caracteristics
                                .get(3)
                                .toString();
                        resolution = resolution
                                .substring(resolution.indexOf(":") + 2, resolution.length() - 5);
                    }
                }

                phone.setBrand(brand);
                phone.setModel(model);
                phone.setScreenSizeInches(screenSizeInches);
                phone.setScreenSizePx(screenSizePx);
                phone.setProcessor(processor);
                phone.setResolution(resolution);
                phone.setScreenTechnology(screenTechnology);

                smartphones.add(phone);
            }

            Elements priceElement = doc.select("span.currentPrice");

            for (Element priceByPhone : priceElement) {
                String finalPriceByPhone = priceByPhone.getElementsByTag("ins").attr("content");
                finalPrice = round(Double.parseDouble(finalPriceByPhone), 2);
                priceList.add(finalPrice);
            }

            i = 0;
            for (Smartphone sPhone : smartphones) {
                sPhone.setPrice(priceList.get(i));
                i++;
            }

            //System.out.println(smartphones );
            File newFile = jaxbObjectToXML(listSmartphones);

            int count = 0;
            int maxTries = 3;
            while (count < maxTries) {
                try {
                    SenderTopic st = new SenderTopic("price_keeper_topic_sender", "jms/RemoteConnectionFactory", "jms/topic/SmartTopic", "topic", "topic");
                    st.startSenderTopic();
                    st.sendSmartphoneList(newFile);
                    st.stopSenderTopic();
                    break;
                } catch (NamingException e) {
                    e.printStackTrace();
                    System.out.println("Retrying");
                    count++;
                } catch (JMSException e) {
                    e.printStackTrace();
                    System.out.println("Retrying");
                    count++;
                }
            }
        }catch (IllegalArgumentException iaEx){
            System.out.println("Exception: "+iaEx);
        }catch (NullPointerException npEx){
            System.out.println("Exception: "+npEx);
        }catch (CharConversionException e) {
            System.out.println(e);
        }




    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Enter a valid URL to parse: ");
            url = sc.nextLine();
            System.out.println("\nParsing...");
            WebCrawler crawler = new WebCrawler();

        }

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




    private static File jaxbObjectToXML(Smartphonelist listSmartphones  ) {
        try {
            OutputStream os = new FileOutputStream(FILE_NAME);
            JAXBContext jaxbContext = JAXBContext.newInstance(Smartphonelist.class);

            Marshaller marshaller = jaxbContext.createMarshaller();

            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // marshaller.marshal(listSmartphones, System.out );

            marshaller.marshal(listSmartphones, os);

        } catch (JAXBException ex) {
            ex.printStackTrace();
        }catch (FileNotFoundException fnfEx){
            fnfEx.printStackTrace();
        }
        return new File(FILE_NAME);
    }
}
