package dao.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import model.Product;

public class DomWriter {

	private Document document;

	public DomWriter() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newDefaultInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			document = builder.newDocument();
		} catch (ParserConfigurationException e) {
			System.out.println("ERROR generando fichero XML");
		}
	}

	public boolean generateDocument(ArrayList<Product> inventory) {

		boolean generated = false;

		Element products = document.createElement("products");
		products.setAttribute("total", String.valueOf(inventory.size()));
		document.appendChild(products);

		for (Product productInventory : inventory) {
            Element product = document.createElement("product");
            product.setAttribute("id", String.valueOf(productInventory.getId()));
            products.appendChild(product);

            Element name = document.createElement("name");
            name.setTextContent(productInventory.getName());
            product.appendChild(name);

            Element price = document.createElement("price");
            price.setAttribute("currency", "€");
            price.setTextContent("" + productInventory.getWholesalerPrice());
            product.appendChild(price);

            Element stock = document.createElement("stock");
            stock.setTextContent("" + productInventory.getStock());
            product.appendChild(stock);
        }

        generated = generateXml();
        return generated;
    }

	private boolean generateXml() {
		boolean generated = false;
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();

			// Give format to the generated document
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");  
			Source source = new DOMSource(document);

			// Date
			Date date = new Date();
			String formatDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

			File f = new File("files" + File.separator + "inventory_" + formatDate + ".xml");
			FileWriter fw = new FileWriter(f);
			PrintWriter pw = new PrintWriter(fw);
			Result result = new StreamResult(pw);

			transformer.transform(source, result);
			generated = true;
		} catch (IOException e) {
			System.out.println("ERROR creando el documento");
			generated = false;
		} catch (TransformerException e) {
			System.out.println("ERROR dando formato al documento");
			generated = false;
		}
		return generated;
	}

}