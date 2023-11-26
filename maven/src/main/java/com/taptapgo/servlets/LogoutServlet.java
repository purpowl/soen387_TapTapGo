package com.taptapgo.servlets;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "logoutServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        // log staff out
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath());
    }
}
