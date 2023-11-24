package com.taptapgo.servlets;

import com.taptapgo.*;
import com.taptapgo.repository.UserIdentityMap;
import com.taptapgo.repository.OrderIdentityMap;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "tapTapServlet", value = "/tap-tap-servlet")
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
}