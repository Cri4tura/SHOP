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
    public void startDocument() throws SAXException {
        this.products = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case "product":
                String productName = attributes.getValue("name");
                int productId = Integer.parseInt(attributes.getValue("id")); 
                product = new Product(productName, new Amount(0), true, 0); 
                product.setId(productId);
                break;
            case "wholesalerPrice":
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
                double wholesalerPrice = Double.parseDouble(buffer.toString().trim()); 
                Amount amount = new Amount(wholesalerPrice); 
//                product.setWholesalerPrice(amount.getValue()); 
//               product.setPublicPrice(amount.getValue() * 2); 
                break;
            case "stock":
                int stock = Integer.parseInt(buffer.toString().trim()); 
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
