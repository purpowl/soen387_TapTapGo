package com.taptapgo.servlets;

import java.io.IOException;
import com.taptapgo.User;
import com.taptapgo.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.taptapgo.repository.UserIdentityMap;
import org.apache.commons.lang3.StringUtils;

@WebServlet(name = "changePasscodeServlet", value = "/change-passcode")
public class ChangePasscodeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // get request parameters for passcode
        String oldpwd = request.getParameter("oldpwd");
        String newpwd = request.getParameter("newpwd");

        if ((newpwd.length() < 4) || !(StringUtils.isAlphanumeric(newpwd))) {
            // output fail message
            response.sendRedirect(request.getContextPath() + "/modify-pwd.jsp?modify=fail");
            return;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("registered_user");

        // authenticate old password
        if (user.getUserID().equals(UserIdentityMap.authenticate(oldpwd))) {
            try {
                // try to change passcode
                boolean passcodeChanged = user.setPasscode(newpwd);

                if (passcodeChanged) {
                    // output success message
                    response.sendRedirect(request.getContextPath() + "/modify-pwd.jsp?modify=success");
                }
                else {
                    // output fail message - new passcode is invalid
                    response.sendRedirect(request.getContextPath() + "/modify-pwd.jsp?modify=fail");
                }
            }
            catch (InvalidParameterException e) {
                e.printStackTrace();
                // output fail message
                response.sendRedirect(request.getContextPath() + "/modify-pwd.jsp?modify=fail");
                return;
            }
        }
        else {
            // output fail message
            response.sendRedirect(request.getContextPath() + "/modify-pwd.jsp?modify=fail");
        }
    }
}