package com.taptapgo.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.taptapgo.repository.UserIdentityMap;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // get request parameters for password
        String passcode = request.getParameter("passcode");
        String previousPage = request.getParameter("from");

//        if(UserIdentityMap.authenticateCustomer(pwd)){
//            // set the owner of session
//            // auto logout every 30 mins
//            if (UserIdentityMap.getCustomerbyUserName(username) != null) {
//                HttpSession session = request.getSession();
//                session.setAttribute("isStaff", null);
//                session.setAttribute("staff", null);
//                session.setAttribute("isRegisteredCustomer", true);
//                session.setAttribute("registered_user", UserIdentityMap.getCustomerbyUserName(username));
//                session.setMaxInactiveInterval(30 * 60);
//                response.sendRedirect("user-account.jsp");
//            }
//            else {
//                HttpSession session = request.getSession();
//                session.setAttribute("isRegisteredCustomer", null);
//                session.setAttribute("registered_user", null);
//                session.setAttribute("isStaff", true);
//                session.setAttribute("staff", StaffRepository.readByUsername(username));
//                session.setMaxInactiveInterval(30 * 60);
//                response.sendRedirect(previousPage);
//            }
//        }else{
//            // output wrong password message
//            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.jsp");
//            PrintWriter out= response.getWriter();
//            out.println("<font color=red>Username or password is wrong.</font>");
//            rd.include(request, response);
//        }
    }
}