package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.Employee;
import model.Product;
import model.ProductList;

public class DaoImplJDBC implements Dao {
	private Connection connection;
	
    public DaoImplJDBC() {
        connect();
    }
	
	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1,employeeId);
    	  	ps.setString(2,password);
    	  	//System.out.println(ps.toString());
            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            		employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
            	}
            }
        } catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
    	return employee;
	}


    @Override
    public ArrayList<Product> getInventory(){
        ArrayList<Product> products = new ArrayList<>(); 
       
        try (Statement ps = this.connection.createStatement(); 
             ResultSet rs = ps.executeQuery("SELECT * FROM inventory")) {
            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), 
                		rs.getString("name"),
                        rs.getDouble("wholesalerPrice"), 
                        rs.getBoolean("available"),
                        rs.getInt("stock")));
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return products; 
    }

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) { 
	    connect();
	    boolean isExported = false;
	    LocalDateTime now = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String formattedDate = now.format(formatter);

	    for (Product product : inventory) {  
	        String query = "insert into historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) VALUES (?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement statement = connection.prepareStatement(query)) {
	            statement.setInt(1, product.getId());
	            statement.setString(2, product.getName());
	            statement.setDouble(3, product.getWholesalerPrice().getValue());
	            statement.setBoolean(4, product.isAvailable());
	            statement.setInt(5, product.getStock());
	            statement.setString(6, formattedDate);
	            statement.executeUpdate();
	            isExported = true;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    disconnect();
	    return isExported;
	}


	@Override
	public void addProduct(Product product) {
		connect();
		String query = "insert into inventory (id, name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?, ?);";

		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, product.getId());
			statement.setString(2, product.getName());
			statement.setDouble(3, product.getWholesalerPrice().getValue());
			statement.setBoolean(4, product.isAvailable());
			statement.setInt(5, product.getStock());
			statement.executeUpdate();
			 System.out.println(product.getName() + " ha sido añadido a la base de datos");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) {
        String updateProduct = "UPDATE inventory SET stock = ?, wholesalerPrice = ?, available = ? WHERE name = ?"; 
        
        try (PreparedStatement statement = this.connection.prepareStatement(updateProduct)) {
            statement.setInt(1, product.getStock());
            statement.setDouble(2, product.getWholesalerPrice().getValue());
            statement.setBoolean(3, product.isAvailable());
            statement.setString(4, product.getName());
            int rowsAffected = statement.executeUpdate(); 
            if (rowsAffected > 0) {
                System.out.println("Producto actualizado: " + product.getName());
            } else {
                System.out.println("No se ha encontrado: " + product.getName());
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteProduct(Product product) {
        String deleteProduct = "DELETE FROM inventory WHERE name = ?"; 
        
        try (PreparedStatement deletePs = this.connection.prepareStatement(deleteProduct)) {
            deletePs.setString(1, product.getName());
            int rowsAffected = deletePs.executeUpdate(); 
            if (rowsAffected > 0) {
                System.out.println("Producto borrado correctamente: " + product.getName());
            } else {
                System.out.println("No se ha encontrado: " + product.getName());
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
