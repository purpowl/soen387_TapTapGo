package com.taptapgo.servlets;

import java.io.*;

import com.taptapgo.Staff;
import com.taptapgo.Warehouse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "tapTapServlet", value = "/tap-tap-servlet")
public class TapTapServlet extends HttpServlet {

    public void init() {
        // create an in memory warehouse and staff instance accessible by other servlets
        // only need 1 staff instance for now, with universal password
        ServletContext context = getServletContext();
        Warehouse warehouse = Warehouse.getInstance();
        context.setAttribute("warehouse", warehouse);
        Staff staff = new Staff("adminStaff", "secret");
        context.setAttribute("staff", staff);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public void destroy() {
    }
}