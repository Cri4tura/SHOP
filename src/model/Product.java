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
	private Amount publicPrice;
	private Amount wholesalerPrice;
	private boolean available;
	private int stock;
	private static int totalProducts;

	public final static double EXPIRATION_RATE = 0.60;

	// Constructor vacío para JAXB
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
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Product(String name, Amount amount, boolean available, int stock) {
		super();
		this.id = totalProducts + 1;
		this.name = name;
		this.wholesalerPrice = amount;
		this.publicPrice = new Amount(amount.getValue() * 2);
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
		return "Product [ID=" + id + ", name=" + name + ", publicPrice=" + publicPrice + ", wholesalerPrice="
				+ wholesalerPrice + ", available=" + available + ", stock=" + stock + "]";
	}
}
