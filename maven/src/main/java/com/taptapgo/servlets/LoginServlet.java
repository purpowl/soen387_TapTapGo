package com.taptapgo.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import com.taptapgo.Staff;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    // staff password - universal
    private final String password = "secret";

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // get request parameters for password
        String pwd = request.getParameter("login-password");
        String previousPage = request.getParameter("from");

        if(password.equals(pwd)){
            // set the owner of session to staff
            // auto logout every 30 mins
            HttpSession session = request.getSession();
            session.setAttribute("isStaff", true);
            session.setMaxInactiveInterval(30 * 60);
            // redirect to previous page
            response.sendRedirect(previousPage);
        }else{
            // output wrong password message
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
            PrintWriter out= response.getWriter();
            out.println("<font color=red>Password is wrong.</font>");
            rd.include(request, response);
        }
    }
}