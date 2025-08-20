package com.zthapp;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.sql.*;


public class UserServlet extends HttpServlet {
    private static final String DB_URL = "jdbc:mysql://vlpsqldb01.zth.com:3306/appdb?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "appuser";
    private static final String DB_PASS = "h251zi8ORyuUdL0ZrDYI";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name, email FROM users");

            out.println("<html><body>");
            out.println("<h2>Users from MySQL Database:</h2>");
            out.println("<table border='1'><tr><th>ID</th><th>Name</th><th>Email</th></tr>");
            while (rs.next()) {
                out.println("<tr><td>" + rs.getInt("id") + "</td><td>" 
                    + rs.getString("name") + "</td><td>" 
                    + rs.getString("email") + "</td></tr>");
            }
            out.println("</table>");
            out.println("</body></html>");

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
// This servlet connects to a MySQL database and retrieves user data.
