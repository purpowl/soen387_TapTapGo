package com.taptapgo.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.taptapgo.Staff;
import com.taptapgo.exceptions.InvalidNumberException;

@WebServlet(name = "createProductServlet", value = "/create-product")
public class CreateProductServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

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
        try{
            int amount = Integer.parseInt(amount_str);
            if (amount <= 0) {
                throw new InvalidNumberException("Negative amount entered.");
            }

            double price = Double.parseDouble(price_str);
            if (price <= 0) {
                throw new InvalidNumberException("Negative price entered.");
            }
            staff.createProduct(SKU, name, price, vendor, desc, amount);
        } catch(NumberFormatException e){
            // output message if there's an issue creating product and reset page
            response.sendRedirect("create-product.jsp");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/create-product.jsp");
            PrintWriter out= response.getWriter();
            out.println("<font color=red>Unable to create product. Invalid number entered for price or amount.</font>");
            rd.include(request, response);
        } catch (Exception e) {
            // output message if there's an issue creating product and reset page
            response.sendRedirect("create-product.jsp");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/create-product.jsp");
            PrintWriter out= response.getWriter();
            out.println("<font color=red>Unable to create product. " + e.getMessage() + "</font>");
            rd.include(request, response);
        }
        // output message for successful product creation and reset page
        response.sendRedirect("create-product.jsp");
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/create-product.jsp");
        PrintWriter out= response.getWriter();
        out.println("<font color=green>Product created.</font>");
        rd.include(request, response);
    }
}