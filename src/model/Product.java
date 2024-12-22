package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "product")
@XmlType(propOrder = { "name", "available", "wholesalerPrice", "publicPrice", "stock" })
public class Product {
	private int id;
	private String name;
	private Amount publicPrice = new Amount(0.0);
	private Amount wholesalerPrice = new Amount(0.0);
	private boolean available;
	private int stock;
	private static int totalProducts;

	public final static double EXPIRATION_RATE = 0.60;

	public Product() {
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPublicPrice(Amount publicPrice) {
		this.publicPrice = publicPrice;
	}

	public void setWholesalerPrice(Amount wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;
		this.publicPrice = new Amount(wholesalerPrice.getValue() * 2); // publicPrice dependerá del wholesalerPrice
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public void setStock(int stock) {
		this.stock = stock;
		this.available = stock > 0; // available dependerá de la cantidad de stock del producto
	}

	public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
		super();
		this.name = name;
		this.wholesalerPrice = wholesalerPrice;
		this.available = available;
		this.stock = stock;
		this.publicPrice = wholesalerPrice;
		this.id = totalProducts + 1;
		totalProducts++;
	}
	
	public Product(int id, String name, double wholesalerPrice, boolean available, int stock) {
		super();
		this.id = id;
		this.name = name;
		this.wholesalerPrice = new Amount(wholesalerPrice);
		this.publicPrice = new Amount(wholesalerPrice * 2);
		this.available = available;
		this.stock = stock;
		totalProducts++;
	}
	
	@XmlAttribute(name = "id")
	public int getId() {
		return id;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	@XmlElement(name = "wholesalerPrice")
	public Amount getWholesalerPrice() {
		return wholesalerPrice;
	}

	@XmlElement(name = "publicPrice")
	public Amount getPublicPrice() {
		return publicPrice;
	}

	@XmlElement(name = "available")
	public boolean isAvailable() {
		return available;
	}

	@XmlElement(name = "stock")
	public int getStock() {
		return stock;
	}

	public static int getTotalProducts() {
		return totalProducts;
	}

	public void expire() {
		this.publicPrice = new Amount(this.publicPrice.getValue() * EXPIRATION_RATE);
	}

	@Override
	public String toString() {
		return "Product [ID=" + id + ", name=" + name + ", wholesalerPrice=" + wholesalerPrice + ", publicPrice="
				+ publicPrice + ", available=" + available + ", stock=" + stock + "]";
	}
}
