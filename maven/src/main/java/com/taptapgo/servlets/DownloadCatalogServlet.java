package com.taptapgo.servlets;

import java.io.IOException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.taptapgo.Staff;

@WebServlet(name = "downloadProductCatalog", value = "/download-catalog")
public class DownloadCatalogServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // get staff attribute to create new product
        ServletContext context = getServletContext();
        Staff staff = (Staff) context.getAttribute("staff");
        try{
            // prob need to modify method to return data, which we write as http response
            //staff.downloadProductCatalog();
        }
        catch (Exception e) {
            System.out.println("Cannot download catalog.");
        }
        response.sendRedirect("products.jsp");
    }
}