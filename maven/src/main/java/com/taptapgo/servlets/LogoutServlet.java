package com.taptapgo.servlets;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.HashMap;

import com.taptapgo.Product;

@WebServlet(name = "logoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        // log staff out
        if (session != null) {
            session.invalidate();
            HashMap<Product, Integer> cart = new HashMap<Product, Integer>();
            session.setAttribute("cart", cart);
            session.setAttribute("userID", "gc" + session.getId());
        }
        response.sendRedirect("index.jsp");
    }
}
