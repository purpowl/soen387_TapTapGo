package com.taptapgo.servlets;

import java.io.IOException;

import com.taptapgo.Product;
import com.taptapgo.Warehouse;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "productDetailServlet", value = "/product/*")
public class ProductDetailServlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        Warehouse warehouse = Warehouse.getInstance();
        String slug = request.getPathInfo().substring(1);


        Product product = warehouse.findProductBySlug(slug);
        if (product == null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product not found in warehouse");
        } else {
            request.setAttribute("product", product);

            // Forward to product view page
            request.getRequestDispatcher("/product-view.jsp").forward(request, response);
        }
    }
}
