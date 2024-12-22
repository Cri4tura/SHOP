CREATE DATABASE shop;
USE shop;

CREATE TABLE employee(
employeeId INT PRIMARY KEY,
name VARCHAR (100),
password VARCHAR (100)
);
INSERT INTO employee ( employeeId, name, password) VALUES (123, 'Test User', 'test');
INSERT INTO employee ( employeeId, name, password) VALUES (456, 'Test User1', 'pass');
INSERT INTO employee ( employeeId, name, password) VALUES (111, 'Test User2', 'uno'); 

select * from employee;

CREATE TABLE inventory (
	id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    wholesalerPrice DECIMAL(10,2) NOT NULL,
    available boolean NOT NULL,
    stock INT NOT NULL
);
CREATE TABLE historical_inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_product INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    wholesalerPrice DECIMAL(10, 2) NOT NULL,
    available INT NOT NULL,
    stock INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

SELECT * FROM EMPLOYEE;

SELECT * FROM historical_inventory;

-- Insertar productos de una sola palabra en la tabla inventory
INSERT INTO inventory (id, name, wholesalerPrice, available, stock) 
VALUES 
    (1, 'Arroz', 1.50, TRUE, 500),
    (2, 'Pasta', 0.90, TRUE, 350),
    (3, 'Aceite', 4.50, TRUE, 150),
    (4, 'Harina', 0.80, TRUE, 600),
    (5, 'Azúcar', 1.20, TRUE, 800),
    (6, 'Lentejas', 1.00, TRUE, 300),
    (7, 'Leche', 1.80, TRUE, 400),
    (8, 'Manteca', 2.40, TRUE, 200),
    (9, 'Sardinas', 1.50, TRUE, 150),
    (10, 'Tomates', 1.20, TRUE, 250);

SELECT * FROM inventory;