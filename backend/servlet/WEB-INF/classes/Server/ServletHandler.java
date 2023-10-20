package Server;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServletHandler extends HttpServlet{
    public void init() throws ServletException{
        // Place for any initialization (load data into memory)
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
    }
}
