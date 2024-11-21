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

		ProductList productList = unMarshaller.init();

		if (productList == null) {
			return null;
		}

		// ProductList a un ArrayList<Product>
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

			return true; 

		} catch (Exception e) {
			e.printStackTrace(); 
			return false; 
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
