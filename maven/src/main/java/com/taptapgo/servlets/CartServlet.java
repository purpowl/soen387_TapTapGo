package com.taptapgo.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.taptapgo.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "cartServlet", value = "/cart")
public class CartServlet extends HttpServlet{

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession currentSession = request.getSession();
        Object cartObject = currentSession.getAttribute("cart");
        HashMap<Product, Integer> cart;

        if (cartObject == null) {
            cart = loadCart(request);
        } else {
            cart = (HashMap<Product, Integer>) cartObject;
        }

        // request.setAttribute("cart", cart);
        // request.setAttribute("cart_modified", currentSession.getAttribute("cart_modified"));
        response.addCookie(archiveCart(request, cart));
        response.sendRedirect(request.getContextPath() + "/cart.jsp");
    }

    public static HashMap<Product, Integer> loadCart(HttpServletRequest request) {
        // Get cookies
        HashMap<Product, Integer> cart = new HashMap<Product, Integer>();
        Cookie[] cookies = request.getCookies();
        String cart_content_json = "[]";
        for(Cookie cookie : cookies){
            if (cookie.getName().equals("cart")) {
                cart_content_json = cookie.getValue();
            }
        }

        // Parse the cookie and create products to cart
        JSONArray cart_content = new JSONArray(cart_content_json);
        for (int i = 0; i < cart_content.length(); i++) {
            JSONObject cart_item = cart_content.getJSONObject(i);
            Product product = new Product(cart_item.getString("sku"), cart_item.getString("name"), cart_item.getString("description"), cart_item.getString("vendor"), cart_item.getString("slug"), Double.parseDouble(cart_item.getString("price")));
            int amount = Integer.parseInt(cart_item.getString("amount"));

            cart.put(product, amount);
        }

        HttpSession currentSession = request.getSession();
        currentSession.setAttribute("cart", cart);
        return cart;
    }

    public static Cookie archiveCart(HttpServletRequest request, HashMap<Product, Integer> cart) {
        Cookie[] cookies = request.getCookies();
        Cookie cart_cookie = null;

        // Get the cart cookie
        for (Cookie c : cookies) {
            if (c.getName().equals("cart")) {
                cart_cookie = c;
            }
        }

        // If cart cookie doesn't exist yet, create one
        if (cart_cookie == null) {
            cart_cookie = new Cookie("cart", null);
        }

        // Put all products into the cookie
        JSONArray cart_json = new JSONArray();
        for (Map.Entry<Product, Integer> product_entry : cart.entrySet()) {
            Product product = product_entry.getKey();
            int amount = product_entry.getValue();

            JSONObject cart_item = new JSONObject();
            cart_item.put("name", product.getName());
            cart_item.put("description", product.getDescription());
            cart_item.put("vendor", product.getVendor());
            cart_item.put("price", Double.toString(product.getPrice()));
            cart_item.put("sku", product.getSKU());
            cart_item.put("slug", product.getSlug());
            cart_item.put("amount", Integer.toString(amount));

            cart_json.put(cart_item);
        }

        cart_cookie.setValue(cart_json.toString());

        return cart_cookie;
    }
}
