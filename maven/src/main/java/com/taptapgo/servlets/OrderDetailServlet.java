package com.taptapgo.servlets;

import java.io.IOException;

import com.taptapgo.Order;
import com.taptapgo.repository.OrderIdentityMap;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "orderDetailServlet", value = "/orders/*")
public class OrderDetailServlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String orderID_str = request.getPathInfo().substring(1);
        HttpSession currentSession = request.getSession();
        boolean isStaff = false;
        if (currentSession.getAttribute("staff") != null) {
            isStaff = true;
        } 

        Integer orderID = null;
            // Error handling for incorrect format orderID
            try {
                orderID = Integer.parseInt(orderID_str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                if (isStaff) {
                    response.sendRedirect(request.getContextPath() + "/ship-orders.jsp?search=fail");
                } else {
                    response.sendRedirect(request.getContextPath() + "/orders.jsp?search=fail");
                }
            }
            
            Order order = OrderIdentityMap.getOrderByID(orderID);
            if (order == null) {
                if(isStaff) {
                    response.sendRedirect(request.getContextPath() + "/ship-orders.jsp?search=fail");
                } else {
                    response.sendRedirect(request.getContextPath() + "/orders.jsp?search=fail");
                }
            } else {
                currentSession.setAttribute("order", order);

                if(isStaff) {
                    response.sendRedirect(request.getContextPath() + "/ship-orders.jsp?search=success");
                } else {
                    response.sendRedirect(request.getContextPath() + "/orders.jsp?search=success");
                }
            }
    }
}
