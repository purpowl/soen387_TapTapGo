package com.taptapgo.servlets;

import java.io.IOException;
import java.sql.Date;

import com.taptapgo.Order;
import com.taptapgo.repository.OrderIdentityMap;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "shipOrderServlet", value = "/shipOrder/*")
public class ShipOrderServlet extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{

    }
}