package com.taptapgo.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.taptapgo.Staff;

@WebServlet(name = "downloadProductCatalog", value = "/download-catalog")
public class DownloadCatalogServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/products.jsp");
        PrintWriter out = response.getWriter();

        response.setHeader("Content-Type", "text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"Product_List.csv\"");

        String fileContents = Staff.getProductCatalog();
        
        try{
            // send response to download csv
            out.append(fileContents);

        } catch (Exception e) {
            out.println("<font color=red>Cannot download catalog.</font>");
            rd.include(request, response);
        }
    }
}