package com.lzp.idb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.sql.*;

public class Dao {
	public static final int Status_Online = 1;
	public static final int Status_Offline = 0;
	private Connection con;
	private String tableName = "Accounts";
	
	public Dao() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("/databaseInfo.properties");
		Properties po = new Properties();
		try {
			po.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String username = po.getProperty("username");
		String password = po.getProperty("password");
		String url = po.getProperty("connectUrl");
		String dri = po.getProperty("mysqlclass");
		
		try {
			Class.forName(dri);
			con = DriverManager.getConnection(url,username,password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void createTable() {
		String msql = "create table if not exists "+tableName+" ("
				+"id integer auto_increment,"
				+"username varchar(20) not null,"
				+"password varchar(20) not null, "
				+"ipaddress varchar(20),"
				+"port integer ,"
				+"status integer default 0,"
				+"primary key(id))default charset=utf8;";
		System.out.println(msql);
		try {
			con.createStatement().execute(msql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean Exists(String user) {
		String msql = "select * from "+tableName+" where username=\""+user+"\";";
		try {
			ResultSet re = con.createStatement().executeQuery(msql);
			if (re.next())
				return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
		
	}
	
	public String QueryPassword(String user) {
		String out = "";
		String msql = "select * from "+tableName+" where username="+ "\""+user+"\"" +"; ";
		try {
			ResultSet re = con.createStatement().executeQuery(msql);
			re.absolute(1);
			out = re.getString(3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public String QueryIpAddress(String user) {
		String ip = "";
		String msql = "select ipaddress from "+tableName+" where username = "+"\""+user+"\""+";";
		System.out.println(msql);
		try {
			ResultSet re = con.createStatement().executeQuery(msql);
			re.first();
			ip = re.getString("ipaddress");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ip;
	}
	
	public int QueryPort(String user) {
		int port = 0;
		String msql = "select port from "+tableName+" where username = "+"\""+user+"\""+";";
		try {
			ResultSet re = con.createStatement().executeQuery(msql);
			re.first();
			port = re.getInt("port");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return port ;
	}
	
	public int QueryStatus(String user) {
		int status = Status_Offline;
		String msql = "select status from "+tableName+" where username = "+"\""+user+"\";";
		try {
			ResultSet re = con.createStatement().executeQuery(msql);
			if(re.first()) {
				status = re.getInt("status");
			};
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public ArrayList<Integer> QueryAllPorts(){
		String msql = "select port from "+tableName+";";
		ArrayList<Integer> list = new ArrayList<>();
		try {
			ResultSet re = con.createStatement().executeQuery(msql);
			while(re.next()) {
				list.add(re.getInt("port"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public void InsertNew(String user,String passwd ,String ip,int port) {
		String msql = "insert into "+tableName+" ( username,password ,ipaddress,port) values (\""+ user +"\" , \""+passwd+"\" ,"+"\""+ip+"\""+",\""+port+"\""+" );";
		System.out.println(msql);
		try {
			con.createStatement().execute(msql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void UpdateIpAddress(String user , String ip) {
		String msql = "update "+tableName+" set ipaddress = "+"\""+ip+"\""+" where username= "+"\""+user+"\""+";";
		try {
			con.createStatement().execute(msql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void UpdateStatus(String user , int status) {
		String msql = "update "+tableName+" set status="+status+" where username = "+user+";";
		try {
			con.createStatement().execute(msql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void updatePassword(String user,String passwd) {
		String msql = "update "+tableName+" set password="+"\""+passwd+"\""+" where username="+"\""+user+"\""+"; ";
		try {
			con.createStatement().execute(msql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
