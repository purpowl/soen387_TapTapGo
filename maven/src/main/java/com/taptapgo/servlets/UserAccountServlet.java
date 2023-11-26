package com.taptapgo.servlets;

import java.io.IOException;

import com.taptapgo.User;
import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.repository.UserIdentityMap;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "userAccountServlet", value = "/account")
public class UserAccountServlet extends HttpServlet{
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String userID = (String) request.getParameter("userID");
        String firstName = (String) request.getParameter("inputFirstName");
        String lastName = (String) request.getParameter("inputLastName");
        String phone = (String) request.getParameter("inputPhone");
        String email = (String) request.getParameter("inputEmailAddress");

        if(firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/personal-information.jsp?failureMessage=fieldEmpty");
            return;
        }

        boolean result = UserIdentityMap.updateUserInfo(userID, firstName, lastName, phone, email);
        if (result) {
            // Update session user
            HttpSession currentSession = request.getSession();
            User userAfterUpdate = null;
            try {
                userAfterUpdate = UserIdentityMap.getUserByID(userID);
            } catch(InvalidParameterException e) {
                e.printStackTrace();
                
                response.sendRedirect(request.getContextPath() + "/logout");
                return;
            }
            
            currentSession.setAttribute("registered_user", userAfterUpdate);
            response.sendRedirect(request.getContextPath() + "/personal-information.jsp?successMessage");
        } else {
            response.sendRedirect(request.getContextPath() + "/personal-information.jsp?failureMessage=dbFail");
        }
    }
}
