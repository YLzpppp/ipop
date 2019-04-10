package com.lzp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lzp.idb.Dao;

public class Registerserver extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Dao dao = new Dao();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		dao.createTable();
		String user = request.getParameter("username");
		String pass = request.getParameter("password");
		String ip = request.getRemoteAddr();
		int port = Integer.valueOf(request.getParameter("port"));
		System.out.println("register : " +user+"  |  "+ pass +" | "+ip +" | "+port);
		if (dao.Exists(user)) {
			response.getOutputStream().write("already exists".getBytes());
		}else {
			dao.InsertNew(user, pass,ip,port);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
