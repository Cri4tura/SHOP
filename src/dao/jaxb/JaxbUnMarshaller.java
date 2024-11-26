package dao.jaxb;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import model.Product;
import model.ProductList;

public class JaxbUnMarshaller {

	public ProductList init() {
		ProductList productList = null;

		try {
			// Crear el contexto JAXB para la clase ProductList
			JAXBContext context = JAXBContext.newInstance(ProductList.class);

			Unmarshaller unmarshaller = context.createUnmarshaller();

			System.out.println("Unmarshalling...");

			// Deserializar el archivo inputInventory.xml a un objeto ProductList
			productList = (ProductList) unmarshaller.unmarshal(new File("xml/inputInventory.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		System.out.println("Inventario cargado: ");
		for(Product products : productList.getProducts()) {
			
			System.out.println(products);
		}
		return productList;
	}
}
