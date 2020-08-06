package com.todolist.controller;

import java.io.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.todolist.model.User;
import com.todolist.model.UserDAO;

@WebServlet("/register")
public class UserController extends HttpServlet {
	    private static final long serialVersionUID = 1L;
	    private UserDAO userDAO;

	    public void init() {
	        userDAO = new UserDAO();
	    }

	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	        register(request, response);
	    }

	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	        response.sendRedirect("register/register.jsp");
	    }

	    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	        String firstName = request.getParameter("firstName");
	        String lastName = request.getParameter("lastName");
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");

	        User employee = new User();
	        employee.setFirstName(firstName);
	        employee.setLastName(lastName);
	        employee.setUsername(username);
	        employee.setPassword(password);

	        try {
	            int result = userDAO.registerEmployee(employee);
	            if (result == 1) {
	                request.setAttribute("NOTIFICATION", "User Registered Successfully!");
	            }

	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        RequestDispatcher dispatcher = request.getRequestDispatcher("register/register.jsp");
	        dispatcher.forward(request, response);
	    
	}

}
