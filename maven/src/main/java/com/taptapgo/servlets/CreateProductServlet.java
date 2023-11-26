package com.taptapgo.servlets;

import java.io.IOException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.taptapgo.Staff;
import com.taptapgo.exceptions.InvalidNumberException;

@WebServlet(name = "createProductServlet", value = "/create-product")
public class CreateProductServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        try {
            // get request parameters for product sku and name
            String SKU = request.getParameter("sku");
            String name = request.getParameter("name");
            String price_str = request.getParameter("price");
            String desc = request.getParameter("desc");
            String vendor = request.getParameter("vendor");
            String amount_str = request.getParameter("amount");

            // get staff attribute to create new product
            ServletContext context = getServletContext();
            Staff staff = (Staff) context.getAttribute("staff");

            int amount = 0;
            float price = 0;

            if (!amount_str.isEmpty()) {
                amount = Integer.parseInt(amount_str);
                if (amount <= 0) {
                    throw new InvalidNumberException("Negative amount entered.");
                }
            }

            if (!price_str.isEmpty()) {
                price = Float.parseFloat(price_str);
                if (price <= 0) {
                    throw new InvalidNumberException("Negative price entered.");
                }
            }

            staff.createProduct(SKU, name, price, vendor, desc, amount);

        } catch (Exception e) {
            // output message for failed product creation and redirect to products page
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/products.jsp?create=fail");
            return;
        }

        // output message for successful product creation and redirect to products page
        response.sendRedirect(request.getContextPath() + "/products.jsp?create=success");
    }
}