package com.taptapgo.servlets;

import java.io.IOException;
import com.taptapgo.Order;
import com.taptapgo.repository.OrderIdentityMap;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "backServlet", value = "/back")
public class BackServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();

        String previousPage = request.getParameter("from");
        Integer orderID = Integer.parseInt(request.getParameter("orderID"));

        if (previousPage.contains("manage-order.jsp")) {
            response.sendRedirect(request.getContextPath() + "/manage-order.jsp");
        }
        else if (previousPage.contains("user-orders.jsp")) {
            response.sendRedirect(request.getContextPath() + "/user-orders.jsp");
        }
        else if (previousPage.contains("orders.jsp")) {
            Order order = OrderIdentityMap.getOrderByID(orderID);
            session.setAttribute("order", order);
            response.sendRedirect(request.getContextPath() + "/orders.jsp?search=success");
        }
    }
}