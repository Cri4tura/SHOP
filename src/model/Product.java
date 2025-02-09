package model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table(name = "inventory")
@XmlRootElement(name = "product")
@XmlType(propOrder = { "name", "available", "wholesalerPrice", "publicPrice", "stock" })
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "price")
	private double price;
	
	@Transient
	private Amount publicPrice = new Amount(0.0);
	
	@Transient
	private Amount wholesalerPrice = new Amount(0.0);
	
	@Column(name = "available")
	private boolean available;
	
	@Column(name = "stock")
	private int stock;
	
	@Transient
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

	public void setPrice() {
		this.price = price;
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
	
	public Product(String name, double price, boolean available, int stock) {
		super();
		this.name = name;
		this.price = price;
		this.available = available;
		this.stock = stock;
	}
	
	public int getId() {
		return id;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
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
		return "\nProduct [id=" + id + ", name=" + name + ", price=" + price + ", available=" + available + ", stock="
				+ stock + "]";
	}


//	@Override
//	public String toString() {
//		return "Product [ID=" + id + ", name=" + name + ", wholesalerPrice=" + wholesalerPrice + ", publicPrice="
//				+ publicPrice + ", available=" + available + ", stock=" + stock + "]";
//	}
	
	
}
