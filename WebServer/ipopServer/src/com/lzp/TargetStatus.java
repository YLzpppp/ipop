package com.lzp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lzp.idb.Dao;


@WebServlet("/server/TargetStatus")
public class TargetStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static Dao dao = new Dao();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("username");
		int status = dao.QueryStatus(user);
		response.getOutputStream().write(String.valueOf(status).getBytes());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
