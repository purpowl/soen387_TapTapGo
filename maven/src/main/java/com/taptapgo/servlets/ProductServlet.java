package com.taptapgo.servlets;

import java.io.IOException;
import java.util.HashMap;

import com.taptapgo.Product;
import com.taptapgo.Staff;
import com.taptapgo.Warehouse;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "productServlet", value = "/products/*")
public class ProductServlet extends HttpServlet {
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getParameter("method");
        String slug = request.getParameter("slug");

        /* Modify the product for POST method */
        if (method.equals("post")){
            String name = request.getParameter("name");
            String price_str = request.getParameter("price");
            String desc = request.getParameter("desc");
            String vendor = request.getParameter("vendor");
            String amount_str = request.getParameter("amount");

            // Put the fields into a dictionary
            HashMap<String, Object> updateFields = new HashMap<>();
            if (!name.isEmpty()) {
                updateFields.put("name", name);
            }
            if (!price_str.isEmpty()) {
                updateFields.put("price", price_str);
            }
            if (!desc.isEmpty()) {
                updateFields.put("description", desc);
            }
            if (!vendor.isEmpty()) {
                updateFields.put("vendor", vendor);
            }
            if (!amount_str.isEmpty()) {
                updateFields.put("amount", amount_str);
            }

            try {
                Staff.updateProduct(slug, updateFields);

                // output message for successful product modification and reset page
                response.sendRedirect(request.getContextPath() + "/manage-product.jsp?modify=success");
            } catch (Exception e) {
                // output message if there's an issue modifying product and reset page
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/manage-product.jsp?modify=fail");
            }
        } else if (method.equals("delete")) {
            try {
                Staff.deleteProduct(slug);

                // output message for successful product deletion and reset page
                response.sendRedirect(request.getContextPath() + "/manage-product.jsp?delete=success");
            } catch (Exception e) {
                // output message if there's an issue deleting product and reset page
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/products.jsp?delete=fail");
            }
        }
        
        
    }
}
