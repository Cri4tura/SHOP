package dao.jaxb;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import model.ProductList;

public class JaxbUnMarshaller {

    // Método que devuelve un ProductList deserializado desde el archivo XML
    public ProductList init() {
        ProductList productList = null;
        
        try {
            // Crear el contexto JAXB para la clase ProductList
            JAXBContext context = JAXBContext.newInstance(ProductList.class);
            
            // Crear el deserializador (unmarshaller)
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            // Deserializar el archivo inputInventory.xml a un objeto ProductList
            productList = (ProductList) unmarshaller.unmarshal(new File("files/inputInventory.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        
        // Retornar el ProductList deserializado
        return productList;
    }
}
