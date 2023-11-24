package com.taptapgo.servlets;

import java.io.IOException;
import com.taptapgo.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.commons.lang3.StringUtils;

@WebServlet(name = "signUpServlet", value = "/signup")
public class SignUpServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();

        try {
            // get request parameters for product sku and name
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            String passcode = request.getParameter("passcode");
            System.out.println(passcode.length());

            // check passcode validity
            if ((passcode.length() < 4) || !(StringUtils.isAlphanumeric(passcode))) {
                // output fail message
                response.sendRedirect(request.getContextPath() + "/register.jsp?create=fail");
                return;
            }
            else {
                // create new registered user and add to database and identity map
                User newUser = User.createRegisteredUser(firstName, lastName, phone, email, false);
                boolean userAddedToDB = newUser.addUserToDB(passcode);

                if (userAddedToDB) {
                    // set user related session attributes
                    session.setAttribute("isRegisteredUser", true);
                    session.setAttribute("registered_user", newUser);
                    // auto logout every 30 mins
                    session.setMaxInactiveInterval(30 * 60);
                }
                else {
                    response.sendRedirect(request.getContextPath() + "/register.jsp?create=fail");
                    return;
                }
            }
        } catch (Exception e) {
            // output message for failed product creation and redirect to products page
            response.sendRedirect(request.getContextPath() + "/register.jsp?create=fail");
            return;
        }

        // output message for successful product creation and redirect to products page
        response.sendRedirect("user-account.jsp");
    }
}