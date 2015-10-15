import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;

public class HTMLSummaryCreator {
    public static void main(String[] args)
    {
        try
        {
            TransformerFactory tFactory = TransformerFactory.newInstance();

            Source xslDoc = new StreamSource("src/htmlSummaryCreator.xsl");
            Source xmlDoc = new StreamSource("src/SavedSmartphones.xml");

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
