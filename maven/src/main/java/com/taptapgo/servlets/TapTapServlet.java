package com.taptapgo.servlets;

import java.io.*;
import java.util.HashMap;
import com.taptapgo.*;
import com.taptapgo.repository.UserIdentityMap;
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
        User.updateRegisteredIDGen(UserIdentityMap.getMaxRegisteredUserID());
        context.setAttribute("warehouse", warehouse);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession currentSession = request.getSession();
        Object cart_object = currentSession.getAttribute("cart");
        Object userID_object = currentSession.getAttribute("userID");

        // Create cart for new anonymous user
        if (cart_object == null){
            HashMap<Product, Integer> cart = new HashMap<>();
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