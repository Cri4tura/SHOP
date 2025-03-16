package dao;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

import model.Employee;
import model.Product;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DaoImplMongoDB implements Dao {
    private MongoClient mongoClient;
    private MongoDatabase database;

    @Override
    public void connect() {
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        database = mongoClient.getDatabase("shop");
    }

    @Override
    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    @Override
    public ArrayList<Product> getInventory() {
        ArrayList<Product> inventory = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("inventory");

        for (Document doc : collection.find()) {
            Document wholesalerPriceDoc = (Document) doc.get("wholesalerPrice");
            double wholesalerPriceValue = wholesalerPriceDoc.getDouble("value");

            Product product = new Product(
                doc.getInteger("id"),
                doc.getString("name"),
                wholesalerPriceValue,
                doc.getBoolean("available"),
                doc.getInteger("stock")
            );
            inventory.add(product);
        }
        
        System.out.println("Inventario cargado: " + inventory);
        return inventory;
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        try {
            MongoCollection<Document> collection = database.getCollection("historical_inventory");
            ArrayList<Document> documents = new ArrayList<>();

            for (Product product : inventory) {
                LocalDateTime date = LocalDateTime.now();
                Document doc = new Document("id", product.getId())
                    .append("name", product.getName())
                    .append("wholesalerPrice", new Document("value", product.getWholesalerPrice().getValue())
                        .append("currency", product.getWholesalerPrice().getCurrency()))
                    .append("available", product.isAvailable())
                    .append("stock", product.getStock())
                    .append("created_at", date);
                documents.add(doc);
            }
            collection.insertMany(documents);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void addProduct(Product product) {
        MongoCollection<Document> collection = database.getCollection("inventory");
        int newId = 0;
        Document lastDocument = collection.find().sort(Sorts.descending("id")).first();
        if (lastDocument != null) {
            newId = lastDocument.getInteger("id") + 1;
        }

        Document document = new Document("_id", new ObjectId())
            .append("id", newId)
            .append("name", product.getName())
            .append("wholesalerPrice", new Document("value", product.getWholesalerPrice().getValue())
                .append("currency", product.getWholesalerPrice().getCurrency()))
            .append("available", product.isAvailable())
            .append("stock", product.getStock());

        collection.insertOne(document);
    }

    @Override
    public void updateProduct(Product product) {
        MongoCollection<Document> collection = database.getCollection("inventory");
        Document document = collection.find(Filters.eq("name", product.getName())).first();
        if (document != null) {
            collection.updateOne(document, Updates.set("stock", product.getStock()));
        } else {
            System.out.println("Producto no encontrado");
        }
    }

    @Override
    public void deleteProduct(Product product) {
        MongoCollection<Document> collection = database.getCollection("inventory");
        Document document = collection.find(Filters.eq("name", product.getName())).first();
        if (document != null) {
            collection.deleteOne(document);
        } else {
            System.out.println("Producto no encontrado");
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        MongoCollection<Document> collection = database.getCollection("users");
        Document doc = collection.find(Filters.and(
            Filters.eq("employeeId", employeeId),
            Filters.eq("password", password)
        )).first();

        if (doc != null) {
        	System.out.println("Datos empleado: " + doc.toJson());
            return new Employee(employeeId, doc.getString("name"), password);
        }
        return null;
    }

}