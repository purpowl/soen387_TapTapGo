package com.taptapgo.servlets;

import java.io.IOException;

import com.taptapgo.Order;
import com.taptapgo.repository.OrderIdentityMap;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "orderServlet", value = "/orders")
public class OrderServlet extends HttpServlet{
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession currentSession = request.getSession();
        Object staff_object = currentSession.getAttribute("staff");
        Object registeredUser_object = currentSession.getAttribute("registered_user");

        if(registeredUser_object != null) {
            response.sendRedirect(request.getContextPath() + "/user-orders.jsp");
        } else if (staff_object != null) {
            response.sendRedirect(request.getContextPath() + "/ship-orders.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/orders.jsp");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String orderID_str = request.getParameter("orderID");
        HttpSession currentSession = request.getSession();
        boolean isStaff = false;
        if (currentSession.getAttribute("staff") != null) {
            isStaff = true;
        }

        // Error handling for empty search field
        if (orderID_str.isEmpty()) {
            if(isStaff){
                response.sendRedirect(request.getContextPath() + "/ship-orders.jsp?search=fail");
            } else {
                response.sendRedirect(request.getContextPath() + "/orders.jsp?search=fail");
            }
        } 
        
        else {
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
}
