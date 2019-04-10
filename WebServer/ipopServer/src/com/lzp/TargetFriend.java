package com.lzp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lzp.idb.Dao;

public class TargetFriend extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Dao dao = new Dao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String user = request.getParameter("username");
		if (dao.Exists(user)) {
			response.getOutputStream().write("true".getBytes());
		}else {
			response.getOutputStream().write("false".getBytes());
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
