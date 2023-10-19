package servlet.Server;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// import servlet.Frontend_Elements.HomePage;;

@WebServlet("/test_servlet")
public class ServletHandler extends HttpServlet{
    public void init() throws ServletException{
        // Place for any initialization (load data into memory)
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        // HomePage homepage = new HomePage();

        out.println("<h1>Hello World</h1>");
    }
}
