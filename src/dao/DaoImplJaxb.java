package dao;

import java.util.ArrayList;

import dao.jaxb.JaxbMarshaller;
import dao.jaxb.JaxbUnMarshaller;
import model.Employee;
import model.Product;
import model.ProductList;

public class DaoImplJaxb implements Dao {

	@Override
	public void connect() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Product> getInventory() {
		JaxbUnMarshaller unMarshaller = new JaxbUnMarshaller();

		// Obtenemos el ProductList desde JaxbUnMarshaller
		ProductList productList = unMarshaller.init();

		// Si productList es null, significa que hubo un problema al deserializar
		if (productList == null) {
			return null;
		}

		// Convertimos el ProductList a un ArrayList<Product> y lo retornamos
		return new ArrayList<>(productList.getProducts());
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		try {
			// Convertir el ArrayList<Product> en un ProductList
			ProductList productList = new ProductList(products);

			// Crear una instancia de JaxbMarshaller para serializar el ProductList
			JaxbMarshaller jaxbMarshaller = new JaxbMarshaller();

			// Serializar el ProductList en un archivo XML
			jaxbMarshaller.init(productList); // Llamamos a init() de JaxbMarshaller para serializar el objeto

			return true; // Si la serialización fue exitosa, devolvemos true

		} catch (Exception e) {
			e.printStackTrace(); // Si ocurre algún error, lo imprimimos
			return false; // Devolvemos false si hay un error
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
