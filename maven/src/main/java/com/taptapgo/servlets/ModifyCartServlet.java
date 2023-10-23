package com.taptapgo.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.taptapgo.Product;
import com.taptapgo.Warehouse;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "modifyCartServlet", value = "/cart/*")
public class ModifyCartServlet extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String action = request.getPathInfo().substring(1);
        String slug = request.getParameter("slug");

        if (action.equals("add")){
            // Pull up the cart
            HttpSession currentSession = request.getSession();
            Object cartObject = currentSession.getAttribute("cart");
            HashMap<Product, Integer> cart;

            if (cartObject == null) {
                cart = CartServlet.loadCart(request);
            } else {
                cart = (HashMap<Product, Integer>) cartObject;
            }

            Map.Entry<Product, Integer> product_entry = findItemInCart(cart, slug);
            if (product_entry == null) {
                // If product is not already in cart, put it in cart as a new entry
                Product product_to_add = Warehouse.getInstance().findProductBySlug(slug);
                if(product_to_add == null) {
                    // If product not found in warehouse, raise error
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request. Product not found");
                }

                // Add product to cart
                cart.put(product_to_add, 1);
                redirect(request, response, cart, currentSession, true);
            } else {
                // If product is already in cart, increment the value
                int amount_prev = product_entry.getValue();
                int amount_avail = Warehouse.getInstance().getProductInventoryBySlug(slug);
                Product product_to_modify = product_entry.getKey();

                // Check if inventory is sufficient
                if (amount_avail >= (amount_prev+1)) {
                    cart.replace(product_to_modify, (amount_prev+1));
                    redirect(request, response, cart, currentSession, true);
                } else {
                    redirect(request, response, cart, currentSession, false);
                }
            }
        } else if (action.equals("remove")) {
            // Pull up the cart
            HttpSession currentSession = request.getSession();
            Object cartObject = currentSession.getAttribute("cart");
            HashMap<Product, Integer> cart;

            if (cartObject == null) {
                cart = CartServlet.loadCart(request);
            } else {
                cart = (HashMap<Product, Integer>) cartObject;
            }

            Map.Entry<Product, Integer> product_entry = findItemInCart(cart, slug);
            if (product_entry == null) {
                // If product not in cart, return error
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request. Product not in cart");
            } else {
                int amount_prev = product_entry.getValue();
                Product product_to_remove = product_entry.getKey();

                if ((amount_prev-1) > 0) {
                    // If after remove, there's still some product in cart, then modify the cart
                    cart.replace(product_to_remove, (amount_prev-1));
                    redirect(request, response, cart, currentSession, true);
                } else {
                    // If after remove, there's no more product, then remove the product entirely from cart
                    cart.remove(product_to_remove);
                    redirect(request, response, cart, currentSession, true);
                }
            }
        } else {
            // Handle the case where the action is incorrect
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action request");
        }
    }


    /**
     * Find and pull out a product in cart by its slug
     * 
     * @param cart the cart object to be searched
     * @param slug the product slug as an identifier
     * @return the product found in cart that matches the given slug. Returns null if product is not found
     */
    public static Map.Entry<Product, Integer> findItemInCart(HashMap<Product, Integer> cart, String slug) {
        Map.Entry<Product, Integer> result = null;
        for (Map.Entry<Product, Integer> product_entry : cart.entrySet()) {
            Product product = product_entry.getKey();

            if (product.getSlug().equals(slug)) {
                result = product_entry;
            }
        }

        return result;
    }

    private static void redirect(HttpServletRequest request, HttpServletResponse response, HashMap<Product, Integer> cart, HttpSession session, boolean cart_modified_success) throws IOException{
        if (cart_modified_success){
            session.setAttribute("cart", cart);
            session.setAttribute("cart_modified", Boolean.TRUE);
        } else {
            session.setAttribute("cart_modified", Boolean.FALSE);
        }
        response.sendRedirect(request.getContextPath() + "/cart.jsp");
    }
}
