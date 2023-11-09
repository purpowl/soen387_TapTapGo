package com.taptapgo.servlets;

import java.io.*;
import java.util.HashMap;
import java.util.Random;

import com.taptapgo.Order;
import com.taptapgo.Product;
import com.taptapgo.Staff;
import com.taptapgo.Warehouse;
import com.taptapgo.repository.OrderIdentityMap;

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
        warehouse.loadDatabase();
        Order.updateOrderCounter(OrderIdentityMap.getMaxOrderID());
        context.setAttribute("warehouse", warehouse);
        Staff staff = new Staff("adminStaff", "secret");
        context.setAttribute("staff", staff);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession currentSession = request.getSession();
        Object cart_object = currentSession.getAttribute("cart");
        Object userID_object = currentSession.getAttribute("userID");

        // Create cart for new anonymous user
        if (cart_object == null){
            HashMap<Product, Integer> cart = new HashMap<Product, Integer>();
            currentSession.setAttribute("cart", cart);
        }

        // Create userID for new anonymous user
        if(userID_object == null) {
            currentSession.setAttribute("userID", "gc" + currentSession.getId());
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public void destroy(){
        
    }
}