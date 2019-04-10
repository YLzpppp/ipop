package com.lzp;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lzp.idb.Dao;

@WebServlet("/server/AllPorts")
public class AllPorts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static Dao dao = new Dao();
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<Integer> list = dao.QueryAllPorts();
		StringBuilder builder = new StringBuilder();
		for(int item : list) {
			builder.append(item+",");
		}
		String s = builder+"";
		System.out.println(s);
		response.getOutputStream().write(s.getBytes());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
