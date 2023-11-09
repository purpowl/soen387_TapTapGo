package com.taptapgo.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

import com.taptapgo.Order;
import com.taptapgo.repository.OrderIdentityMap;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "shipOrderServlet", value = "/shipOrder/*")
public class ShipOrderServlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String orderID_str = request.getPathInfo().substring(1);
        Integer orderID = null;

        try{
            orderID = Integer.parseInt(orderID_str);
        } catch(NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/manage-order.jsp");
            return;
        }

        Order order = OrderIdentityMap.getOrderByID(orderID);
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/manage-order.jsp");
            return;
        }

        HttpSession currentSession = request.getSession();
        currentSession.setAttribute("order", order);
        response.sendRedirect(request.getContextPath() + "/ship-order.jsp");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession currentSession = request.getSession();
        Order order = (Order) currentSession.getAttribute("order");

        String status = request.getParameter("ShippingStatus");
        String trackingNumber = request.getParameter("TrackingNumber");

        // Parse the date
        String shipDate = request.getParameter("ShipDate");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(shipDate, dateFormatter);
        Date sqlDate = Date.valueOf(localDate);

        boolean db_result = OrderIdentityMap.shipOrder(order, trackingNumber, status, sqlDate);
        if(db_result) {
            response.sendRedirect(request.getContextPath() + "/manage-order.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/ship-order.jsp");
        }
    }
}