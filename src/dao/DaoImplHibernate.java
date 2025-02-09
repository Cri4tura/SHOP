package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import model.Amount;
import model.Employee;
import model.Product;
import model.ProductHistory;
import model.ProductList;

public class DaoImplHibernate implements Dao {

	private Connection connection;
	private SessionFactory factory;

	public DaoImplHibernate() {
		connect();
	}

	@Override
	public void connect() {
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
			factory = new Configuration()
					.configure("hibernate.cfg.xml")
					.addAnnotatedClass(Product.class)
					.buildSessionFactory();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null && factory != null) {
			try {
				connection.close();
				factory.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public Employee getEmployee(int user, String password) {
		try (Session session = factory.openSession()) {
			return session.get(Employee.class, user);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> products = null;
		try (Session session = factory.getCurrentSession()) {
			session.beginTransaction();

			EntityManager em = session.getEntityManagerFactory().createEntityManager();
			TypedQuery<Product> query = em.createQuery("from Product", Product.class);
			products = (ArrayList<Product>) query.getResultList();


			session.getTransaction().commit();

			System.out.println("Inventario cargado correctamente");
			System.out.println(products);
			return products;
		}catch (Exception e) {
			System.out.println("Error cargando inventario");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		try(Session session = factory.getCurrentSession()) {
			session.beginTransaction();

			for(Product product : inventory) {
				ProductHistory history = new ProductHistory();
				history.setIdProduct(product.getId());
				history.setAvailable(product.isAvailable());
				history.setCreatedAt(LocalDateTime.now());
				history.setName(product.getName());
				history.setPrice(product.getPrice());
				history.setStock(product.getStock());

				session.save(history);
			}

			session.getTransaction().commit();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void addProduct(Product product) {
		connect();
		try (Session session = factory.openSession()) {
			session.beginTransaction();
			session.save(product);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	@Override
	public void updateProduct(Product product) {
		connect();
		try (Session session = factory.openSession()) {
			session.beginTransaction();
			session.update(product);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	@Override
	public void deleteProduct(Product product) {
		connect();
		try (Session session = factory.openSession()) {
			session.beginTransaction();
			session.delete(product);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}
}
