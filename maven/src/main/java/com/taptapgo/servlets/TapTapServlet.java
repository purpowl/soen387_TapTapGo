package com.taptapgo.servlets;

import java.io.IOException;
import java.util.HashMap;

import com.taptapgo.*;
import com.taptapgo.repository.UserIdentityMap;
import com.taptapgo.repository.OrderIdentityMap;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "tapTapServlet", value = "/taptapgo")
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
        HttpSession session = request.getSession();

        // if user is not registered
        if (session.getAttribute("isRegisteredUser") == null) {
            // create new cart if they don't have a cart yet
            if (session.getAttribute("cart") == null) {
                HashMap<Product, Integer> cart = new HashMap<>();
                session.setAttribute("cart", cart);
            }
            // create guest ID if they don't have one
            if (session.getAttribute("userID") == null){
                session.setAttribute("userID", session.getId());
            } 
        } else {
            // else get their registered user ID
            session.setAttribute("userID", ((User) session.getAttribute("registered_user")).getUserID());
        }
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}