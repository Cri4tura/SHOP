package dao.jaxb;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import model.ProductList;

public class JaxbMarshaller {

    public void init(ProductList products) {
        try {
            // Crear contexto JAXB para ProductList
            JAXBContext context = JAXBContext.newInstance(ProductList.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            System.out.println("Marshalling...");

            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String filename = "xml/inventory_" + date + ".xml";

            // Serializar el objeto ProductList en un archivo XML
            marshaller.marshal(products, new File(filename));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
