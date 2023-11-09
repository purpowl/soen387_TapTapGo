package com.taptapgo.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import com.taptapgo.Staff;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.taptapgo.repository.CustomerIdentityMap;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    // staff password - universal


    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // get request parameters for password
        String username = request.getParameter("login-username");
        String pwd = request.getParameter("login-password");
        String previousPage = request.getParameter("from");

        if(CustomerIdentityMap.authenticateCustomer(username, pwd)){
            // set the owner of session
            // auto logout every 30 mins
            if (CustomerIdentityMap.getCustomerbyUserName(username) != null) {
                HttpSession session = request.getSession();
                session.setAttribute("isRegisteredCustomer", true);
                session.setAttribute("registeredCustomer", CustomerIdentityMap.getCustomerbyUserName(username));
                session.setMaxInactiveInterval(30 * 60);
                response.sendRedirect("user-account.jsp");
            }
            else {
                HttpSession session = request.getSession();
                session.setAttribute("isStaff", true);
                session.setMaxInactiveInterval(30 * 60);
                response.sendRedirect(previousPage);
            }
        }else{
            // output wrong password message
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
            PrintWriter out= response.getWriter();
            out.println("<font color=red>Username or password is wrong.</font>");
            rd.include(request, response);
        }
    }
}