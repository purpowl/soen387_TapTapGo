package src;

import src.Product;
import src.Warehouse;

public class Driver {
    public static void main(String[] args) {
        Warehouse warehouse = Warehouse.getInstance();
        Product test = new Product();
        warehouse.addProduct(test, 0);
    }
    
}
