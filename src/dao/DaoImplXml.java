package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import dao.xml.DomWriter;
import model.Employee;
import model.Product;
import dao.xml.SaxReader;

public class DaoImplXml implements Dao {

	public ArrayList<Product> inventory;

	@Override
	public void connect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Product> getInventory() {
		inventory = new ArrayList<Product>();

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;

		try {
			parser = factory.newSAXParser();
			File f = new File(System.getProperty("user.dir") + File.separator + "files/inputInventory.xml");
			SaxReader saxReader = new SaxReader();
			parser.parse(f, saxReader);
			inventory = saxReader.getProducts();
			return inventory;

		} catch (ParserConfigurationException | SAXException e) {
			System.out.println("ERROR parseando el inventario");
			return null;
		} catch (IOException e) {
			System.out.println("ERROR fichero no encontrado");
			return null;
		}
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		boolean generated = false;

		DomWriter domWriter = new DomWriter();
		generated = domWriter.generateDocument(inventory);
		return generated;
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProduct(Product product) {
		// TODO Auto-generated method stub
		
	}

}