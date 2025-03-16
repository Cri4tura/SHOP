package main;

import model.Product;
import model.Sale;
import model.Amount;
import model.Client;
import model.Employee;

import java.util.ArrayList;
import java.util.Scanner;

import dao.*;

public class Shop {
    public Amount cash = new Amount(100.00);
    public static ArrayList<Product> inventory;
    private int numberProducts;
    private ArrayList<Sale> sales;
    private int numberSales;
    public Dao dao;

    final static double TAX_RATE = 1.04;

    public Shop() {
        inventory = new ArrayList<>();
        sales = new ArrayList<>();
        dao = new DaoImplMongoDB();
        dao.connect();
        loadInventory();
    }

    public Amount getCash() {
        return cash;
    }

    public void setCash(Amount cash) {
        this.cash = cash;
    }

    public ArrayList<Product> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Product> inventory) {
        this.inventory = inventory;
    }

    public int getNumberProducts() {
        return numberProducts;
    }

    public void setNumberProducts(int numberProducts) {
        this.numberProducts = numberProducts;
    }

    public ArrayList<Sale> getSales() {
        return sales;
    }

    public void setSales(ArrayList<Sale> sales) {
        this.sales = sales;
    }

    public int getNumberSales() {
        return numberSales;
    }

    public void setNumberSales(int numberSales) {
        this.numberSales = numberSales;
    }

    public void loadInventory() {
        this.setInventory(dao.getInventory());
    }

    public boolean writeInventory() {
        return dao.writeInventory(this.inventory);
    }

    public void addProduct(Product product) {
        if (isInventoryFull()) {
            System.out.println("No se pueden añadir más productos, se ha alcanzado el máximo de " + inventory.size());
            return;
        }
        inventory.add(product);
        numberProducts++;
        dao.addProduct(product); // Sincroniza con MongoDB
    }

    public void removeProduct(String name) {
        Product product = findProduct(name);
        if (product != null) {
            inventory.remove(product);
            dao.deleteProduct(product); // Sincroniza con MongoDB
            System.out.println("Producto " + name + " eliminado.");
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    public void addStock(String name, int stock) {
        Product product = findProduct(name);
        if (product != null) {
            product.setStock(product.getStock() + stock);
            dao.updateProduct(product); // Sincroniza con MongoDB
            System.out.println("Stock de " + name + " actualizado a " + product.getStock());
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    public Product findProduct(String name) {
        for (Product product : inventory) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    public boolean isInventoryFull() {
        return numberProducts >= 10;
    }

    public void showCash() {
        System.out.println("Dinero en caja: " + cash.getValue());
    }

    public void sale() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Realizar venta, escribir nombre cliente");
        String nameClient = sc.nextLine();
        Client client = new Client(nameClient);

        ArrayList<Product> shoppingCart = new ArrayList<>();
        int numberShopping = 0;
        Amount totalAmount = new Amount(0.0);
        String name = "";

        while (!name.equals("0")) {
            System.out.println("Introduce el nombre del producto, escribir 0 para terminar:");
            name = sc.nextLine();

            if (name.equals("0")) {
                break;
            }

            Product product = findProduct(name);
            boolean productAvailable = false;

            if (product != null && product.isAvailable()) {
                productAvailable = true;
                product.setStock(product.getStock() - 1);
                shoppingCart.add(product);
                numberShopping++;
                totalAmount.setValue(totalAmount.getValue() + product.getPublicPrice().getValue());

                if (product.getStock() == 0) {
                    product.setAvailable(false);
                }
                System.out.println("Producto añadido con éxito");
            }

            if (!productAvailable) {
                System.out.println("Producto no encontrado o sin stock");
            }
        }

        totalAmount.setValue(totalAmount.getValue() * TAX_RATE);
        System.out.println("Venta realizada con éxito, total: " + totalAmount);

        if (!client.pay(totalAmount)) {
            System.out.println("Cliente debe: " + client.getBalance());
        }

        Sale sale = new Sale(client, shoppingCart, totalAmount);
        sales.add(sale);
        cash.setValue(cash.getValue() + totalAmount.getValue());
    }

    public void showSales() {
        System.out.println("Lista de ventas:");
        for (Sale sale : sales) {
            if (sale != null) {
                System.out.println(sale);
            }
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("Exportar fichero ventas? S / N");
        String option = sc.nextLine();
        if ("S".equalsIgnoreCase(option)) {
            this.writeSales();
        }
    }

    private void writeSales() {
        // Implementación para escribir las ventas en un archivo
        // (Código omitido por brevedad)
    }

    public void showSalesAmount() {
        Amount totalAmount = new Amount(0.0);
        for (Sale sale : sales) {
            if (sale != null) {
                totalAmount.setValue(totalAmount.getValue() + sale.getAmount().getValue());
            }
        }
        System.out.println("Total cantidad ventas:");
        System.out.println(totalAmount);
    }

    public static void main(String[] args) {
        Shop shop = new Shop();
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;
        boolean exit = false;

        do {
            System.out.println("\n===========================");
            System.out.println("Menu principal miTienda.com");
            System.out.println("===========================");
            System.out.println("1) Contar caja");
            System.out.println("2) Añadir producto");
            System.out.println("3) Añadir stock");
            System.out.println("4) Marcar producto proxima caducidad");
            System.out.println("5) Ver inventario");
            System.out.println("6) Venta");
            System.out.println("7) Ver ventas");
            System.out.println("8) Ver venta total");
            System.out.println("9) Eliminar producto");
            System.out.println("10) Salir programa");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    shop.showCash();
                    break;
                case 2:
                    shop.addProduct();
                    break;
                case 3:
                    shop.addStock();
                    break;
                case 4:
                    shop.setExpired();
                    break;
                case 5:
                    shop.showInventory();
                    break;
                case 6:
                    shop.sale();
                    break;
                case 7:
                    shop.showSales();
                    break;
                case 8:
                    shop.showSalesAmount();
                    break;
                case 9:
                    shop.removeProduct();
                    break;
                case 10:
                    System.out.println("Cerrando programa ...");
                    exit = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        } while (!exit);
    }

    public void addProduct() {
        if (isInventoryFull()) {
            System.out.println("No se pueden añadir más productos, se ha alcanzado el máximo de " + inventory.size());
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre del producto: ");
        String name = scanner.nextLine();
        System.out.print("Precio del producto: ");
        double price = scanner.nextDouble();
        System.out.print("Stock del producto: ");
        int stock = scanner.nextInt();

        addProduct(new Product(name, new Amount(price), true, stock));
    }

    public void removeProduct() {
        if (inventory.size() == 0) {
            System.out.println("No se pueden eliminar productos, inventario vacío");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre del producto a eliminar: ");
        String name = scanner.nextLine();
        removeProduct(name);
    }

    public void addStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre del producto: ");
        String name = scanner.nextLine();
        System.out.print("Cantidad de stock a añadir: ");
        int stock = scanner.nextInt();
        addStock(name, stock);
    }

    public void setExpired() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nombre del producto: ");
        String name = scanner.nextLine();
        Product product = findProduct(name);
        if (product != null) {
            product.expire();
            System.out.println("El precio del producto " + name + " ha sido actualizado a " + product.getPublicPrice());
        } else {
            System.out.println("Producto no encontrado.");
        }
    }

    public void showInventory() {
        System.out.println("Contenido actual de la tienda:");
        for (Product product : inventory) {
            if (product != null) {
                System.out.println(product);
            }
        }
    }
}