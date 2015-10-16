import org.jsoup.nodes.Document;
import org.xml.sax.SAXException;
import java.io.*;
import javax.lang.model.util.Elements;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants;


public class HTMLSummaryCreator {
    public static void main(String[] args)
    {
       
        try
        {
            TransformerFactory tFactory = TransformerFactory.newInstance();

            //Sources
            Source xsdDoc = new StreamSource("src/xmlSchema.xsd");
            Source xslDoc = new StreamSource("src/htmlSummaryCreator.xsl");
            Source xmlDoc = new StreamSource("src/SavedSmartphones.xml");

            //Validacao

            SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Schema schema = sFactory.newSchema(xsdDoc);
            Validator validator = schema.newValidator();

            try {
                validator.validate(xmlDoc);
                System.out.println(xmlDoc.getSystemId() + " is valid");
            } catch (SAXException e) {
                System.out.println(xmlDoc.getSystemId() + " is NOT valid");
                System.out.println("Reason: " + e.getLocalizedMessage());
            }

            //Criação do HTML
            String outputFileName = "src/SavedSmartphonesTable.html";
            OutputStream htmlFile = new FileOutputStream(outputFileName);

            Transformer transformer = tFactory.newTransformer(xslDoc);
            transformer.transform(xmlDoc, new StreamResult(htmlFile));



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
