package com.taptapgo.servlets;

import java.io.IOException;

import com.taptapgo.Order;
import com.taptapgo.User;
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
        boolean isStaff = currentSession.getAttribute("staff") != null;

        Integer orderID;
        // Error handling for incorrect format orderID
        try {
            orderID = Integer.parseInt(orderID_str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            if (isStaff) {
                response.sendRedirect(request.getContextPath() + "/ship-orders.jsp?search=fail");
                return;
            } else {
                response.sendRedirect(request.getContextPath() + "/orders.jsp?search=fail");
                return;
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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession currentSession = request.getSession();
        if(currentSession.getAttribute("isRegisteredUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) currentSession.getAttribute("registered_user");
        String userID = user.getUserID();
        String orderID_str = request.getParameter("orderID");
        int orderID = Integer.parseInt(orderID_str);

        boolean db_result = OrderIdentityMap.setOrderOwner(orderID, userID);
        if(!db_result) {
            response.sendRedirect(request.getContextPath() + "/orders.jsp?reclaim=fail");
        } else {
            response.sendRedirect(request.getContextPath() + "/user-orders.jsp");
        }
    }
}
