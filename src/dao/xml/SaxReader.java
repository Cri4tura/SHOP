package dao.xml;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import model.Amount;
import model.Product;

public class SaxReader extends DefaultHandler {

	private ArrayList<Product> products = new ArrayList<>();
	private Product product;
	private StringBuilder buffer = new StringBuilder();

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		switch (qName) {
		case "product":
			String productName = attributes.getValue("name");
			product = new Product(productName, new Amount(0), true, 0);
			break;
		case "wholesalerPrice":
		    String currency = attributes.getValue("currency");
		    buffer.setLength(0);
		    break;
		case "stock":
			buffer.setLength(0);
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		case "wholesalerPrice":
		    double wholesalerPrice = Double.parseDouble(buffer.toString());
		    Amount amount = new Amount(wholesalerPrice);
		    product = new Product(product.getName(), amount, product.isAvailable(), product.getStock());
		    break;
		case "stock":
			int stock = Integer.parseInt(buffer.toString());
			product.setStock(stock);
			product.setAvailable(stock > 0);
			break;
		case "product":
			products.add(product);
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
	    buffer.append(ch, start, length);
	}
}