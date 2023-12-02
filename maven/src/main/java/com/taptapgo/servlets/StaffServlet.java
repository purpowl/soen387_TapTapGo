package com.taptapgo.servlets;

import java.io.IOException;

import com.taptapgo.repository.UserIdentityMap;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "staffServlet", value = "/staff")
public class StaffServlet extends HttpServlet{
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String action = request.getParameter("action");
        String userID = request.getParameter("userID");
        
        if (action.equals("revoke")) {
            UserIdentityMap.changePermission(userID, false);
            response.sendRedirect(request.getContextPath() + "/manage-user.jsp");
        } else if (action.equals("promote")) {
            UserIdentityMap.changePermission(userID, true);
            response.sendRedirect(request.getContextPath() + "/manage-user.jsp");
        }
    }
}
