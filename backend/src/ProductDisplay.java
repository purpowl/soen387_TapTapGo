package src;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;

@WebServlet("/products")
public class ProductDisplay extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        HashMap<Product, Integer> product_list = Warehouse.getInstance().getProductList();

        
    }
}
