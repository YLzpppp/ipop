package com.lzp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lzp.idb.Dao;

public class Loginserver extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Dao dao = new Dao();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("username");
		String passwd = request.getParameter("password");
		if (dao.Exists(user)) {
			String p = dao.QueryPassword(user);
			String ip = request.getRemoteAddr();

			System.out.println("user:"+user+"\t"+"pass:"+passwd+"\t"+"ip:"+ip);
			
			if(p.equals(passwd) ) {
				dao.UpdateIpAddress(user, ip);
				dao.UpdateStatus(user, dao.Status_Online);
				response.getOutputStream().write("right".getBytes());
				
			}else {
				response.getOutputStream().write("wrong".getBytes());
			}
		}else{
			response.getOutputStream().write("account not exists".getBytes());
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
