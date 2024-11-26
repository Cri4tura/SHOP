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

			// Deserializar el archivo inputInventory.xml a un objeto ProductList
			productList = (ProductList) unmarshaller.unmarshal(new File("jaxb/inputInventory.xml"));

			System.out.println(" ");
			System.out.println("Unmarshalling...");
			System.out.println(" ");
			System.out.println("Cargando inventario... ");

			for (Product products : productList.getProducts()) {
				System.out.println(products);
			}

		} catch (JAXBException e) {
			System.out.println(" ");
		    System.err.println("Error unmarshalling...");
			e.printStackTrace();
		}

		return productList;
	}
}
