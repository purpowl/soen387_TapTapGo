package com.taptapgo.servlets;

import java.io.IOException;
import java.util.HashMap;

import com.taptapgo.Order;
import com.taptapgo.Product;
import com.taptapgo.User;
import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.repository.UserIdentityMap;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "checkoutServlet", value = "/checkout")
public class CheckoutServlet extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        HttpSession currentSession = request.getSession();
        boolean isRegistered = false;
        User user = null;
        String userID;

        if(currentSession.getAttribute("registered_user") != null) {
            isRegistered = true;
            user = (User) currentSession.getAttribute("registered_user");
        }

        if(!isRegistered) {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            userID = (String) currentSession.getAttribute("userID");

            try {
                user = UserIdentityMap.getUserByID("gc" + userID);
            } catch (InvalidParameterException e) {
                // Skip error in case user doesn't exist in database
            }

            if(user == null) {
                try {
                    user = User.createGuestUserWithInfo(userID, firstName, lastName, phone, email);

                    boolean db_result;

                    db_result = UserIdentityMap.addGuestUserToDB(user);

                    if (!db_result) {
                        response.sendRedirect(request.getContextPath() + "/checkout.jsp?checkout=dbcustfail");
                        return;
                    }
                }
                catch (InvalidParameterException e) {
                    response.sendRedirect(request.getContextPath() + "/checkout.jsp?checkout=dbcustfail");
                    e.printStackTrace();
                    return;
                }
            }
            else {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
            }
        }

        // Load current cart into customer
        HashMap<Product, Integer> cart = (HashMap<Product, Integer>) currentSession.getAttribute("cart");
        if (cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart.jsp");
            return;
        }
        user.loadCart(cart);

        String billAddress = request.getParameter("billAddress");
        String billCity = request.getParameter("billCity");
        String billCountry = request.getParameter("billCountry");
        String billPostalCode = request.getParameter("billPostalCode");
        String shipAddress = request.getParameter("shipAddress");
        String shipCity = request.getParameter("shipCity");
        String shipCountry = request.getParameter("shipCountry");
        String shipPostalCode = request.getParameter("shipPostalCode");
        String paymentMethod = request.getParameter("paymentMethod");
        String ccNumber = request.getParameter("cc-number");
        int ccNumberLast4Digits;

        // Handle bad inputs for credit card number
        if (ccNumber.length() != 16) {
            response.sendRedirect(request.getContextPath() + "/checkout.jsp?checkout=ccinputfail");
            return;
        } else {
            String last4Digits = ccNumber.substring(ccNumber.length()-4);
            try {
                ccNumberLast4Digits = Integer.parseInt(last4Digits);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/checkout.jsp?checkout=ccinputfail");
                return;
            }
        }

        Order newOrder = Order.createOrder(billAddress, billCity, billCountry, billPostalCode, paymentMethod, ccNumberLast4Digits, shipAddress, shipCity, shipCountry, shipPostalCode, cart, user.getUserID());
        boolean db_result = Order.addOrderToDB(newOrder);

        if (!db_result) {
            response.sendRedirect(request.getContextPath() + "/checkout.jsp?checkout=dborderfail");
        } else {
            currentSession.setAttribute("order", newOrder);
            user.clearCart();
            currentSession.setAttribute("cart", new HashMap<>());
            response.sendRedirect(request.getContextPath() + "/order-confirmation.jsp");
        }
    }
}
