package com.taptapgo.servlets;

import java.io.IOException;
import com.taptapgo.User;
import com.taptapgo.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.taptapgo.repository.UserIdentityMap;

@WebServlet(name = "loginServlet", value = "/login")
public class LoginServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // get request parameters for passcode
        String passcode = request.getParameter("login-password");

        // authenticate passcode
        String userID = UserIdentityMap.authenticate(passcode);

        if(userID != null){
            try {
                // get the user
                User user = UserIdentityMap.getUserByID(userID);
                HttpSession session = request.getSession();

                // set user related session attributes
                session.setAttribute("isRegisteredUser", true);
                session.setAttribute("registered_user", user);

                // set staff related session attributes
                if (user.isStaff()) {
                    session.setAttribute("isStaff", true);
                }
                else {
                    session.setAttribute("isStaff", null);
                }

                // auto logout every 30 mins
                session.setMaxInactiveInterval(30 * 60);

                // redirect to user account page
                response.sendRedirect("user-account.jsp");
            }
            catch (InvalidParameterException e) {
                e.printStackTrace();
            }
        }
        else{
            response.sendRedirect(request.getContextPath() + "/login.jsp?create=fail");
        }
    }
}