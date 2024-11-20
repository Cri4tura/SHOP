package model;

public class Product {
    private int id; 
    private String name;
    private double publicPrice; 
    private double wholesalerPrice;
    private boolean available; 
    private int stock; 
    private static int totalProducts;

    public final static double EXPIRATION_RATE = 0.60;

    // Constructor
    public Product(String name, Amount amount, boolean available, int stock) {
        super();
        this.id = totalProducts+1;
        this.name = name;
        this.publicPrice = amount.getValue() * 2;
        this.wholesalerPrice = amount.getValue();
        this.available = available;
        this.stock = stock;
        totalProducts++;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
		this.id = id;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPublicPrice() {
        return publicPrice;
    }

    public void setPublicPrice(double publicPrice) {
        this.publicPrice = publicPrice;
    }

    public double getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(double wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public static int getTotalProducts() {
        return totalProducts;
    }

    public void expire() {
        this.publicPrice = this.publicPrice * EXPIRATION_RATE;
    }

    @Override
    public String toString() {
        return "Product [ID=" + id + ", name=" + name + ", publicPrice=" + publicPrice + ", wholesalerPrice="
                + wholesalerPrice + ", available=" + available + ", stock=" + stock + "]";
    }
}
