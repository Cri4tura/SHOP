package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import model.Amount;
import model.Employee;
import model.Product;
import model.Sale;

public class DaoImplFile implements Dao {

	@Override
	public void connect() {
		// TODO Auto-generated method stub

	}	
	
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Product> getInventory() {
		// TODO Auto-generated method stub
		ArrayList<Product> inventoryLoaded = new ArrayList<Product>();

		// locate file, path and name
		File f = new File(System.getProperty("user.dir") + File.separator + "files/inputInventory.txt");

		try {			
			// wrap in proper classes
			FileReader fr;
			fr = new FileReader(f);				
			BufferedReader br = new BufferedReader(fr);

			// read first line
			String line = br.readLine();

			// process and read next line until end of file
			while (line != null) {
				// split in sections
				String[] sections = line.split(";");

				String name = "";
				double wholesalerPrice=0.0;
				int stock = 0;

				// read each sections
				for (int i = 0; i < sections.length; i++) {
					// split data in key(0) and value(1) 
					String[] data = sections[i].split(":");

					switch (i) {
					case 0:
						// format product name
						name = data[1];
						break;

					case 1:
						// format price
						wholesalerPrice = Double.parseDouble(data[1]);
						break;

					case 2:
						// format stock
						stock = Integer.parseInt(data[1]);
						break;

					default:
						break;
					}
				}

				// read next line
				line = br.readLine();

				// Add a product with the extracted information to some data structure
				inventoryLoaded.add(new Product(name, new Amount(wholesalerPrice), true, stock));
			}
			fr.close();
			br.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inventoryLoaded;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> products) {
		// define file name based on date
		LocalDate myObj = LocalDate.now();
		String fileName = "inventory_" + myObj.toString() + ".txt";

		try {
			// locate file, path and name
			File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);

			
			// wrap in proper classes
			FileWriter fw;
			fw = new FileWriter(f, true);
			PrintWriter pw = new PrintWriter(fw);

			// write line by line
			int count=1;
			for (Product product : products) {			
				StringBuilder productLine = new StringBuilder(count + ";Product:"+product.getName()+";Stock:" + product.getStock()+";");
				pw.write(productLine.toString());
				fw.write("\n");

				// increment counter products
				count++;
			}

			StringBuilder totalProducts = new StringBuilder("Número total de productos: " + (count - 1) + ";");
			pw.write(totalProducts.toString());
			fw.write("\n");
			
			// close files
			pw.close();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}		
		
		return true;
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
